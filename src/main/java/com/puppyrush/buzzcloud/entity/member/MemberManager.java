
package com.puppyrush.buzzcloud.entity.member;


import java.sql.*;
import java.util.*;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.puppyrush.buzzcloud.dbAccess.DBManager;
import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberStandard;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberState;
import com.puppyrush.buzzcloud.mail.MailManager;
import com.puppyrush.buzzcloud.mail.PostMan;
import com.puppyrush.buzzcloud.mail.PostManImple;
import com.puppyrush.buzzcloud.mail.enumMail;
import com.puppyrush.buzzcloud.mail.enumMailState;
import com.puppyrush.buzzcloud.mail.enumMailType;
import com.puppyrush.buzzcloud.mail.PostManImple.Builder;
import com.puppyrush.buzzcloud.page.enums.enumPage;
import com.puppyrush.buzzcloud.property.ConnectMysql;

@Service("memberManager")
public final class MemberManager {
	
	private static Connection conn = ConnectMysql.getConnector();
	
	@Autowired
	private MemberDB mDB;
	
	@Autowired
	private MemberController mCtl;

	@Autowired
	private DBManager dbMng;
	
	@Autowired
	private PostMan postman;
	
	@Autowired
	private MailManager mailMng;
	
	
	
	/**
	 * 	가입 후 인증메일로 받은 email과 인증번호를 비교하여 결과를 반환한다.
	 * @param email 가입당시 사용한 메
	 * @param hashedUUID  가입인증을 위해 발급된 인증번호(해시된 비번)
	 * @return
	 * @throws Throwable 
	 */
	public  boolean resolveCertificateJoin(String sId, String email, String hashedUUID) throws Throwable {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		if(mDB.isJoin(email) == false)
			throw (new EntityException.Builder(enumPage.JOIN))
			.errorCode(enumMemberState.NOT_JOIN).build(); 
		
		try{
			
			conn.setAutoCommit(false);
			
			Member member;
			if(mCtl.containsEntity(sId))
				member = mCtl.getMember(sId);
			else{
				member = mDB.getMember(email);
				mCtl.addMember(member, sId);
			}
			 
			int memberId = member.getId();
			
			ps= conn.prepareStatement("select certificationNumber,mailId from joinCertification where memberId = ?");
			ps.setInt(1, memberId);
			rs = ps.executeQuery();
			
			if(rs.next()==false)
				throw new SQLException(email+","+memberId+"joinCertification테이블에 id가 둘 이상 존재하거나 한개도 존재하지 않습니다.");
			 			
			String planeUUID = rs.getString("certificationNumber");
			int mailId = rs.getInt("mailId");
			
			ps.close();
			rs.close();
						
			if(BCrypt.checkpw( planeUUID, hashedUUID )){
				
				ps = conn.prepareStatement("delete from mail where mailId = ?");
				ps.setInt(1, mailId);
				ps.executeUpdate();
				ps.close();
				
				ps = conn.prepareStatement("update memberState set joinCertification = "+ enumMemberState.RESOLVE_JOIN.toInt() +" where memberId = ?");
				ps.setInt(1, memberId);
				ps.executeUpdate();
				ps.close();
				
				ps = conn.prepareStatement("select nickname from member where memberId = ?");
				ps.setInt(1, member.getId());
				rs = ps.executeQuery();
				rs.next();
				String name = rs.getString(1);
				member.setNickname(name);
				
								
				String subject = "버즈클라우드에 가입하실것을 환영합니다.";
				String content = member.getNickname() + "의 버즈클라우드 가입을 축하드립니다. 버즈클라우드 서비스의 사용방법은 사이트를 참고해주세요. 감사합니다.";
				
				Builder bld = new PostManImple.Builder(enumMail.gmailID.toString(), email).subject(subject).content(content).build();
				postman.send(bld);
				
				conn.commit();
			}else
				throw new SQLException();
		
			
		}finally{
			
		}
		
		return true;
	}
	
	public  boolean requestCertificateJoin(Member member) throws EntityException, NumberFormatException, AddressException, SQLException, MessagingException{
		
		PreparedStatement ps;
		
		String certificationNumber = mailMng.SendCertificationMail(member, enumMailType.JOIN);
		
		List<String> col = new ArrayList<String>();
		col.add("memberId");
		col.add("certificationNumber");
		
		List<List<Object>> values = new ArrayList<List<Object>>();
		List<Object> list = new ArrayList<Object>();
		list.add(member.getId());
		list.add(certificationNumber);
		values.add(list);
		
		dbMng.insertColumn("joinCertification", col, values);		
		return true;
	}
	
	public void updatePassword(int id, String pw) throws ControllerException, SQLException{
		
		if(mCtl.containsEntity(id)){
			Member member = mCtl.getEntity(id);
			member.setPlanePassword(pw);
			
		}
		
		String newPw = BCrypt.hashpw( pw, BCrypt.gensalt(12));
		
		Map<String, Object> set = new HashMap<String, Object>();
		Map<String, Object> where = new HashMap<String, Object>();
		
		set.put("password", newPw);
		where.put("memberId", id);
		
		dbMng.updateColumn("member", set, where);
		
	}

	public void setLostPassword(String email) throws SQLException, AddressException, MessagingException, NumberFormatException, EntityException, ControllerException{
		
		
		Map<String, Object> where = new HashMap<String, Object>();
		Map<String, Object> set = new HashMap<String, Object>();
	
		String certificationNumber = mailMng.SendCertificationMail(mDB.getMember(email), enumMailType.LOST_PASSWORD);

		int id = mDB.getIdOfEmail(email);
		where.put("memberId", id);
		set.put("isAbnormal", 1);
		set.put("LostPassword", 1);
		
		dbMng.updateColumn("memberState", set, where);		
		set.clear();
		
		set.put("password", certificationNumber);
		dbMng.updateColumn("member", set, where);
		
		List<String> col = new ArrayList<String>();
		List<List<Object>> values = new ArrayList<List<Object>>();
		col.add("memberId");
		col.add("temporaryPassword");
		col.add("date");
		
		List<Object> val = new ArrayList<Object>();
		val.add(id);
		val.add(certificationNumber);
		val.add(new Timestamp(System.currentTimeMillis()));
		values.add(val);
		dbMng.insertColumn("lostPassword", col, values);
		
		
	}
	
}



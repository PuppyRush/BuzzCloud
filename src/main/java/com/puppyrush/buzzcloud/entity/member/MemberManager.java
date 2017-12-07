
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
	
	public  boolean isOverDateOfChangePassword(int uId) throws SQLException{
		
		PreparedStatement ps = conn.prepareStatement("lastModifiedPasswordDate from memberDetail where memberId = ?");
		ps.setInt(1, uId);
		ResultSet rs = ps.executeQuery();
		rs.next();
		
		Timestamp time = rs.getTimestamp("lastModifiedPasswordDate");
															 
		Date today = new Date ( );
		Calendar cal = Calendar.getInstance ( );
		cal.setTime ( today );// 오늘로 설정. 
		 
		 
		Calendar cal2 = Calendar.getInstance ( );
		cal2.setTime(time);
	 
		 
		int count = 0;
		while ( !cal2.after ( cal ) )
		{
			count++;
			cal2.add ( Calendar.MONTH, 1 ); // 다음날로 바뀜					
		}
		
		int stdDate = Integer.valueOf( enumMemberStandard.PASSWD_CHANGE_DATE_OF_MONTH.toString());
		//인증메일을 보낸지 24시간이 아직 경과 하지 않았는가?
		//경과하지않음.
		if(stdDate > count)
			return false;
		
		return true;
	
	}
	

	
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
	
	public  boolean requestCertificateJoin(Member member) throws EntityException{
		
	
		PreparedStatement ps;
		
		try{
		
			conn.setAutoCommit(false);

			String _uuid =  UUID.randomUUID().toString();
			String hashedUUID =  BCrypt.hashpw(_uuid, BCrypt.gensalt(12));
			
			if(mDB.isCertificatingJoin(member.getId())){
				
				ps = conn.prepareStatement("delete from mail where memberId = ?");
				ps.setInt(1, member.getId());
				ps.executeUpdate();
				ps.close();
				conn.commit();
				
			}
			
			String _fullUrl = new StringBuilder(enumPage.ROOT.toString()).append("/mail/join.do")
					.append("?email=").append(member.getEmail()).append("&number=").append(hashedUUID).toString();
					
			String subject = "버즈클라우드의 가입 인증메일 입니다.";
			String content = new StringBuilder(
					"안녕하세요.  회원님의 가입 인증을 위해 다음 url에 접속하시면 가입이 마무리됩니다. 만일 가입하지 않으셨는데 메일이 도착하셨다면 관리자에 문의 하시기 바랍니다.\n\n인증URL : ")
							.append(_fullUrl).toString();
			
			Builder bld = new PostManImple.Builder(enumMail.gmailID.toString(), member.getEmail()).subject(subject).content(content).build();
			List<Integer> key = postman.send(bld);
			
			ps = conn.prepareStatement("insert into joinCertification (memberId,mailId, certificationNumber) values(?,?,?)" );
			ps.setInt(1,member.getId() );
			ps.setInt(2, key.get(0));
			ps.setString(3, _uuid);
			ps.executeUpdate();
			ps.close();
			conn.commit();
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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

	public void setLostPassword(String email) throws SQLException, AddressException, MessagingException{
		
		
		Map<String, Object> where = new HashMap<String, Object>();
		Map<String, Object> set = new HashMap<String, Object>();
		String tempPw = getTemporaryPassword();
		String hashedPw = BCrypt.hashpw(tempPw, BCrypt.gensalt());
		
		int id = mDB.getIdOfEmail(email);
		where.put("memberId", id);
		set.put("isAbnormal", 1);
		set.put("LostPassword", 1);
		
		dbMng.updateColumn("memberState", set, where);		
		set.clear();
		
		set.put("password", hashedPw);
		dbMng.updateColumn("member", set, where);
		
		List<String> col = new ArrayList<String>();
		List<List<Object>> values = new ArrayList<List<Object>>();
		col.add("memberId");
		col.add("temporaryPassword");
		col.add("date");
		
		List<Object> val = new ArrayList<Object>();
		val.add(id);
		val.add(hashedPw);
		val.add(new Timestamp(System.currentTimeMillis()));
		values.add(val);
		dbMng.insertColumn("lostPassword", col, values);
		
		sendMail(tempPw, email);
		
	}
	

	private void sendMail(String temporaryPw, String to) throws AddressException, MessagingException{
		
		String subject = "[BuzzCloud]요청하신 임시비밀 번호 입니다.";
		String content = "비밀번호 분실로 임시 비밀번호를 보냅니다. 유효기간은 하루동안이니 이 안에 로그인 하시기 바랍니다.\n임시비밀번호 : " + temporaryPw; 
		
		postman.send(new PostManImple.Builder(enumMail.gmailID.toString(), to).subject(subject).content(content).build());
	
		
	}
	
	private String getTemporaryPassword(){
		final int numbers = '9'-'0'+1;
		final int letters = 'Z'-'A'+1;
		StringBuilder tempPW = new StringBuilder("");
		for(int i=0 ; i < 6; i++){
			if(new Random().nextBoolean()){
				tempPW.append((char)(new Random().nextInt(letters)+'A'));
			}
			else
				tempPW.append((char)(new Random().nextInt(numbers)+'0'));
		}
		return tempPW.toString();
	}
}



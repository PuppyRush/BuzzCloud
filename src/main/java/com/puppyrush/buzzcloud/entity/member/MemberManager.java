
package com.puppyrush.buzzcloud.entity.member;


import java.sql.*;
import java.util.*;
import java.util.Date;

import javax.mail.MessagingException;
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
import com.puppyrush.buzzcloud.mail.enumMailType;
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
	
	/**
	 * 
	 * 
	 * @param member
	 * @return	3개월 이상 지났다면 true 아니면 false
	 * @throws SQLException 
	 */
	public  boolean isPassingDateOfMail(String email, enumMailType mailType) throws SQLException{
		
		int memberId = mDB.getIdOfEmail(email);
		
		PreparedStatement ps = conn.prepareStatement("select sendedDate from mail where memberId = ? and certificationKind=?");
		ps.setInt(1,memberId);
		ps.setInt(2,Integer.valueOf(mailType.toString()));
		ResultSet _rs = ps.executeQuery();
		_rs.next();		
		
		Timestamp time = _rs.getTimestamp("sendedDate");
		//int dateGap = Calendar.getInstance().get
																 
		Date today = new Date ( );
		Calendar cal = Calendar.getInstance ( );
		cal.setTime ( today );// 오늘로 설정. 
		 
		 
		Calendar cal2 = Calendar.getInstance ( );
		cal2.setTime(time);
	 
		 
		int count = 0;
		while ( !cal2.after ( cal ) )
		{
			count++;
			cal2.add ( Calendar.DATE, 1 ); // 다음날로 바뀜					
		}
		
		int stdDate = Integer.valueOf( enumMemberStandard.RESEND_STANDRATE_DATE.toString());
		//인증메일을 보낸지 24시간이 아직 경과 하지 않았는가?
		//경과하지않음.
		if(stdDate > count)
			return false;
				
		return true;
					
	}

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
	 * 	비밀번호 분실 경우 메일을 보낼것인가 인증번호 입력페이지로 갈 것인가를 결정하는 함수.	  
	 * 
	 * @param member
	 * @return	true if redirect to inputmailPage, false if to inputAuthnumPage
	 * @throws SQLException 
	 * @throws NumberFormatException 
	 */
	public  boolean isSendedmail(String email, enumMailType mailType) throws NumberFormatException, SQLException{
		
		//이미 전송하였는가?
		int memberId = mDB.getIdOfEmail(email);
		boolean isAlreadySend=false;
		PreparedStatement ps =conn.prepareStatement("select isSendedMail, sendedMailDate from mail where memberId = ? and certificationKind = ?");
		ps.setInt(1, memberId);
		ps.setInt(2, Integer.valueOf(mailType.toString()));
		ResultSet _rs = ps.executeQuery();
		_rs.next();
		
		isAlreadySend = _rs.getInt("isSendedMail")==1 ? true : false;
		return isAlreadySend;
		
	}
	
	/**
	 * 	가입 후 인증메일로 받은 email과 인증번호를 비교하여 결과를 반환한다.
	 * @param email 가입당시 사용한 메
	 * @param hashedUUID  가입인증을 위해 발급된 인증번호(해시된 비번)
	 * @return
	 * @throws Exception 
	 * @throws Throwable 
	 */
	public  boolean resolveCertificateJoin(String sId, String email, String hashedUUID) throws Exception {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		if(mDB.isJoin(email) == false)
			throw new EntityException(enumMemberState.NOT_JOIN, enumPage.ERROR404);
		
		try{
			
			conn.setAutoCommit(false);
			
			Member member;
			if(mCtl.containsEntity(sId))
				member = mCtl.getMember(sId);
			else{
				member = mDB.getMember(email);
				mCtl.addMember(member, sId);
			}
			 
			int _id = member.getId();
			
			ps= conn.prepareStatement("select certificationNumber from mail where memberId = ? and certificationKind = ? ");
			ps.setInt(1, _id);
			ps.setInt(2, Integer.parseInt(enumMailType.JOIN.toString()) );
			rs = ps.executeQuery();
			
			if(rs.next()==false)
				throw new SQLException(email+","+_id+"joinCertification테이블에 id가 둘 이상 존재하거나 한개도 존재하지 않습니다.");
			 			
			String planeUUID = rs.getString(1);
			
			ps.close();
			rs.close();
						
			if(BCrypt.checkpw( planeUUID, hashedUUID )){
				
				ps = conn.prepareStatement("delete from mail where memberId = ? and certificationKind = ? ");
				ps.setInt(1, _id);
				ps.setInt(2, Integer.parseInt(enumMailType.JOIN.toString()));
				ps.executeUpdate();
				ps.close();
				
				ps = conn.prepareStatement("update memberState set joinCertification = 0 where memberId = ?");
				ps.setInt(1, _id);
				ps.executeUpdate();
				ps.close();
				
				ps = conn.prepareStatement("select nickname from member where memberId = ?");
				ps.setInt(1, member.getId());
				rs = ps.executeQuery();
				rs.next();
				String name = rs.getString(1);
				member.setNickname(name);
				
				
				////////userDetail table
				
				Timestamp date = new Timestamp(System.currentTimeMillis());
				ps = conn.prepareStatement("update memberDetail set lastModifiedPasswordDate =? , lastLogoutDate = ? where memberId = ?");				
				ps.setInt(3,_id);
				ps.setTimestamp(1, date);
				ps.setTimestamp(2, date);
				
				ps.executeUpdate();
					
				ps.close();
				rs.close();
				
				String subject = "버즈클라우드에 가입하실것을 환영합니다.";
				String content = member.getNickname() + "의 버즈클라우드 가입을 축하드립니다. 버즈클라우드 서비스의 사용방법은 사이트를 참고해주세요. 감사합니다.";
				
				PostMan man = new PostManImple.Builder(enumMail.gmailID.toString(), member.getEmail()).subject(subject).content(content).build();
				man.send();
				
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
				
				ps = conn.prepareStatement("delete from mail where memberId = ? and certificationKind = ?");
				ps.setInt(1, member.getId());
				ps.setInt(2, Integer.valueOf(enumMailType.JOIN.toString()));
				ps.executeUpdate();
				conn.commit();
				
			}
			
			ps = conn.prepareStatement("insert into mail (memberId, certificationNumber, certificationKind) values(?,?,?)" );
			ps.setInt(1,member.getId() );
			ps.setString(2, _uuid);
			ps.setInt(3, Integer.parseInt(enumMailType.JOIN.toString()) );	
			ps.executeUpdate();
		
			
			String _fullUrl = new StringBuilder(enumPage.ROOT.toString()).append("/mail/join.do")
			.append("?email=").append(member.getEmail()).append("&number=").append(hashedUUID).toString();
			
			String subject = "버즈클라우드의 가입 인증메일 입니다.";
			String content = new StringBuilder(
					"안녕하세요.  회원님의 가입 인증을 위해 다음 url에 접속하시면 가입이 마무리됩니다. 만일 가입하지 않으셨는데 메일이 도착하셨다면 관리자에 문의 하시기 바랍니다.\n\n인증URL : ")
							.append(_fullUrl).toString();
			
			ps.close();
			
			PostMan man = new PostManImple.Builder(enumMail.gmailID.toString(), member.getEmail()).subject(subject).content(content).build();
			man.send();
			
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

	public void setLostPassword(String email, int mailId, String tempPW) throws SQLException{
		
		
		Map<String, Object> where = new HashMap<String, Object>();
		Map<String, Object> set = new HashMap<String, Object>();
		String hashedPw = BCrypt.hashpw(tempPW, BCrypt.gensalt());
		
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
		col.add("mailId");
		col.add("temporaryPassword");
		col.add("date");
		
		List<Object> val = new ArrayList<Object>();
		val.add(id);
		val.add(hashedPw);
		val.add(mailId);
		val.add(new Timestamp(System.currentTimeMillis()));
		values.add(val);
		dbMng.insertColumn("lostPassword", col, values);
		
		
	}
}



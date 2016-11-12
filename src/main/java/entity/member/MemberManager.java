
package entity.member;


import java.sql.*;
import java.util.*;
import java.util.Date;
import org.mindrot.jbcrypt.BCrypt;

import entity.EntityException;
import entity.enumEntityState;
import entity.member.enums.enumMemberAbnormalState;
import entity.member.enums.enumMemberStandard;
import entity.member.enums.enumMemberState;
import entity.member.enums.enumMemberType;
import property.ConnectMysql;
import property.enums.enumSystem;
import mail.PostMan;
import mail.enumMailType;
import page.PageException;
import page.enums.enumPage;
import page.enums.enumPageError;

public final class MemberManager {
	
	private static Connection conn = ConnectMysql.getConnector();

	private MemberManager() {
	}


	private static class Singleton {
		private static final MemberManager instance = new MemberManager();
	}
	
	public static MemberManager getInstance () {
		return Singleton.instance;
	}
	
	

	/**
	 * 
	 * 
	 * @param member
	 * @return	3개월 이상 지났다면 true 아니면 false
	 * @throws SQLException 
	 */
	public  boolean isPassingDateOfMail(String email, enumMailType mailType) throws SQLException{
		
		int memberId = MemberDB.getInstance().getIdOfEmail(email);
		
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
		int memberId = MemberDB.getInstance().getIdOfEmail(email);
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
		
		PreparedStatement _ps = null;
		ResultSet _rs = null;
		
		if(!MemberDB.getInstance().isMember(sId))
			throw new EntityException(enumMemberState.NOT_JOIN, enumPage.ERROR404);
		
		try{
			
			conn.setAutoCommit(false);
			
			Member member = MemberController.getInstance().getMember(sId);
			 
			int _id = member.getId();
			
			 _ps= conn.prepareStatement("select certificationNumber from joinCertification where memberId = ? and certificationType = ? ");
			_ps.setInt(1, _id);
			_ps.setInt(2, Integer.parseInt(enumMailType.JOIN.toString()) );
			_rs = _ps.executeQuery();
			
			if(!_rs.next())
				throw new SQLException(email+","+_id+"joinCertification테이블에 id가 둘 이상 존재하거나 한개도 존재하지 않습니다.");
			 			
			String planeUUID = _rs.getString(1);
			
			if(BCrypt.checkpw( planeUUID, hashedUUID )){
				
				_ps = conn.prepareStatement("delete from joinCertification where memberId = ? and certificationType = ? ");
				_ps.setInt(1, _id);
				_ps.setInt(2, Integer.parseInt(enumMailType.JOIN.toString()));
				_ps.executeUpdate();
				
				_ps = conn.prepareStatement("update memberState set joinCertification = 0 where memberId = ?");
				_ps.setInt(1, _id);
				_ps.executeUpdate();

				
				////////userDetail table
				
				Timestamp date = new Timestamp(System.currentTimeMillis());
				_ps = conn.prepareStatement("update memberDetail set lastModifiedPasswordDate =? , lastLogoutDate = ? where memberId = ?");				
				_ps.setInt(3,_id);
				_ps.setTimestamp(1, date);
				_ps.setTimestamp(2, date);
				
				_ps.executeUpdate();
					
				PostMan.sendWelcomeMail(member.getNickname(), member.getEmail());
				
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
			
			if(MemberDB.getInstance().isCertificatingJoin(member.getId())){
				
				ps = conn.prepareStatement("delete from mail where memberId = ? and certificationType = ?");
				ps.setInt(1, member.getId());
				ps.setInt(2, Integer.valueOf(enumMailType.JOIN.toString()));
				ps.executeUpdate();
				conn.commit();
				
			}
			
			ps = conn.prepareStatement("insert into mail (memberId, certificationNumber, certificationType) values(?,?,?)" );
			ps.setInt(1,member.getId() );
			ps.setString(2, _uuid);
			ps.setInt(3, Integer.parseInt(enumMailType.JOIN.toString()) );	
			ps.executeUpdate();
		
			
			String _fullUrl = new StringBuilder(enumPage.ROOT.toString()).append(enumPage.MAIL.toString()).append("?email=").append(member.getEmail()).
					append("&number=").append(hashedUUID).append("&kind=").append(enumMailType.JOIN.toString()).toString();
			
			ps.close();
			
			if(PostMan.sendCeriticationJoin(member.getEmail(), _fullUrl )){
				conn.commit();
				
			}
			else{
				conn.rollback();
				
				return false;
			}
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}
		
		return true;
		
	}
	


}



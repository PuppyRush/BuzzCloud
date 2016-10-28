
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
	
	
	public  EnumMap<enumMemberAbnormalState, Boolean> getMemberAbnormalStates(int memberId) throws Throwable{

		PreparedStatement ps = null;
		ResultSet rs = null;
		EnumMap<enumMemberAbnormalState, Boolean> stateMap = new EnumMap<>(enumMemberAbnormalState.class);

		ps = conn.prepareStatement("select * from memberState where memberId = ?");
		ps.setInt(1, memberId);			
		rs = ps.executeQuery();
		rs.next();
	
		if(rs.getInt("isAbnormal")==1){
		
			for(enumMemberAbnormalState e : enumMemberAbnormalState.values()){												
				String _attributeName = e.getString();
				if(rs.getInt(_attributeName)==1)
					stateMap.put(e, true);
				else
					stateMap.put(e, false);
			}
		}

		return stateMap;		
	}

	/**
	 * 
	 * 
	 * @param member
	 * @return	3개월 이상 지났다면 true 아니면 false
	 * @throws SQLException 
	 */
	public  boolean isPassingDateOfMail(String email, enumMailType mailType) throws SQLException{
		
		int memberId = emailToIdFromDB(email);
		
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
		int memberId = emailToIdFromDB(email);
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
		
		if(!isMember(sId))
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
			
			if(isCertificatingJoin(member.getId())){
				
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
	

	public  boolean changePassword(Member member, String newPassword) throws Throwable{
				
		PreparedStatement _sp = conn.prepareStatement("update member set password = ? where memberId = ?");
		
		String _hashPassword = BCrypt.hashpw( newPassword, BCrypt.gensalt());
		_sp.setString(1, _hashPassword);
		_sp.setInt(2, member.getId());
				
		return true;
		
	}

	public  void withdrawMember(int uId, String nickname, String email, String reason) throws SQLException{
		
		PreparedStatement _ps= null;
		try{
			
			conn.setAutoCommit(false);
		
		
			_ps= conn.prepareStatement("delete from member where memberId = ?");
			_ps.setInt(1, uId);
			_ps.executeQuery();			
			
			PostMan.sendWithdraw(nickname, email, reason);
			
			conn.commit();
		}finally{
			
			_ps.close();
		}
		
	}
	
	public  ArrayList<Member> getAllMember(){
		
		ArrayList<Member> _members = new ArrayList<Member>();
		PreparedStatement _ps=null, __ps=null;
		ResultSet _rs=null, __rs=null;
		
		try{
			
			 _ps = conn.prepareStatement("select * from member");
			 _rs = _ps.executeQuery();
			
			while(_rs.next()){
											
			
							
				__ps = conn.prepareStatement("select * from memberState where memberId = ?");
				__ps.setInt(1, _rs.getInt("memberId"));
				__rs = __ps.executeQuery();
				__rs.next();
				
				EnumMap<enumMemberAbnormalState, Boolean> state = new EnumMap<>(enumMemberAbnormalState.class);
				
				
				if(__rs.getInt("isAbnormal")==1){
		
					
					if(__rs.getInt(enumMemberAbnormalState.LOST_PASSWORD.getString())==1)
						state.put(enumMemberAbnormalState.LOST_PASSWORD, true);
					else
						state.put(enumMemberAbnormalState.LOST_PASSWORD, false);
					
					if(__rs.getInt(enumMemberAbnormalState.FAILD_LOGIN.getString())==1)
						state.put(enumMemberAbnormalState.FAILD_LOGIN, true);
					else
						state.put(enumMemberAbnormalState.FAILD_LOGIN, false);
					
					if(__rs.getInt(enumMemberAbnormalState.SLEEP.getString())==1)
						state.put(enumMemberAbnormalState.SLEEP, true);
					else
						state.put(enumMemberAbnormalState.SLEEP, false);
					
					if(__rs.getInt(enumMemberAbnormalState.OLD_PASSWORD.getString())==1)
						state.put(enumMemberAbnormalState.OLD_PASSWORD, true);
					else
						state.put(enumMemberAbnormalState.OLD_PASSWORD, false);
					
					if(__rs.getInt(enumMemberAbnormalState.JOIN_CERTIFICATION.getString())==1)
						state.put(enumMemberAbnormalState.JOIN_CERTIFICATION, true);
					else
						state.put(enumMemberAbnormalState.JOIN_CERTIFICATION, false);
				}
		
				Member member = (new Member.Builder(_rs.getInt("memberId"), _rs.getString("email"))).registrationKind(enumMemberType.valueOf(_rs.getString("idType"))  )
						.nickname(_rs.getString("nickname")).abnormalState(state).build();
				
				_members.add(member);
				
			}
			
		
		}catch(Throwable e){
			
		}finally{
			if (_ps != null)
				try {
					_ps.close();
				} catch (SQLException ex) {
				}
			if (_rs != null)
				try {
					_rs.close();
				} catch (SQLException ex) {
				}
			if (__ps != null)
				try {
					__ps.close();
				} catch (SQLException ex) {
				}
			if (__rs != null)
				try {
					__rs.close();
				} catch (SQLException ex) {
				}
		}
		return _members;
	}
	

	public  boolean isMember(int uId) throws SQLException{
		
		PreparedStatement _ps = null;
		ResultSet _rs = null;
		
		_ps = conn.prepareStatement("select * from member where memberId = ?");
		_ps.setInt(1, uId);
		_rs = _ps.executeQuery();
		
		if(_rs.next())			
			return true;
			
		else
			return false;
		
	}
	
	public  boolean isMember(String email) throws SQLException{
		
		PreparedStatement _ps = null;
		ResultSet _rs = null;
		
		_ps = conn.prepareStatement("select * from member where email = ?");
		_ps.setString(1, email);
		_rs = _ps.executeQuery();
		
		if(_rs.next())			
			return true;
			
		else
			return false;
		
	}
	
	private  int emailToIdFromDB(String email) throws SQLException{
		
		PreparedStatement ps = conn.prepareStatement(" select memberId from member where email = ? ");
		ps.setString(1,"email");
		ResultSet rs = ps.executeQuery();
		rs.next();
		return rs.getInt("memberId");		
		
	}

	private  boolean isCertificatingJoin(int uId) throws EntityException{
		
		boolean isDoing = false;
		
		try{
				
			PreparedStatement ps = conn.prepareStatement(" select * from memberState where memberId = ? ");
			ps.setInt(1, uId);
			ResultSet rs = ps.executeQuery();
			
			if(!rs.next())
				throw new EntityException(enumMemberState.NOT_JOIN, enumPage.JOIN);
						
			if(rs.getInt("certificationJoin")==1){
				
				ps.close();
				rs.close();
				
				ps = conn.prepareStatement("select * from mail where memberId = ?, certificationKind = ?");
				ps.setInt(1, uId);
				ps.setInt(2, Integer.valueOf(enumMailType.JOIN.toString()));
				rs = ps.executeQuery();
				
				if(rs.next())					
					isDoing = true;
				else
					isDoing = false;
				
			}else
				throw new EntityException("가입 인증 메일을 보낸 상태입니다. 인증메일을 다시 요청하고 싶으시면 같은 메일로 다시 가입 하세요.",
						enumMemberState.ALREADY_CERTIFICATION, enumPage.LOGIN);

			
		}catch(SQLException e){
			
			e.printStackTrace();
			
		}
		
		return isDoing;
	}
	
	public  Member getMember(int memberId){
		
		PreparedStatement ps = null;
		ResultSet rs= null;
		Member member = null;
		
		try{
			
			ps = conn.prepareStatement("select * from member where memberId = ?");
			ps.setInt(1, memberId);
			rs = ps.executeQuery();
			if(!rs.next())
				throw new EntityException(enumEntityState.NOT_EXIST_IN_MAP);
			
			String email = rs.getString("email");
			EnumMap<enumMemberAbnormalState, Boolean> state = getMemberAbnormalStates(memberId);
			enumMemberType idType = enumMemberType.valueOf(rs.getString("registrationKind"));
			String nickname = rs.getString("nickname");
			
			member = new Member.Builder(memberId,email).abnormalState(state).registrationKind(idType).nickname(nickname).build();
					
		}catch(SQLException e){
			e.printStackTrace();
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
			try {
				ps.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return member;
	}
	
	public Member getMember(String email){
			
		PreparedStatement ps = null;
		ResultSet rs= null;
		Member member = null;
		
		try{
			
			ps = conn.prepareStatement("select * from member where email = ?");
			ps.setString(1, email);
			rs = ps.executeQuery();
			if(!rs.next())
				throw new EntityException(enumEntityState.NOT_EXIST_IN_MAP);
			
			int memberId= rs.getInt("memberId");
			EnumMap<enumMemberAbnormalState, Boolean> state = getMemberAbnormalStates(memberId);
			enumMemberType idType = enumMemberType.valueOf(rs.getString("registrationKind"));
			String nickname = rs.getString("nickname");
			
			member = new Member.Builder(memberId,email).abnormalState(state).registrationKind(idType).nickname(nickname).build();
					
		}catch(SQLException e){
			e.printStackTrace();
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
			try {
				ps.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return member;
	}
		
}



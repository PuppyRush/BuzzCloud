
package member;


import java.sql.*;
import java.util.*;
import java.util.Date;
import org.mindrot.jbcrypt.BCrypt;

import property.ConnectMysql;
import property.enums.enumSystem;
import mail.PostMan;
import mail.enumMailKind;
import member.enums.enumMemberAbnormalState;
import member.enums.enumMemberStandard;
import member.enums.enumMemberState;
import member.enums.enumMemberType;
import page.enums.enumPage;



public class ManageMember extends Member {
	
	private static Connection conn = ConnectMysql.getConnector();

	private ManageMember() {
	}

	public static EnumMap<enumMemberAbnormalState, Boolean> getMemberStates(Member member) throws Throwable{

		Statement _st = null;
		PreparedStatement _ps = null;
		ResultSet _rs = null;
		EnumMap<enumMemberAbnormalState, Boolean> _stateMap = new EnumMap<>(enumMemberAbnormalState.class);
		try{
			
			int _id = member.getId();
			
			
			_ps = conn.prepareStatement("select * from userState where u_id = ?");
			_ps.setInt(1, _id);			
			_rs = _ps.executeQuery();
			
			if(_rs.next()){
				
				if(_rs.getInt("isAbnormal")==1){
				
					for(enumMemberAbnormalState e : enumMemberAbnormalState.values()){												
						String _attributeName = e.getString();
						if(_rs.getInt(_attributeName)==1)
							_stateMap.put(e, true);
						else
							_stateMap.put(e, false);
					}
				}
				
			}
			else
				throw new SQLException();
		}
		 finally {
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
			if (_st != null)
				try {
					_st.close();
				} catch (SQLException ex) {
				}
		}
		
		return _stateMap;
		
	}



	/**
	 * @param member  가입할 유저의 정보의 객체 
	 * @param request jsp페이지로부터  넘어온 attribute값을 이용하기 위함 
	 * @return 가입이 무사히 성사됐는지 여부를 반환
	 * @throws Throwable 
	 */
	@SuppressWarnings("resource")
	public static boolean joinMember(Member member) throws Throwable {
		
		conn.setAutoCommit(false);
		ResultSet _rs = null;
		PreparedStatement _ps = null;
		int _key=-1;
		
		try {
			
			_ps = conn.prepareStatement("select u_id from user where email = ?");
			_ps.setString(1, member.getEmail());
			_rs = _ps.executeQuery();

			//이미 가입한 경우
			if( !_rs.next()){
				
				int id = _rs.getInt("u_id");
				
				_ps = conn.prepareStatement("select joinCertification from userState where u_id = ?");
						
				_ps.setInt(1, id);
				_rs = _ps.executeQuery();
						
				if(_rs.next()){
					if(_rs.getInt("joinCertification")==1){
						throw new MemberException( "가입후  인증하지 않은 상태입니다.메일 인증 후 로그인 하세요" ,enumMemberState.NOT_JOIN_CERTIFICATION, enumPage.ENTRY);
					}
				}else
					throw new SQLException("id"+id+"가 userState에 존재하지 않습니다.");
					
				throw new SQLException(member.getEmail() +"이나" + member.getNickname()+"이 중복됩니다." );
			}
		
			////////user table
			
			_ps = conn.prepareStatement(
					"insert into user ( email, nickname, password, registrationDate, registrationKind) values (?,?,?,?,?)",
					PreparedStatement.RETURN_GENERATED_KEYS);

			_ps.setString(1, member.getEmail());
			_ps.setString(2, member.getNickname());
			_ps.setTimestamp(4, member.getRegDate());
			_ps.setString(5, member.getIdType().getString());
			if(member.getIdType().getString().equals( enumMemberType.NOTHING.getString() )){

				String pw = BCrypt.hashpw( member.getPlanePassword(), BCrypt.gensalt(12));
				_ps.setString(3, pw);
			}
			else
				_ps.setString(3, "");
			
			_ps.executeUpdate();

			_rs = _ps.getGeneratedKeys();

			if (_rs.next()) {
			    _key = _rs.getInt(1);
			}	
			else{
				member.setJoin(false);
				throw new SQLException("-");
			}
			

			member.setJoin(true);
			_ps.close();
			_rs.close();
			
			//insert certification table and send mail 
				
			String _uuid =  UUID.randomUUID().toString();
			String hashedUUID =  BCrypt.hashpw(_uuid, BCrypt.gensalt(12));
			_ps = conn.prepareStatement("insert into joinCertification (u_id, certificationNumber ) values(?,?)" );
			_ps.setInt(1,_key);
			_ps.setString(2, _uuid);	
			_ps.executeUpdate();
			
			String _fullUrl = new StringBuilder(enumPage.ROOT.toString()).append(enumPage.MAIL.toString()).append("?email=").append(member.getEmail()).
					append("&number=").append(hashedUUID).append("&kind=").append(enumMailKind.JOIN.toString()).toString();
			if(PostMan.sendCeriticationJoin(member.getEmail(), _fullUrl ))
				conn.commit();
			else{
				conn.rollback();
			}
		}
		finally {
			if(conn!=null) 
				try{conn.rollback();}// Exception 발생시 rollback 한다.
					catch(SQLException ex1){
						System.out.println(ex1.getMessage());
						ex1.printStackTrace();
					}
			if (_ps != null)
				try {
					_ps.close();
				} catch (SQLException ex) {
					System.out.println(ex.getMessage());
					ex.printStackTrace();
				}
			if (_rs != null)
				try {
					_rs.close();
				} catch (SQLException ex) {
					System.out.println(ex.getMessage());
					ex.printStackTrace();
				}
		}
		
			
	
		return true;
	}

	/**
	 * @throws Exception 
	 * @param member  가입할 유저의 정보의 객체 
	 * @param request jsp페이지로부터  넘어온 attribute값을 이용하기 위함 
	 * @return 로그인 무사히 성사됐는지 여부를 반환
	 * @throws  
	 */
	public static boolean loginMember(Member member) throws Exception {
		
		PreparedStatement __ps =null;
		ResultSet __rs = null;
		Statement _st = conn.createStatement();
		PreparedStatement _ps = null;
		ResultSet _rs=null;
		boolean _res=false;

		try {				
			
			_ps = conn.prepareStatement("select * from user where u_id = ? ");
			_ps.setInt(1, member.getId());
			_rs = _ps.executeQuery();	
			_rs.next();
			String hashedpw =  _rs.getString("password");

			//String hashed = BCrypt.hashpw(member.getPassword(), BCrypt.gensalt(12));	
			//비밀번호 일치.
			if( BCrypt.checkpw( member.getPlanePassword(), hashedpw ) ){
				
				_res =  true;
				

				////// 마지막 로그인 날짜 갱신
				__ps = conn.prepareStatement("update userDetail set lastLoginDate = ?, failedLoginCount = ? where u_id = ?");
				
				
				Timestamp t = new Timestamp(System.currentTimeMillis());
				__ps.setTimestamp(1, t);
				__ps.setInt(2, 0);
				__ps.setInt(3, member.getId());
				__ps.executeUpdate();
				__ps.close();
								
				
				//잠금상태 해제 
				//sleep인경우?


				member.setEmail( _rs.getString("email"));
				member.setNickname(_rs.getString("nickname"));
				member.setRegDate(_rs.getTimestamp("registrationDate"));
				
				_rs.close();
				_ps.close();
				
				member.setLogin(true);
				
			}
			//불일치
			else{
				_res = false;
				
				__ps = conn.prepareStatement("select failedLoginCount from userDetail where u_id =?");
				__ps.setInt(1, member.getId());
				__rs = __ps.executeQuery();
				__rs.next();
				int _failedLoginCount = __rs.getInt("failedLoginCount");
				__ps.close();
								
				__ps = conn.prepareStatement("update userDetail set failedLoginCount = ? where u_id = ?");
				__ps.setInt(1, _failedLoginCount+1);
				__ps.setInt(2, member.getId());					
				__ps.close();
				if(_failedLoginCount >= Integer.valueOf(enumMemberStandard.POSSIBILLTY_FAILD_LOGIN_NUM.toString())){
					__ps = conn.prepareStatement("update userState set isAbnormal = 1 , failedLogin = 1 where u_id = ?");
					__ps.setInt(1, member.getId());
					__ps.executeUpdate();
					__ps.close();
				}
					

				member.setLogin(false);
				
			}
		
		}
		finally {
			if (_ps != null)
				try {
					_ps.close();
				} catch (SQLException ex) {
					System.out.println(ex.getMessage());
					ex.printStackTrace();
				}
			if (_rs != null)
				try {
					_rs.close();
				} catch (SQLException ex) {
					System.out.println(ex.getMessage());
					ex.printStackTrace();
				}
			if (_st != null)
				try {
					_st.close();
				} catch (SQLException ex) {
					System.out.println(ex.getMessage());
					ex.printStackTrace();
				}
		}

		return _res;
	}

	public static boolean loginManager(Member member) throws Throwable{
		
		
		PreparedStatement _ps = null;
		ResultSet _rs=null;
		
		
		boolean _res=false;
		try {	
							
			
			if(!member.getEmail().equals(enumSystem.ADMIN.toString()))
				throw new MemberException(enumMemberState.NOT_ADMIN, enumPage.LOGIN_MANAGER);
			
			_ps = conn.prepareStatement("select password from user where u_id=? ");
			_ps.setInt(1, member.getId());
			_rs = _ps.executeQuery();	
			
			_rs = _ps.executeQuery();
			_rs.next();
			String hashedpw =  _rs.getString("password");
			_ps.close();
			_rs.close();

				
			//비밀번호 일치.
			if( BCrypt.checkpw( member.getPlanePassword(), hashedpw ) ){
				
				_res =  true;
				
				////// 마지막 로그인 날짜 갱신
				_ps = conn.prepareStatement("update userDetail set lastLoginDate = ?, failedLoginCount = ? where u_id = ?");
				
				
				Timestamp t = new Timestamp(System.currentTimeMillis());
				_ps.setTimestamp(1, t);
				_ps.setInt(2, 0);
				_ps.setInt(3, member.getId());
				_ps.executeUpdate();
				_ps.close();
								
				
				//잠금상태 해제 
				//sleep인경우?
		
				member.setLogin(true);
			}
			//불일치
			else{
				_res = false;
				
				_ps = conn.prepareStatement("select failedLoginCount from userDetail where u_id =?");
				_ps.setInt(1, member.getId());
				_rs = _ps.executeQuery();
				_rs.next();
				int _failedLoginCount = _rs.getInt("failedLoginCount");
				_ps.close();
								
				_ps = conn.prepareStatement("update userDetail set failedLoginCount = ? where u_id = ?");
				_ps.setInt(1, _failedLoginCount+1);
				_ps.setInt(2, member.getId());					
				_ps.close();
				if(_failedLoginCount >= Integer.valueOf(enumMemberStandard.POSSIBILLTY_FAILD_LOGIN_NUM.toString())){
					_ps = conn.prepareStatement("update userState set isAbnormal = 1 , failedLogin = 1 where u_id = ?");
					_ps.setInt(1, member.getId());
					_ps.executeUpdate();
					_ps.close();
				}

				member.setLogin(false);
				
			}
		
		}
		finally {
			if (_ps != null)
				try {
					_ps.close();
				} catch (SQLException ex) {
					System.out.println(ex.getMessage());
					ex.printStackTrace();
				}
			if (_rs != null)
				try {
					_rs.close();
				} catch (SQLException ex) {
					System.out.println(ex.getMessage());
					ex.printStackTrace();
				}

		}
		
		
		return true;
	}
	
	/**
	 * 	로그아웃을 위한 처리를 합니다.
	 * @param member
	 * @throws Throwable 
	 */
	public static void logoutMember(Member member) throws Throwable {
			
		PreparedStatement _ps = conn.prepareStatement("update userState set lastLogoutDate = ? where u_id = ?");
		_ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()) ) ;
		_ps.setInt(2,  member.getId());
		_ps.executeUpdate();

		Member.removeMember(member);
	}
	
	public static boolean isMember(String email) throws SQLException{
		
		PreparedStatement _ps = null;
		ResultSet _rs = null;
		
		_ps = conn.prepareStatement("select u_id from user where email = ?");
		_ps.setString(1, email);
		_rs = _ps.executeQuery();
		
		if(_rs.next())			
			return true;
			
		else
			return false;
		
	}
	
	public static boolean isMember(Member member) throws SQLException{
	
		ResultSet _rs = null;
		PreparedStatement _ps = null;

		boolean _result = false;
	
		if(member.getNickname().equals("")==false){
			
			_ps = conn.prepareStatement("select nickname from user where nickname = ?");
			_ps.setString(1, member.getNickname());
			_rs = _ps.executeQuery();
			
			if(_rs.next())
				_result = true;
			else
				_result = false;
							
		}
		else if(member.getEmail().equals("")==false){
			_ps = conn.prepareStatement("select u_id from user where email = ?");
			_ps.setString(1, member.getEmail());
			_rs = _ps.executeQuery();
			
			if(_rs.next())			
				_result = true;
				
			else
				_result = false;
		}
		else if(member.getId()>Member.DEFAULT_VALUE){
			_ps = conn.prepareStatement("select u_id from user where u_id = ?");
			_ps.setString(1, member.getNickname());
			_rs = _ps.executeQuery();
			
			if(_rs.next())
				_result = true;
			else
				_result = false;
		}
			

		
		
		return _result;
	}
	
	/**
	 * 
	 * 
	 * @param member
	 * @return	3개월 이상 지났다면 true 아니면 false
	 * @throws SQLException 
	 */
	public static boolean isPassingDate(Member member) throws SQLException{
		
		PreparedStatement _ps = conn.prepareStatement("select sendedMailDate from losingPassword where u_id = ?");
		_ps.setInt(1, member.getId());
		ResultSet _rs = _ps.executeQuery();
		_rs.next();		
		
		Timestamp time = _rs.getTimestamp("sendedMailDate");
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
		
		int stdDate = Integer.valueOf( enumMemberStandard.PASSWD_CHANGE_STADNDATE_DATE.getString());
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
	public static boolean isSendmail(Member member) throws NumberFormatException, SQLException{
		
		//이미 전송하였는가?
		
		boolean isAlreadySend=false;
		PreparedStatement _ps =conn.prepareStatement("select isSendedMail, sendedMailDate from losingPassword where u_id = ?");
		_ps.setInt(1, member.getId());
		ResultSet _rs = _ps.executeQuery();
		_rs.next();
		
		isAlreadySend = _rs.getInt("isSendedMail")==1 ? true : false;
		Timestamp sendedMailDate = _rs.getTimestamp("isSendedMail");
		
		if(isAlreadySend){
			Timestamp time = sendedMailDate;
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
				cal2.add ( Calendar.HOUR, 1 ); // 다음날로 바뀜					
			}
			
			int stdDate = Integer.valueOf( enumMemberStandard.RESEND_STANDRATE_DATE.getString());
			//인증메일을 보낸지 24시간이 아직 경과 하지 않았는가?
			//경과하지않음.
			if(stdDate > count)
				return false;
		}					
		
		return true;

	}
	
	/**
	 * 	가입 후 인증메일로 받은 email과 인증번호를 비교하여 결과를 반환한다.
	 * @param email 가입당시 사용한 메
	 * @param hashedUUID  가입인증을 위해 발급된 인증번호(해시된 비번)
	 * @return
	 * @throws Exception 
	 * @throws Throwable 
	 */
	public static boolean certificateJoin(String sId, String email, String hashedUUID) throws Exception {
		
		PreparedStatement _ps = null;
		ResultSet _rs = null;
		
		if(!isMember(email))
			throw new MemberException(enumMemberState.NOT_JOIN, enumPage.ERROR404);
		
		try{
			
			conn.setAutoCommit(false);
			
			Member member = Member.getMember(sId);
			 
			int _id = member.getId();
			
			 _ps= conn.prepareStatement("select certificationNumber from joinCertification where u_id = ? ");
			_ps.setInt(1, _id);
			_rs = _ps.executeQuery();
			
			if(!_rs.next())
				throw new SQLException(email+","+_id+"joinCertification테이블에 id가 둘 이상 존재하거나 한개도 존재하지 않습니다.");
			 			
			String planeUUID = _rs.getString(1);
			
			if(BCrypt.checkpw( planeUUID, hashedUUID )){
				
				_ps = conn.prepareStatement("delete from joinCertification where u_id = ?");
				_ps.setInt(1, _id);
				_ps.executeUpdate();
				
				_ps = conn.prepareStatement("update userState set joinCertification = 0 where u_id = ?");
				_ps.setInt(1, _id);
				_ps.executeUpdate();

				
				////////userDetail table
				
				Timestamp date = new Timestamp(System.currentTimeMillis());
				_ps = conn.prepareStatement("update userDetail set lastModifiedPasswordDate =? , lastLogoutDate = ? where u_id = ?");				
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
	
	public static boolean changePassword(Member member, String newPassword) throws Throwable{
				
		PreparedStatement _sp = conn.prepareStatement("update user set password = ? where u_id = ?");
		
		String _hashPassword = BCrypt.hashpw( newPassword, BCrypt.gensalt());
		_sp.setString(1, _hashPassword);
		_sp.setInt(2, member.getId());
				
		return true;
		
	}

	private static void setAbnormalState(int uId) throws SQLException{
		
		
		PreparedStatement _ps = null;
		ResultSet _rs = null;
		
		try{
			_ps = conn.prepareStatement("select * from usetState where u_id = ?");
			_ps.setInt(1, uId);
			_rs = _ps.executeQuery();
			if(!_rs.next())
					throw new SQLException("userState 테이블에 "+uId+"가 없습니다.");
			
			boolean isAbnormal=false;
			for(enumMemberAbnormalState e: enumMemberAbnormalState.values()){
				if(e.getString().equals("isAbnormal") || e.getString().equals("u_id"))
					continue;
				
				if(_rs.getInt(e.getString())==1)
					isAbnormal=true;
			}
			_ps.close();
			
			_ps = conn.prepareStatement("update userState set isAbnormal = ? where u_id = ?");
			if(isAbnormal)
				_ps.setInt(1, 1);
			else
				_ps.setInt(1, 0);
			
			_ps.setInt(2, uId);
			_ps.executeQuery();		
			
			
		}finally{
			try {
				_ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				_rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	public static void withdrawMember(int uId, String nickname, String email, String reason) throws SQLException{
		
		PreparedStatement _ps= null;
		try{
			
			conn.setAutoCommit(false);
		
		
			_ps= conn.prepareStatement("delete from user where u_id = ?");
			_ps.setInt(1, uId);
			_ps.executeQuery();			
			
			PostMan.sendWithdraw(nickname, email, reason);
			
			conn.commit();
		}finally{
			
			_ps.close();
		}
		
	}
	
	public static ArrayList<Member> getAllMember(){
		
		ArrayList<Member> _members = new ArrayList<Member>();
		PreparedStatement _ps=null, __ps=null;
		ResultSet _rs=null, __rs=null;
		
		
		try{
			
			 _ps = conn.prepareStatement("select * from user");
			 _rs = _ps.executeQuery();
			
			while(_rs.next()){
											
				Member m = new Member();
				m.setEmail( _rs.getString("email"));
				m.setId( _rs.getInt("u_id"));
				m.setNickname(_rs.getString("nickname"));
				m.setRegDate(_rs.getTimestamp("registrationDate"));
								
				__ps = conn.prepareStatement("select * from userState where u_id = ?");
				__ps.setInt(1, m.getId());
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
		
				m.setAbnormalState(state);
				_members.add(m);
				
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
	
}



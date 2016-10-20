
package member;


import java.sql.*;
import java.util.*;
import java.util.Date;
import org.mindrot.jbcrypt.BCrypt;

import property.ConnectMysql;
import property.enums.enumSystem;
import mail.PostMan;
import mail.enumMailType;
import member.enums.enumMemberAbnormalState;
import member.enums.enumMemberStandard;
import member.enums.enumMemberState;
import member.enums.enumMemberType;
import page.PageException;
import page.enums.enumPage;
import page.enums.enumPageError;



public final class MemberManager {
	
	private static Map<String, Member> MemberMap = new HashMap<String, Member>();
	private static Connection conn = ConnectMysql.getConnector();

	private MemberManager() {
	}

	public static EnumMap<enumMemberAbnormalState, Boolean> getMemberStates(Member member) throws Throwable{

		Statement _st = null;
		PreparedStatement _ps = null;
		ResultSet _rs = null;
		EnumMap<enumMemberAbnormalState, Boolean> _stateMap = new EnumMap<>(enumMemberAbnormalState.class);
		try{
			
			int _id = member.getId();
			
			
			_ps = conn.prepareStatement("select * from userState where userId = ?");
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



	public static boolean isMember(String email) throws SQLException{
		
		PreparedStatement _ps = null;
		ResultSet _rs = null;
		
		_ps = conn.prepareStatement("select userId from user where email = ?");
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
			_ps = conn.prepareStatement("select userId from user where email = ?");
			_ps.setString(1, member.getEmail());
			_rs = _ps.executeQuery();
			
			if(_rs.next())			
				_result = true;
				
			else
				_result = false;
		}
		else if(member.getId()>Member.DEFAULT_VALUE){
			_ps = conn.prepareStatement("select userId from user where userId = ?");
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
		
		PreparedStatement _ps = conn.prepareStatement("select sendedMailDate from losingPassword where userId = ?");
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
	public static boolean isSendedmail(Member member ) throws NumberFormatException, SQLException{
		
		//이미 전송하였는가?
		
		boolean isAlreadySend=false;
		PreparedStatement _ps =conn.prepareStatement("select isSendedMail, sendedMailDate from losingPassword where userId = ?");
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
	public static boolean resolveCertificateJoin(String sId, String email, String hashedUUID) throws Exception {
		
		PreparedStatement _ps = null;
		ResultSet _rs = null;
		
		if(!isMember(email))
			throw new MemberException(enumMemberState.NOT_JOIN, enumPage.ERROR404);
		
		try{
			
			conn.setAutoCommit(false);
			
			Member member = getMember(sId);
			 
			int _id = member.getId();
			
			 _ps= conn.prepareStatement("select certificationNumber from joinCertification where userId = ?, certificationType = ? ");
			_ps.setInt(1, _id);
			_ps.setInt(2, Integer.parseInt(enumMailType.JOIN.toString()) );
			_rs = _ps.executeQuery();
			
			if(!_rs.next())
				throw new SQLException(email+","+_id+"joinCertification테이블에 id가 둘 이상 존재하거나 한개도 존재하지 않습니다.");
			 			
			String planeUUID = _rs.getString(1);
			
			if(BCrypt.checkpw( planeUUID, hashedUUID )){
				
				_ps = conn.prepareStatement("delete from joinCertification where userId = ?  certificationType = ? ");
				_ps.setInt(1, _id);
				_ps.setInt(2, Integer.parseInt(enumMailType.JOIN.toString()));
				_ps.executeUpdate();
				
				_ps = conn.prepareStatement("update userState set joinCertification = 0 where userId = ?");
				_ps.setInt(1, _id);
				_ps.executeUpdate();

				
				////////userDetail table
				
				Timestamp date = new Timestamp(System.currentTimeMillis());
				_ps = conn.prepareStatement("update userDetail set lastModifiedPasswordDate =? , lastLogoutDate = ? where userId = ?");				
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
	
	public static boolean requestCertificateJoin(Member member) throws SQLException{
		
		conn.setAutoCommit(false);
		PreparedStatement _ps;
		
		String _uuid =  UUID.randomUUID().toString();
		String hashedUUID =  BCrypt.hashpw(_uuid, BCrypt.gensalt(12));
		_ps = conn.prepareStatement("insert into mail (userId, certificationNumber, certificationType) values(?,?,?)" );
		_ps.setInt(1,member.getId() );
		_ps.setString(2, _uuid);
		_ps.setInt(3, Integer.parseInt(enumMailType.JOIN.toString()) );	
		_ps.executeUpdate();
		
		String _fullUrl = new StringBuilder(enumPage.ROOT.toString()).append(enumPage.MAIL.toString()).append("?email=").append(member.getEmail()).
				append("&number=").append(hashedUUID).append("&kind=").append(enumMailType.JOIN.toString()).toString();
		
		_ps.close();
		
		if(PostMan.sendCeriticationJoin(member.getEmail(), _fullUrl )){
			conn.commit();
			return true;
		}
		else{
			conn.rollback();
			
			conn.setAutoCommit(false);
			
			_ps = conn.prepareStatement("delete from user where userId = ? ");
			_ps.setInt(1, member.getId());
			_ps.executeUpdate();
			removeMember(member);
			
			conn.setAutoCommit(true);
			
			return false;
		}
		
		
	}
	
	public static boolean changePassword(Member member, String newPassword) throws Throwable{
				
		PreparedStatement _sp = conn.prepareStatement("update user set password = ? where userId = ?");
		
		String _hashPassword = BCrypt.hashpw( newPassword, BCrypt.gensalt());
		_sp.setString(1, _hashPassword);
		_sp.setInt(2, member.getId());
				
		return true;
		
	}

	public static void withdrawMember(int uId, String nickname, String email, String reason) throws SQLException{
		
		PreparedStatement _ps= null;
		try{
			
			conn.setAutoCommit(false);
		
		
			_ps= conn.prepareStatement("delete from user where userId = ?");
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
											
			
							
				__ps = conn.prepareStatement("select * from userState where userId = ?");
				__ps.setInt(1, _rs.getInt("userId"));
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
		
				Member member = (new Member.Builder(_rs.getInt("userId"), _rs.getString("email"), "")).idType(enumMemberType.valueOf(_rs.getString("idType"))  )
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
	
	

	/**
	 * 	로그인 시도중 가입한 사람이면  member객체의 정보를 모두 DB로 부터 읽어온다. 
	 * @param sId		sessionId
	 * @param nickname	페이지로부터 넘어온 nickname string
	 * @throws SQLException 
	 * @throws MemberException 
	 * @throws Throwable member, sql exception을 던진다
	 */
	public static Member setMemberFromDBaaaa(Member member) throws SQLException, MemberException{
		
		PreparedStatement _ps = null; 
		
		if(member.getEmail()!=null && !member.getEmail().equals("")){
			_ps = conn.prepareStatement("select * from user where email = ?");
			_ps.setString(1, member.getEmail());
		}
		else if(member.getNickname()!=null && !member.getNickname().equals("")){
			_ps = conn.prepareStatement("select * from user where nickname = ?");
			_ps.setString(1, member.getNickname());
		}
		
		ResultSet _rs = _ps.executeQuery();

		if(_rs.next()){
			
	/*		member.setNickname(_rs.getString("nickname"));
			member.setId(_rs.getInt("userId"));
			member.setEmail(_rs.getString("email"));
			member.setRegDate(_rs.getTimestamp("registrationDate"));
			member.setJoin(true);
			
			
			boolean _isExist = false;
			for(enumMemberType e : enumMemberType.values()){
				if(e.getString().equals(_rs.getString("registrationKind"))){
					_isExist = true;
					member.setIdType(e);
				}
			}
			if(!_isExist)
				throw new MemberException(enumMemberState.NOT_EXIST_IN_DB, enumPage.ENTRY);
			
			_ps = conn.prepareStatement("select d_id from developer where userId = ?");
			_ps.setInt(1, member.getId());
			_rs = _ps.executeQuery();*/
			
		}
		
		return member;
	}
	
	public static boolean isContainsMemberOfEmail(String email){
		
		for(String key : MemberMap.keySet())
			if(MemberMap.get(key).getEmail().equals(email))
				return true;
		
		return false;
	
	}
	
	public static boolean isContainsMember(String sId){
		
		return MemberMap.containsKey(sId);
						
	}
	
	public static boolean isContainsMember(int uId){
		
		for(String key : MemberMap.keySet())
			if(MemberMap.get(key).getId() == uId)
				return true;
		
		return false;
	
	}
	
	
	/**
	 * 	로그인한 유저를 대상으로 HashMap으로 객체를 보유하고 없으면 새로 생성한다. 
	 * @param sId	브라우져 sessionId를 통해 유저의 객체를 찾는다. 
	 * @return	sId key에 맞는 객체 value를 반환. 
	 * @throws MemberException 
	 * @throws SQLException 
	 * @throws Throwable	sId가 null이거나 sId를 통해 객체를 찾지 못핳는 경우 예외 발생. 
	 */
	public static Member getMember(String sId) throws SQLException, MemberException{
		
		if(sId==null)
			throw new NullPointerException();
		
		if(MemberMap.containsKey(sId))
			return MemberMap.get(sId);
		else
			throw new MemberException("비 정상적인 접근입니다.",enumMemberState.NOT_EXIST_MEMBER_FROM_MAP, enumPage.ERROR404);
		
	}
	
	public static Member getMember(int uId) throws SQLException, MemberException{
		
		if(uId<=0)
			throw new IllegalArgumentException("userId는 0보다 커야 합니다.");
				
		for(String key : MemberMap.keySet()){
			if(MemberMap.get(key).getId() == uId)
				return MemberMap.get(key);
		}
		
		throw new MemberException("비 정상적인 접근입니다.",enumMemberState.NOT_EXIST_MEMBER_FROM_MAP, enumPage.ERROR404);
		
	}
	
	public static Member getMemberFromDB(String email, String sId) throws SQLException, MemberException{
		
		if(email==null)
			throw new NullPointerException();
		if(email.equals("") || !email.contains("@"))
			throw new IllegalArgumentException(email +" ,파라메터에 이상이 있습니다.");

		
		PreparedStatement _ps = conn.prepareStatement("select * from user where email = ? ");
		_ps.setString(1, email);
		ResultSet _rs = _ps.executeQuery();	
		_rs.next();
		String hashedpw =  _rs.getString("password");
		
		return (new Member.Builder(_rs.getInt("userId"), email, sId)).idType(enumMemberType.valueOf(_rs.getString("registrationKind"))  )
				.nickname(_rs.getString("nickname")).build();
		
		
	}
	
	/**
	 * 	Member의 Builder를 통해서만 생성될 
	 * @param member
	 */
	protected static void addMember(Member member){
		
		if(member==null)
			throw new NullPointerException();
		
		if(member.getSessionId()==null || member.getSessionId().equals(""))
			throw new IllegalArgumentException("sessionId이 존재하지 않습니다.");
		
		if(MemberMap.containsKey(member))
			throw new IllegalArgumentException("맴버 객체가 이미 맵에 존합니다.");
				
		MemberMap.put(member.getSessionId(), member);
	}
		
	public static void removeMember(Member m){
		
		if(m==null)
			throw new NullPointerException();
		
		if(m.getSessionId()==null || m.getSessionId().equals(""))
			throw new IllegalArgumentException("sessionId이 존재하지 않습니다.");
		
		if(!MemberMap.containsValue(m))
			throw new IllegalArgumentException("맴버 객체가 존재하지 않습니다..");
		
		MemberMap.remove(m.getSessionId());
		m = null;
	}
	
	
	
}



package entity.member;

import java.awt.Point;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.mindrot.jbcrypt.BCrypt;

import entity.Entity;
import entity.member.enums.enumMemberAbnormalState;
import entity.member.enums.enumMemberStandard;
import entity.member.enums.enumMemberState;
import entity.member.enums.enumMemberType;
import mail.enumMailType;
import page.PageException;
import page.enums.enumPage;
import page.enums.enumPageError;
import property.ConnectMysql;
import property.enums.enumSystem;

import java.util.Set;
import java.util.EnumMap;
/**
 * member에 대한 객체 정보는 sessionId,email을 이용해  getMember를 통해서만 얻을 수 있으며
 * member객체를 생성하기 위한 생성자는 private이며 Builder를 통해서만 생성이 가능하다.
 *    또 생성후에 이를 관리 하기 위해 addMember 메서드를 이용해 map에 추가해야한다.
 *    제거할때는 removeMember 메서드를 통해서만 제거가 가능하다.
 * 	
 * @author cmk
 *
 */
public final class Member implements Entity {


	
	private static Connection conn = ConnectMysql.getConnector();
	
	/**
	 * Member객체를 생성하기 위한 Builder. build를 통해 최종적으로 member객체 생성 완료.
	 * @author cmk
	 *
	 */
	public static class Builder{
		
		private final int id;
		private String nickname;
		private String planePassword;
		private enumMemberType idType;
		private final String email;
		private EnumMap<enumMemberAbnormalState, Boolean> abnormalState;
		
		public Builder(int id, String email){
			
			this.id = id;
			this.email = email;
						
			nickname="";
			planePassword = "";
			idType = enumMemberType.NOTHING;
			abnormalState = new EnumMap<enumMemberAbnormalState, Boolean>(enumMemberAbnormalState.class);
		}
		
		public Builder(String email, String sId){
			this.id = -1;
			this.email = email;
		
			nickname="";
			planePassword = "";
			idType = enumMemberType.NOTHING;
			abnormalState = new EnumMap<enumMemberAbnormalState, Boolean>(enumMemberAbnormalState.class);
		}
		
		public Builder nickname(String nick){
			nickname = nick; return this;
		}
		
		public Builder planePassword(String pw){
			planePassword = pw; return this;
		}
	
		public Builder idType(enumMemberType idType){
			this.idType = idType; return this;
		}
		
		public Builder abnormalState(EnumMap<enumMemberAbnormalState, Boolean> map){
			abnormalState = map; return this;
		}
		
		public Member build(){
			return new Member(this);
		}
		
		
	}
	

	
	private EnumMap<enumMemberAbnormalState, Boolean> abnormalState;
	
	public static final int DEFAULT_VALUE = -1;
	private int id;
	private String nickname;
	private String planePassword;
	private enumMemberType idType;
	private String email;
	private Timestamp regDate;
	private boolean isLogin;
	private boolean isLogout;
	private boolean isJoin;
	
	
	private Member(){
		
	}
	
	private Member(Builder bld){
		id = bld.id;

		nickname=bld.nickname;
		planePassword=bld.planePassword;
		abnormalState = new EnumMap<>(enumMemberAbnormalState.class);
		idType=bld.idType;
		email=bld.email;
		regDate =  new Timestamp(System.currentTimeMillis());
		
	
		
	}
	
	
	@Override
	public int hashCode(){
		
		return 17;
	}
	
	@Override
	public String toString(){
		
		return new String("해당 객체는 사이트에서... ");
		
	}
	
	@Override
	public boolean equals(Object mdb){
		
		if( mdb == null)
			throw new NullPointerException();
		else if( !(mdb instanceof Member))
			throw new IllegalArgumentException("비교대상으로 적합하지 않은 객체가 비교시도 되었습니다.");
		
		Member m = (Member )mdb;
		
		return ( id==m.getId() && planePassword.equals(m.getPlanePassword()) ) ? true : false;
	}

	

	
	////getter setter////
	
	public int getId() {
		return id;
	}
	
	public String getNickname() {
		return nickname;
	}
	
	public String getPlanePassword() {
		return planePassword;
	}
	
	public String getEmail() {
		return email;
	}

	public boolean isLogin() {
	
		return isLogin;
	}
	
	public boolean isLogout() {
		return isLogout;
	}

	public boolean isJoin() {	
			
		return isJoin;
	}


	public enumMemberType getUserType() {
		return idType;
	}
	
	public Timestamp getRegDate() {
		return regDate;
	}

	public void setRegDate(Timestamp regDate) {
		this.regDate = regDate;
	}

	public EnumMap<enumMemberAbnormalState, Boolean> getAbnormalState() {
		return abnormalState;
	}

	/////method/////

	/**
	 * @param member  가입할 유저의 정보의 객체 
	 * @param request jsp페이지로부터  넘어온 attribute값을 이용하기 위함 
	 * @return 가입이 무사히 성사됐는지 여부를 반환
	 * @throws Throwable 
	 */
	@SuppressWarnings("resource")
	public boolean doJoin(String sId) throws Throwable {
		
		conn.setAutoCommit(false);
		ResultSet rs = null;
		PreparedStatement ps = null;
		int _idKey=-1;
		
		try {
			
			////////user table
			
			ps = conn.prepareStatement(
					"insert into user ( email, nickname, password, registrationDate, registrationKind) values (?,?,?,?,?)",
					PreparedStatement.RETURN_GENERATED_KEYS);

			ps.setString(1,email);
			ps.setString(2, nickname);
			ps.setTimestamp(4, new Timestamp( System.currentTimeMillis()) );
			ps.setString(5, idType.toString());
			if( idType.equals(enumMemberType.NOTHING)){

				String _pw = BCrypt.hashpw( planePassword, BCrypt.gensalt(12));
				ps.setString(3, _pw);
			}
			else
				ps.setString(3, "");
			
			ps.executeUpdate();

			rs = ps.getGeneratedKeys();

			if (rs.next()) {
			    _idKey = rs.getInt(1);
			}	
			else{
				
				throw new SQLException("-");
			}
			
			id = _idKey;
			
			MemberController.getInstance().addMember(this, sId);
			
			ps.close();
			rs.close();
	
			conn.commit();
		}
		catch(SQLException e){
			e.printStackTrace();
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
	public boolean doLogin(String sId) throws Exception {
		
		PreparedStatement _ps =null;
		ResultSet _rs = null;
		PreparedStatement ps = null;
		ResultSet rs=null;
		boolean res=false;

		try {				
			
			conn.setAutoCommit(false);
			
			ps = conn.prepareStatement("select * from user where email = ? ");
			ps.setString(1, email);
			rs = ps.executeQuery();	
			rs.next();
			String hashedpw =  rs.getString("password");
			int _idKey = rs.getInt("userId");
			id = _idKey;
			
			switch(idType){
				case NOTHING:
					if( BCrypt.checkpw( planePassword, hashedpw ) ){
						
						res =  true;

						////// 마지막 로그인 날짜 갱신
						_ps = conn.prepareStatement("update userDetail set lastLoginDate = ?, failedLoginCount = ? where userId = ?");
						
						
						Timestamp t = new Timestamp(System.currentTimeMillis());
						_ps.setTimestamp(1, t);
						_ps.setInt(2, 0);
						_ps.setInt(3, _idKey);
						_ps.executeUpdate();
						_ps.close();
										
						
						//잠금상태 해제 
						//sleep인경우?
						
						rs.close();
						ps.close();
						
						isLogin = true;
						MemberController.getInstance().addMember(this, sId);
						
					}
					//불일치
					else{
						res = false;
						
						_ps = conn.prepareStatement("select failedLoginCount from userDetail where userId =?");
						_ps.setInt(1,_idKey);
						_rs = _ps.executeQuery();
						_rs.next();
						int _failedLoginCount = _rs.getInt("failedLoginCount");
						_ps.close();
										
						_ps = conn.prepareStatement("update userDetail set failedLoginCount = ? where userId = ?");
						_ps.setInt(1, _failedLoginCount+1);
						_ps.setInt(2, _idKey);					
						_ps.close();
						if(_failedLoginCount >= Integer.valueOf(enumMemberStandard.POSSIBILLTY_FAILD_LOGIN_NUM.toString())){
							_ps = conn.prepareStatement("update userState set isAbnormal = 1 , failedLogin = 1 where userId = ?");
							_ps.setInt(1, _idKey);
							_ps.executeUpdate();
							_ps.close();
						}
							
		
						isLogin = false;
						
					}
				
					break;
				
				case GOOGLE:
				case NAVER:
					
					////// 마지막 로그인 날짜 갱신
					_ps = conn.prepareStatement("update userDetail set lastLoginDate = ?, failedLoginCount = ? where userId = ?");
					
					
					Timestamp t = new Timestamp(System.currentTimeMillis());
					_ps.setTimestamp(1, t);
					_ps.setInt(2, 0);
					_ps.setInt(3,_idKey);
					_ps.executeUpdate();
					_ps.close();
									
					
					//잠금상태 해제 
					//sleep인경우?
					
					rs.close();
					ps.close();
					
					isLogin = true;
					MemberController.getInstance().addMember(this, sId);
					
					
					break;
				
				default:
					throw new PageException(enumPageError.UNKNOWN_PARA_VALUE,enumPage.ERROR404);
					
			
					
			}
			
			
			conn.setAutoCommit(true);
			
		}catch(SQLException e){
			e.printStackTrace();
		}
	
		return res;
	}

	public boolean doLoginManager(String sId) throws Throwable{
		
		
		PreparedStatement _ps = null;
		ResultSet _rs=null;
		
		
		boolean _res=false;
		try {	
							
			
			if(!email.equals(enumSystem.ADMIN.toString()))
				throw new MemberException(enumMemberState.NOT_ADMIN, enumPage.LOGIN_MANAGER);
			
			_ps = conn.prepareStatement("select password from user where email=? ");
			_ps.setString(1,email);
			_rs = _ps.executeQuery();	
			
			_rs = _ps.executeQuery();
			_rs.next();
			String hashedpw =  _rs.getString("password");
			_ps.close();
			_rs.close();

				
			//비밀번호 일치.
			if( BCrypt.checkpw( planePassword, hashedpw ) ){
				
				_res =  true;
				
				////// 마지막 로그인 날짜 갱신
				_ps = conn.prepareStatement("update userDetail set lastLoginDate = ?, failedLoginCount = ? where userId = ?");
				
				
				Timestamp t = new Timestamp(System.currentTimeMillis());
				_ps.setTimestamp(1, t);
				_ps.setInt(2, 0);
				_ps.setInt(3, id);
				_ps.executeUpdate();
				_ps.close();
								
				
				//잠금상태 해제 
				//sleep인경우?
		
				isLogin = true;
				MemberController.getInstance().addMember(this, sId);
				
			}
			//불일치
			else{
				_res = false;
				
				_ps = conn.prepareStatement("select failedLoginCount from userDetail where userId =?");
				_ps.setInt(1, id);
				_rs = _ps.executeQuery();
				_rs.next();
				int _failedLoginCount = _rs.getInt("failedLoginCount");
				_ps.close();
								
				_ps = conn.prepareStatement("update userDetail set failedLoginCount = ? where userId = ?");
				_ps.setInt(1, _failedLoginCount+1);
				_ps.setInt(2, id);					
				_ps.close();
				if(_failedLoginCount >= Integer.valueOf(enumMemberStandard.POSSIBILLTY_FAILD_LOGIN_NUM.toString())){
					_ps = conn.prepareStatement("update userState set isAbnormal = 1 , failedLogin = 1 where userId = ?");
					_ps.setInt(1, id);
					_ps.executeUpdate();
					_ps.close();
				}

				isLogin = false;
				
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		
		
		return true;
	}
	
	/**
	 * 	로그아웃을 위한 처리를 합니다.
	 * @param member
	 * @throws Throwable 
	 */
	public void doLogout(String sId) throws Throwable {
			
		PreparedStatement _ps = conn.prepareStatement("update userState set lastLogoutDate = ? where userId = ?");
		_ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()) ) ;
		_ps.setInt(2,  id);
		_ps.executeUpdate();

		MemberController.getInstance().removeObject(this.getId());
	}
	
	public void doWithdraw() {
		
		
		
	}

}

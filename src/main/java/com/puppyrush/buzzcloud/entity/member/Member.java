package com.puppyrush.buzzcloud.entity.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.puppyrush.buzzcloud.dbAccess.DBManager;
import com.puppyrush.buzzcloud.dbAccess.DBManager.ColumnHelper;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.band.enums.enumBand;
import com.puppyrush.buzzcloud.entity.interfaces.Entity;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberAbnormalState;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberStandard;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberState;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberType;
import com.puppyrush.buzzcloud.mail.PostMan;
import com.puppyrush.buzzcloud.mail.PostManImple;
import com.puppyrush.buzzcloud.mail.enumMail;
import com.puppyrush.buzzcloud.page.PageException;
import com.puppyrush.buzzcloud.page.enums.enumPage;
import com.puppyrush.buzzcloud.page.enums.enumPageError;
import com.puppyrush.buzzcloud.property.ConnectMysql;
import com.puppyrush.buzzcloud.property.enumSystem;
/**
 * member에 대한 객체 정보는 sessionId,email을 이용해  getMember를 통해서만 얻을 수 있으며
 * member객체를 생성하기 위한 생성자는 private이며 Builder를 통해서만 생성이 가능하다.
 *    또 생성후에 이를 관리 하기 위해 addMember 메서드를 이용해 map에 추가해야한다.
 *    제거할때는 removeMember 메서드를 통해서만 제거가 가능하다.
 * 	
 * @author cmk
 *
 */

@Repository("member")
public final class Member implements Entity {

	@Autowired
	private DBManager dbMng;	
	
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
		private enumMemberType registrationKind;
		private final String email;
		private EnumMap<enumMemberAbnormalState, Boolean> abnormalState;
				
		public Builder(int id, String email){
			
			this.id = id;
			this.email = email;
						
			nickname="";
			planePassword = "";
			registrationKind = enumMemberType.NOTHING;
			abnormalState = new EnumMap<enumMemberAbnormalState, Boolean>(enumMemberAbnormalState.class);
		}
		
		public Builder(String email){
			this.id = -1;
			this.email = email;
		
			nickname="";
			planePassword = "";
			registrationKind = enumMemberType.NOTHING;
			abnormalState = new EnumMap<enumMemberAbnormalState, Boolean>(enumMemberAbnormalState.class);
		}
		
		public Builder nickname(String nick){
			nickname = nick; return this;
		}
		
		public Builder planePassword(String pw){
			planePassword = pw; return this;
		}
	
		public Builder registrationKind(enumMemberType registrationKind){
			this.registrationKind = registrationKind; return this;
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
	private enumMemberType registrationKind;
	private String email;
	private Timestamp regDate;
	private boolean isLogin;
	private boolean isLogout;
	private boolean isJoin;
	private String sessionId;
	
	
	private Member(){
		
	}
	
	private Member(Builder bld){
		id = bld.id;

		nickname=bld.nickname;
		planePassword=bld.planePassword;
		abnormalState = bld.abnormalState;
		registrationKind=bld.registrationKind;
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
	
	public void setNickname(String name){
		if(name==null)
			throw new NullPointerException();
		
		if(name.equals("") || name.length() < enumMemberStandard.NAME_LENGTH.toInt() )
			throw new IllegalArgumentException("이름의 길이가 적절하지 않습니다. : " + name);
		
		nickname = name;
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
		return registrationKind;
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

	public void setPlanePassword(String planePassword) {
		this.planePassword = planePassword;
	}

	public enumMemberType getRegistrationKind() {
		return registrationKind;
	}

	public void setRegistrationKind(enumMemberType registrationKind) {
		this.registrationKind = registrationKind;
	}

	
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	/////method/////

	/**
	 * @param member  가입할 유저의 정보의 객체 
	 * @param request jsp페이지로부터  넘어온 attribute값을 이용하기 위함 
	 * @return 가입이 무사히 성사됐는지 여부를 반환
	 * @throws SQLException 
	 * @throws Throwable 
	 */
	@SuppressWarnings("resource")
	public boolean doJoin() throws SQLException {
		
		conn.setAutoCommit(false);
		ResultSet rs = null;
		PreparedStatement ps = null;
		int _idKey=-1;
		
		try {
			
			////////user table
			
			ps = conn.prepareStatement(
					"insert into member ( email, nickname, password, registrationDate, registrationKind) values (?,?,?,?,?)",
					PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setString(1,email);
			ps.setString(2, nickname);
			ps.setTimestamp(4, new Timestamp( System.currentTimeMillis()) );
			ps.setString(5, registrationKind.toString());
			
			if( registrationKind.equals(enumMemberType.NOTHING)){

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
			isJoin = true;
			isLogout = true;
			isLogin = false;
			
			ps.close();
			rs.close();

			ps = conn.prepareStatement("insert into memberDetail(permissionCapactiy) values(?)");
			ps.setInt(1, enumBand.DEFAULT_CAPACITY.toInteger());			
			conn.commit();
			

		}catch(SQLException e){
			e.printStackTrace();
		}

		return true;
	}


	/**
	 * @throws SQLException 
	 * @throws PageException 
	 * @throws Exception 
	 * @param member  가입할 유저의 정보의 객체 
	 * @param request jsp페이지로부터  넘어온 attribute값을 이용하기 위함 
	 * @return 로그인 무사히 성사됐는지 여부를 반환
	 * @throws  
	 */
	public boolean doLogin() throws PageException, SQLException {
		
		boolean res=false;			
		
		Map<String, Object> where = new HashMap<String, Object>();
		where.put("email", email);

		ColumnHelper ch = dbMng.getColumnsOfAll("member", where);
						
		String hashedpw =  ch.getString(0, "password");
		int _idKey = ch.getInteger(0,"memberId");
		id = _idKey;
		
		switch(registrationKind){
			case NOTHING:
				if( BCrypt.checkpw( planePassword, hashedpw ) ){
					
					res =  true;

					////// 마지막 로그인 날짜 갱신
					Map<String, Object> _set = new HashMap<String, Object>();
					_set.put("lastLoginDate", new Timestamp(System.currentTimeMillis()));
					_set.put("failedLoginCount", 0);
					
					Map<String, Object> _where = new HashMap<String, Object>();
					_where.put("memberId", _idKey);
					
					dbMng.updateColumn("memberDetail", _set, _where);
					
					//잠금상태 해제 
					//sleep인경우?
				
					isJoin = true;
					isLogin = true;
					isLogout = false;
		
				}
				//불일치
				else{
					res = false;
					
					List<String> _sel = new ArrayList<String>();
					_sel.add("failedLoginCount");
					
					Map<String, Object> _where = new HashMap<String, Object>();
					_where.put("memberId", _idKey);
					
					ColumnHelper _ch = dbMng.getColumnsOfPart("memberDetail", _sel, _where);
					
					
					int _failedLoginCount = ch.getInteger(0, "failedLoginCount");
					
					Map<String, Object> _set = new HashMap<String, Object>();
					_set.put("failedLoginCount", ++_failedLoginCount);
					
					dbMng.updateColumn("memberDetail", _set, _where);
											
					if(_failedLoginCount >= Integer.valueOf(enumMemberStandard.POSSIBILLTY_FAILD_LOGIN_NUM.toString())){
						
						Map<String, Object> set = new HashMap<String, Object>();
						set.put("isAbnormal", 1);
						set.put("failedLogin", 1);
						
						dbMng.updateColumn("memberState", set, _where);
					}
						
					isJoin = true;
					isLogin = false;
					isLogout = true;
				}
			
				break;
			
			case GOOGLE:
			case NAVER:
				
				Map<String, Object> set = new HashMap<String, Object>();
				set.put("lastLoginDate", new Timestamp(System.currentTimeMillis()));
				set.put("failedLoginCount", 0);
				
				Map<String, Object> _where = new HashMap<String, Object>();
				where.put("memberId", _idKey);
				
				dbMng.updateColumn("memberDetail", set, _where);

				//잠금상태 해제 
				//sleep인경우?
				
				isJoin = true;
				isLogin = true;
				isLogout = false;
				
				break;
			
			default:
				throw (new PageException.Builder(enumPage.LOGIN))
				.errorString("로그인 중 시스템 에러가 발생했습니다. 관리자에게 문의하세요.")
				.errorCode(enumPageError.UNKNOWN_PARA_VALUE).build(); 


		}

		return res;
	}

	public boolean doLoginManager() throws Throwable{
		
		
		PreparedStatement ps = null;
		ResultSet rs=null;

		try {	

			if(!email.equals(enumSystem.ADMIN.toString()))
				throw (new EntityException.Builder(enumPage.LOGIN_MANAGER))
				.errorCode(enumMemberState.NOT_ADMIN).build();
				
			
			ps = conn.prepareStatement("select password from member where email=? ");
			ps.setString(1,email);
			rs = ps.executeQuery();	
			
			rs = ps.executeQuery();
			rs.next();
			String hashedpw =  rs.getString("password");
			ps.close();
			rs.close();


			if( BCrypt.checkpw( planePassword, hashedpw ) ){
				

				////// 마지막 로그인 날짜 갱신
				ps = conn.prepareStatement("update memberDetail set lastLoginDate = ?, failedLoginCount = ? where memberId = ?");
				
				
				Timestamp t = new Timestamp(System.currentTimeMillis());
				ps.setTimestamp(1, t);
				ps.setInt(2, 0);
				ps.setInt(3, id);
				ps.executeUpdate();
				ps.close();
								
				
				//잠금상태 해제 
				//sleep인경우?
		
				isLogin = true;
				
			}
			else{

				ps = conn.prepareStatement("select failedLoginCount from memberDetail where memberId =?");
				ps.setInt(1, id);
				rs = ps.executeQuery();
				rs.next();
				
				int failedLoginCount = rs.getInt(1);
				ps.close();
								
				ps = conn.prepareStatement("update memberDetail set failedLoginCount = ? where memberId = ?");
				ps.setInt(1, failedLoginCount++);
				ps.setInt(2, id);			
				ps.executeUpdate();
				ps.close();
				
				if(failedLoginCount >= Integer.valueOf(enumMemberStandard.POSSIBILLTY_FAILD_LOGIN_NUM.toString())){
					ps = conn.prepareStatement("update memberState set isAbnormal = 1 , failedLogin = 1 where memberId = ?");
					ps.setInt(1, id);
					ps.executeUpdate();
					ps.close();
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
	 * @throws SQLException 
	 * @throws Throwable 
	 */
	public void doLogout() throws SQLException  {
			
		PreparedStatement _ps = conn.prepareStatement("update memberDetail set lastLogoutDate = ? where memberId = ?");
		_ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()) ) ;
		_ps.setInt(2,  id);
		_ps.executeUpdate();
	
		isLogout = true;
		isLogin = false;
	}
	
	public void doWithdraw() {
		
		
		
	}

	public boolean verify(){
		
		if(id<=0)
			return false;
		if(email==null && email.equals("") && email.contains("@")==false)
			return false;
		if(registrationKind==null)
			return false;
		if(nickname==null && nickname.equals(""))
			return false;
		
		return true;
		
	}

	
}

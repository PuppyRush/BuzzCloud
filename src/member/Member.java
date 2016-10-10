package member;

import java.awt.Point;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import member.enums.enumMemberAbnormalState;
import member.enums.enumMemberState;
import member.enums.enumMemberType;
import page.enums.enumPage;
import property.ConnectMysql;

import java.util.Set;
import java.util.EnumMap;
/**
 * member에 대한 객체 정보는 static 변수를 통해 참조하며 유저와 관련된 위젯들은 widget관련 변수를 통해 관리한다.
 * 	멤버 상태나 정보는 manageMember패캐지의 클래스들이나 MemberProess에서만 유일하게 조작되며 
 *     개개의 유저가 가진 위젯들의 상태는 WidgetProcess를 통해서만 조작된다.
 * @author cmk
 *
 */
public class Member {


	private static Map<String, Member> MemberMap = new HashMap<String, Member>();
	private static Connection conn = ConnectMysql.getConnector();
	
	private EnumMap<enumMemberAbnormalState, Boolean> abnormalState;
	
	public static final int DEFAULT_VALUE = -1;
	private int id;
	private int dev_id;
	private String nickname;
	private String planPassword;
	private enumMemberType userType;
	private String email;
	private Timestamp regDate;
	private String sessionId;
	private boolean isLogin;
	private boolean isLogout;
	private boolean isJoin;
	
	
	
	public Member(){
		id = DEFAULT_VALUE;
		dev_id = DEFAULT_VALUE;
		nickname="";
		planPassword="";
		abnormalState = new EnumMap<>(enumMemberAbnormalState.class);
		userType=enumMemberType.NOTHING;
		email="";
		regDate =  new Timestamp(System.currentTimeMillis());
		sessionId= "";
		
	}
	
	public Member(Member mdb){
		abnormalState = mdb.abnormalState; 
		dev_id = mdb.dev_id;
		id = mdb.id;
		nickname = new String(mdb.nickname);
		planPassword = new String(mdb.planPassword.toString());
		userType = mdb.userType;
		regDate = (Timestamp)mdb.regDate.clone();

	}
	

	
	////getter setter////
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String name) {
		this.nickname = name;
	}
	public String getPlanePassword() {
		return planPassword;
	}
	public void setPlanePassword(String password) {
		this.planPassword = password;
	}
	public enumMemberType getIdType() {
		return userType;
	}
	public void setIdType(enumMemberType idType) {
		this.userType = idType;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getSessionId(){
		return sessionId;
	}
	public void setSessionId(String arg){
		sessionId = arg;
	}
	public int getDeveloperId(){
		return dev_id;
	}
	public void setDeveloperId(int id){
		dev_id = id;
	}
	public boolean isLogin() {
		return isLogin;
	}

	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}

	public boolean isLogout() {
		return isLogout;
	}

	public void setLogout(boolean isLogout) {
		this.isLogout = isLogout;
	}

	public boolean isJoin() {	
			
		return isJoin;
	}

	public void setJoin(boolean isJoin) {
		this.isJoin = isJoin;
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

	public void setAbnormalState(EnumMap<enumMemberAbnormalState, Boolean> abnormalState) {
		this.abnormalState = abnormalState;
	}
	

	
	/////method/////
	
	/**
	 * 	로그인 시도중 가입한 사람이면  member객체의 정보를 모두 DB로 부터 읽어온다. 
	 * @param sId		sessionId
	 * @param nickname	페이지로부터 넘어온 nickname string
	 * @throws SQLException 
	 * @throws MemberException 
	 * @throws Throwable member, sql exception을 던진다
	 */
	public static Member setMemberFromDB(Member member) throws SQLException, MemberException{
		
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
			
			member.setNickname(_rs.getString("nickname"));
			member.setId(_rs.getInt("u_id"));
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
			
			_ps = conn.prepareStatement("select d_id from developer where u_id = ?");
			_ps.setInt(1, member.getId());
			_rs = _ps.executeQuery();
			
		}
		
		return member;
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
		
		Member member = null;
		
		
		if(Member.MemberMap.containsKey(sId)){
			return Member.MemberMap.get(sId);
		}
		else{
			
			member = new Member();			 
			member.setSessionId(sId);
			if(member.isJoin())
				Member.setMemberFromDB(member);
			MemberMap.put(sId, member);
		}
		
		return member;
	}
	
	public static Member getMember(int uId, String sId) throws SQLException, MemberException{
		
		if(uId<=0)
			throw new IllegalArgumentException("userId는 0보다 커야 합니다.");
		
	
				
		for(String key : MemberMap.keySet()){
			if(MemberMap.get(key).getId() == uId)
				return MemberMap.get(key);
		}
		
		Member member = new Member();			 
		member.setSessionId(sId);
		if(member.isJoin())
			Member.setMemberFromDB(member);
		MemberMap.put(sId, member);
	
		
		return member;
	}
	
	
	public static void addMember(Member m){
		
		if(MemberMap.containsKey(m))
			throw new IllegalArgumentException("맴버 객체가 이미 맵에 존합니다.");
				
		MemberMap.put(m.getSessionId(), m);
	}
		
	public static void removeMember(Member m){
		
		if(!MemberMap.containsValue(m))
			throw new IllegalArgumentException("맴버 객체가 존재하지 않습니다..");
		
		MemberMap.remove(m.getSessionId());
		m = null;
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
		
		return ( id==m.getId() && planPassword.equals(m.getPlanePassword()) ) ? true : false;
	}
	
	public static Member makePerfectMember(String id){
		Member m = new Member();
		m.setEmail("gooddaumi@naver.com");
		
		m.setId(32);
		m.setSessionId(id);
		m.setJoin(true);
		m.setLogin(true);
		m.setLogout(false);
		m.setNickname("cks1023");
		m.setPlanePassword("1234");
		
		return m;
	}


}

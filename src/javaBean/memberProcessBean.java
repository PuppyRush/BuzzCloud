
package javaBean;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Random;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import org.mindrot.jbcrypt.BCrypt;

public class memberProcessBean {
	
	private static Connection conn = connectMysql.connectMysql();
	private static memberDataBean instance = new memberDataBean();

	public static memberDataBean getInstance() {
		return instance;
	}

	public memberProcessBean() {
	}

	private Connection getConnection() throws Exception {
		Context initCtx = new InitialContext();
		Context envCtx = (Context) initCtx.lookup("java:comp/env");
		DataSource ds = (DataSource) envCtx.lookup("jdbc/basicjsp");
		return ds.getConnection();
	}

	/**
	   *   각 속성에 원하는 이름이 중복되는지 여부를 검사함.
	 * @param  1 : 테이블 
	 *	@param  2 : 속성 이름(들)
	 * @param  3 : 값 이름(들)
	 * @return 각 속성에 대하여 중복이 발생시 중복결과에 맞게 배열로 그 결과들을 반환함.
	 */
	public boolean[] confirmAttribute(String tableName, String [] attr, String [] val)
			throws Exception {

		if(attr.length != val.length)
			throw new Exception("속성파라매터와 값 파라매터의 갯수가 일치하지 않습니다.");

		boolean []isDuplicated = new boolean[attr.length];
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			for(int i=0; i < attr.length ; i++){
				switch (attr[i]) {
	
					case "Id" :
						ps = conn.prepareStatement(
								"select count(*) as size from UserMapping where Id = ?");
						ps.setInt(1, Integer.parseInt(val[i]));
						break;
	
					case "Email" :
					case "Nickname" :
						ps = conn.prepareStatement("select count(*) as size from UserMapping where "
								+ attr[i] + " = ?");
	
						ps.setString(1, val[i]);
						break;
	
					default :
				}
				
				rs = ps.executeQuery();
				rs.next();
				// 중복아이디가 있는지 결과를 저장
				if (rs.getInt("size")>=1)
					isDuplicated[i] = true;
				else
					isDuplicated[i] = false;
				
			}
		

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException ex) {
				}
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException ex) {
				}
		}

		return isDuplicated;
	}

	/**
	 * UserMapping테이블의 Id가 중복되는지 여부를 검사한다.
	 * @return 기본키에 중복되지 않는 값을 반환한다.
	 */
	private String getUniqueId(){
		
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		
		while (true) {

			Statement stmt;
			try {
				stmt = conn.createStatement();
		
			rs = stmt.executeQuery(
					"select count(*) as size from UserMapping");
			rs.next();
			if (rs.getLong("size") > 99999999L)
				throw new SQLException("UserMapping테이블이 꽉 찼습니다.");

			String tempID = String.valueOf( (new Random()).nextInt(90000000)
					+ 10000000);
			
			boolean []isDuplicated = 
					confirmAttribute("UserMapping", new String[]{"Id"}, new String[]{tempID});
		
			if(isDuplicated[0] == false)
				return tempID;
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
	}
	
	private String getEncodedVal(String val){
		
		String hashPass = BCrypt.hashpw(val,  BCrypt.gensalt(12));
		return hashPass;
	}
	
	private boolean isEqualVal(String val, String hashedVal){
		
		if( BCrypt.checkpw(val, hashedVal))
			return true;
		else
			return false;		
		
	}

	/**
	 * @param member  가입할 유저의 정보의 객체 
	 * @param request jsp페이지로부터  넘어온 attribute값을 이용하기 위함 
	 * @return 가입이 무사히 성사됐는지 여부를 반환
	 * @throws SQLException 중복, 테이블이 꽉찼는지에 대한 예외가 발생됨 
	 * 홈페이지 내부가입자가 아닌 외부인증을 통하여 가입한 유저는 비밀번호가 공백이다.
	 */
	@SuppressWarnings("resource")
	public boolean joinMember(memberDataBean member, HttpServletRequest request) throws SQLException {

		ResultSet rs = null;
		PreparedStatement pstmt = null;

		try {
			conn.setAutoCommit(false);
			
			// user매핑테이블 추가
			String uniqId = getUniqueId();
			member.setId(Integer.parseInt( uniqId));
			
			boolean []isDuplicated = 
					confirmAttribute("userMapping"
											,new String[]{"Email"}
											,new String[]{member.getEmail()});
			if(isDuplicated[0]){
				throw new Exception(member.getEmail() + " 메일이 중복됩니다");
			}

			pstmt = conn.prepareStatement(
					"insert into UserMapping values (?,?)");
			pstmt.setInt(1, member.getId());
			pstmt.setString(2, member.getEmail());
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement(
					"insert into User values (?,?,?,?,?)");
			pstmt.setInt(1, member.getId());
			pstmt.setString(2, member.getNickname());
			pstmt.setString(3, getEncodedVal( member.getPassword()));
			pstmt.setString(4, member.getIdType());
			pstmt.setTimestamp(5, member.getReg_date());

			pstmt.executeUpdate();
		}
		catch(SQLException ex){
			if(conn!=null) 
				try{conn.rollback();}// Exception 발생시 rollback 한다.
					catch(SQLException ex1){
						System.out.println(ex1.getMessage());
						ex1.printStackTrace();
					}
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		}
		finally {
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException ex) {
				}
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException ex) {
				}
		}
		
		
		conn.commit();
		return true;
	}

	/**
	 * @param member  가입할 유저의 정보의 객체 
	 * @param request jsp페이지로부터  넘어온 attribute값을 이용하기 위함 
	 * @return 로그인 무사히 성사됐는지 여부를 반환
	 * @throws 
	 *  
	 */
	public boolean logonMember(memberDataBean member, HttpServletRequest request) throws SQLException {
		
		ResultSet rs = null;
		PreparedStatement pstmt = null;

		try {
			conn.setAutoCommit(false);
			
			pstmt = conn.prepareStatement(
					"select count(*) as size  (?,?)");
			pstmt.setInt(1, member.getId());
			pstmt.setString(2, member.getEmail());
			pstmt.executeUpdate();
			
		}
	
		return true;
	}
	
}

package javaBean;

import java.sql.*;
import property.constString;


public class connectMysql {

	public static Connection connectMysql(){
		
		
		Connection conn = null;
		try {
			
			constString url = constString.dbUrl;
			constString id = constString.dbId;
			constString pw = constString.dbPasswd;
			
			
			String jdbcUrl = url.getString();
			String dbId = id.getString();
			String dbPasswd = pw.getString();
					
			
			Class.forName( "com.mysql.jdbc.Driver");
			
			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPasswd);
			
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("db접속에 실패하였습니다\n" + e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(conn == null){
			System.out.println("db생성실패!");
			return null;
			
		}
		return conn;
		
		
		
	}
	
}

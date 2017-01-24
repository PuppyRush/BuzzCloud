package com.puppyrush.buzzcloud.property;

import java.sql.*;

import com.puppyrush.buzzcloud.dbAccess.enumMysql;


public class ConnectMysql {

	public static Connection getConnector(){
		
		
		Connection conn = null;
		try {
			
			enumMysql url = enumMysql.dbUrl;
			enumMysql id = enumMysql.dbId;
			enumMysql pw = enumMysql.dbPasswd;
			enumMysql driver = enumMysql.dbDriver;
			
			String jdbcUrl = url.getString();
			String dbId = id.getString();
			String dbPasswd = pw.getString();
					
			
			Class.forName( driver.getString());
			
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

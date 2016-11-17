package entity.authority;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.EnumMap;
import java.util.Iterator;

import entity.authority.band.enumBandAuthority;
import property.ConnectMysql;

public final class AuthorityDB {
	

	private static Connection conn = ConnectMysql.getConnector();


	private static class Singleton {
		private static final AuthorityDB instance = new AuthorityDB();
	}
	
	
	public static AuthorityDB getInstance () {
		return Singleton.instance;
	}
	
	
/*	public void updateBandAuthority(int bandId,  EnumMap<enumBandAuthority, Boolean> auths){
		
		StringBuilder sql = new StringBuilder("update bandAuthority set ");
		
		Iterator<enumBandAuthority> it = auths.keySet().iterator();
		
		
		try {
			PreparedStatement ps = conn.prepareStatement("");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
*/
}

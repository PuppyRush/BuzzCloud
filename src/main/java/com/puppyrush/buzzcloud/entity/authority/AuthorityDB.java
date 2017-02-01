package com.puppyrush.buzzcloud.entity.authority;

import java.sql.Connection;
import org.springframework.stereotype.Service;

import com.puppyrush.buzzcloud.property.ConnectMysql;

@Service("authorityDB")
public final class AuthorityDB {
	

	private static Connection conn = ConnectMysql.getConnector();

	
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

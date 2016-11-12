package entity.band;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import entity.EntityException;
import entity.enumEntityState;
import property.ConnectMysql;
import property.enums.enumSystem;

public class BandDB {


	protected Connection conn = ConnectMysql.getConnector();

	private static class Singleton {
		private static final BandDB instance = new BandDB();
	}

	public static BandDB getInstance() {
		return Singleton.instance;
	}

	public int makeBand(String name, int owner, int admin){
		
		int key = -1;
		
		try{
			conn.setAutoCommit(false);
			
			PreparedStatement ps = conn.prepareStatement("insert into band ( name, owner, administrator) values(?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setString(1, name);
			ps.setInt(2,owner);
			ps.setInt(3,admin);
			
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			
			key = rs.getInt(1);
			
			conn.commit();
			ps.close();
			rs.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		return key;
	}
	
	public void makeBandDetail(int bandId, int maxCapacity, String contents){
		
		String driverPath = enumSystem.DEFAULT_PATH + UUID.randomUUID().toString()+"/";
	
		try {
			conn.setAutoCommit(false);
			
			String sql = "insert into bandDetail (bandId, maxCapacity, driverPath, contents) values(?,?,?,?)";
			PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setInt(1, bandId);
			ps.setInt(2, maxCapacity);
			ps.setString(3, driverPath);
			ps.setString(4, contents);
			ps.executeUpdate();
			
			ps.close();
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void makeBandRelation(int fromId, int toId){
		
		try {
			conn.setAutoCommit(false);
			PreparedStatement ps = conn.prepareStatement("insert into bandRelation (fromBand, toBand) values(?,?)");
			ps.setInt(1, fromId);
			ps.setInt(2, toId);
			ps.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void makeBandMember(int bandId, ArrayList<Integer> memberIds){
		
		try{		
			conn.setAutoCommit(false);
			String sql = "insert into bandMember (bandId, memberId) values (?,?)";
			for(int i=0 ; i < memberIds.size() ; i++){
				
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setInt(1, bandId);
				ps.setInt(2, memberIds.get(i));
				ps.executeUpdate();
				ps.close();
			}
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getIdOfName(String bandName, int upperBandId){
		
		int bandId=-1;
	
		try {
			
			String sql = "select fromBand from bandRelation where to = ?";
			PreparedStatement ps = conn.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setInt(1, upperBandId);
			ResultSet rs = ps.executeQuery();
			
			ArrayList<Integer> ids = new ArrayList<Integer>();
			while(rs.next()){
				int id = rs.getInt(1);
				ids.add(id);
			}
			ps.close();
			rs.close();			
			
			if(ids.size()==0)
				throw new IllegalArgumentException("잘못된 bandId 매개변수입니다 :" + upperBandId);
			
			for(int id : ids){
				ps = conn.prepareStatement("select bandName,bandId from band where bandId = ?");
				ps.setLong(1, id);
				rs = ps.executeQuery();
				rs.next();
				
				if(bandName.equals(rs.getString("bandName"))){
					bandId = rs.getInt("bandId");
					break;
				}	
			}
			
			throw new IllegalArgumentException("잘못된 bandName 매개변수입니다 : " + bandName);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return bandId;
		
	}
}

package com.puppyrush.buzzcloud.entity.band;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.puppyrush.buzzcloud.property.ConnectMysql;
import com.puppyrush.buzzcloud.property.enums.enumSystem;

@Service("bandDB")
public class BandDB {

	protected Connection conn = ConnectMysql.getConnector();


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
	
	/**
	 * 
	 * @param bandId
	 * @param maxCapacity
	 * @param contents
	 * @return	driverPath를 반환함.
	 */
	public String makeBandDetail(int bandId, int maxCapacity, String contents){
		
		String driverFolderPath = enumSystem.DEFAULT_DIRVER_PATH + UUID.randomUUID().toString()+"/";
		makeDirverFolder(driverFolderPath);
		
		try {
			conn.setAutoCommit(false);
			
			String sql = "insert into bandDetail (bandId, maxCapacity, driverPath, contents) values(?,?,?,?)";
			PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setInt(1, bandId);
			ps.setInt(2, maxCapacity);
			ps.setString(3, driverFolderPath);
			ps.setString(4, contents);
			ps.executeUpdate();
			
			ps.close();
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return driverFolderPath;
	}
			 
	private void makeDirverFolder(String folderName){
		
		File file = new File(folderName);
		
		do{
			if(file.exists())
				folderName =  enumSystem.DEFAULT_DIRVER_PATH + UUID.randomUUID().toString()+"/";
			else{
				file.mkdirs();
				break;
			}
				
			
			
		}while(true);
		
		
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

 	public void makeBandRequestJoin(int bandId, int memberId){
		
		try {
			conn.setAutoCommit(false);
			PreparedStatement ps = conn.prepareStatement("insert into bandRequestJoin (bandId, memberId) values(?,?)");
		
			ps.setInt(1, bandId);
			ps.setInt(2, memberId);
			ps.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void makeBandMember(int bandId, int memberId){
		
		try{		
			conn.setAutoCommit(false);
			String sql = "insert into bandMember (bandId, memberId) values (?,?)";
			
				
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, bandId);
			ps.setInt(2, memberId);
			ps.executeUpdate();
			ps.close();
		
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

	public String getBandNameOf(int bandId){
		
		String name=null;
		
		try {
			PreparedStatement ps = conn.prepareStatement("select name from band where bandId = ?");
			ps.setInt(1, bandId);
			ResultSet rs = ps.executeQuery();
			rs.next();
			
			name = rs.getString(1);
			
			ps.close();
			rs.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return name;
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
				ps = conn.prepareStatement("select name,bandId from band where bandId = ?");
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

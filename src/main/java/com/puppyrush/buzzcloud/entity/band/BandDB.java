package com.puppyrush.buzzcloud.entity.band;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.puppyrush.buzzcloud.controller.form.BandForm;
import com.puppyrush.buzzcloud.dbAccess.DBManager;
import com.puppyrush.buzzcloud.entity.message.enums.InstanceMessageType;
import com.puppyrush.buzzcloud.entity.message.instanceMessage.InstanceMessage;
import com.puppyrush.buzzcloud.property.ConnectMysql;
import com.puppyrush.buzzcloud.property.enums.enumSystem;

@Service("bandDB")
public class BandDB {

	protected Connection conn = ConnectMysql.getConnector();

	@Autowired(required = false)
	private DBManager dbMng;	
	
	@Autowired(required = false)
	private BandManager bMng;
	
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
	

	public void makeBandDetail(int newBandId, int maxCapacity,String driverPath, String contents) throws SQLException, IOException{
								
		try {
			conn.setAutoCommit(false);
			
			String sql = "insert into bandDetail (bandId, maxCapacity, driverPath, contents) values(?,?,?,?)";
			PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setInt(1, newBandId );
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

 	public Map<String,Object> makeBandRequestJoin(int bandId, int memberId) throws SQLException{
		
 		Map<String, Object> returns = new HashMap<String, Object>();
 		
 		HashMap<String, Object> where = new HashMap<String, Object>();
		
		where.put("memberId", memberId);
		where.put("bandId", bandId);
 		
		List<Map<String, Object>> result = dbMng.getColumnsOfAll("bandRequestJoin",where);
		if(!result.isEmpty()){
			if((int)result.get(0).get("memberId")==memberId && (int)result.get(0).get("bandId")==bandId){
				
				Timestamp date = (Timestamp)result.get(0).get("requestDate");
				StringBuilder bld = new StringBuilder();
				bld.append("이미 가입요청을 하셨습니다. (");
				bld.append(date.valueOf("2009-10-03 20:10:10").toString());
				bld.append(")\n그룹 소유주의 허가를 기다리시거나 설정페이지에서 가입요청을 철회하세요.");
				
				InstanceMessage msg = new InstanceMessage(bld.toString(),InstanceMessageType.SUCCESS);
				returns.putAll(msg.getMessage());
				returns.put("isSuccess", false);
			}
		}
		else{
			conn.setAutoCommit(false);
			PreparedStatement ps = conn.prepareStatement("insert into bandRequestJoin (bandId, memberId) values(?,?)");
		
			ps.setInt(1, bandId);
			ps.setInt(2, memberId);
			ps.executeUpdate();
			conn.commit();
			
			returns.put("isSuccess", true);
			
			InstanceMessage msg = new InstanceMessage("그룹에 가입이 신청되었습니다. 승인이 되면 사용이 가능합니다.",InstanceMessageType.SUCCESS);
			returns.putAll(msg.getMessage());
		}
		return returns;
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

	public void makeBandMember(int bandId, List<Integer> memberIds){
		
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

	public List<Integer> getBandMembers(int bandId) throws SQLException{
		
		List<Integer> members = new ArrayList<Integer>();
		
		PreparedStatement ps = conn.prepareStatement("select memberId from bandMember where bandId = ?");
		ps.setInt(1, bandId);
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			members.add(rs.getInt(1));
		}
		return members;		
	}
	
	public int getRootMemberOf(int bandId) throws SQLException{
		
		int rootId = bMng.getRootBandOf(bandId);
		
		PreparedStatement ps = conn.prepareStatement("select owner from band where bandId = ?");
		ps.setInt(1, rootId);
		ResultSet rs = ps.executeQuery();
		rs.next();
		
		return rs.getInt(1);
		
	}
	
	public int getOwnerMemberOf(int bandId) throws SQLException{
		
		PreparedStatement ps = conn.prepareStatement("select owner from band where bandId = ?");
		ps.setInt(1, bandId);
		ResultSet rs = ps.executeQuery();
		rs.next();
		
		return rs.getInt(1);
		
	}

	public int getAdminMemberOf(int bandId) throws SQLException{
		
		PreparedStatement ps = conn.prepareStatement("select administrator from band where bandId = ?");
		ps.setInt(1, bandId);
		ResultSet rs = ps.executeQuery();
		rs.next();
		
		return rs.getInt(1);
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


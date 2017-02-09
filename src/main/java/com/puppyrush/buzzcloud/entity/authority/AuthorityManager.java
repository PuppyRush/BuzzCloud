package com.puppyrush.buzzcloud.entity.authority;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.puppyrush.buzzcloud.dbAccess.DBManager;
import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.authority.band.BandAuthority;
import com.puppyrush.buzzcloud.entity.authority.band.enumBandAuthority;
import com.puppyrush.buzzcloud.entity.authority.file.FileAuthority;
import com.puppyrush.buzzcloud.entity.authority.file.enumFileAuthority;
import com.puppyrush.buzzcloud.entity.authority.member.MemberAuthority;
import com.puppyrush.buzzcloud.entity.authority.member.enumMemberAuthority;
import com.puppyrush.buzzcloud.entity.band.Band;
import com.puppyrush.buzzcloud.property.ConnectMysql;

@Service("authorityManager")
public final class AuthorityManager {

	protected Connection conn = ConnectMysql.getConnector();

	@Autowired
	DBManager dbMng;
	
	@Autowired
	AuthorityController authCtl;

 	public FileAuthority getFileAuthoirty(int memberId, int bandId) throws ControllerException {

 		
		FileAuthority fAuth = null;

		try {

			Map<String, Object> where = new HashMap<String, Object>();
			where.put("memberId", memberId);
			where.put("bandId", bandId);
			List<Map<String, Object>> res = dbMng.getColumnsOfAll("fileAuthority", where);
			
			if(res.size()>1)
				throw new SQLException("no more 1 of filAuthority");

			int authId = (Integer)res.get(0).get("authorityId");
			Timestamp time = (Timestamp)res.get(0).get("grantedDate");
			
			if(authCtl.containsEntity(FileAuthority.class,authId))
				return authCtl.getEntity(FileAuthority.class, authId);
			
			
			EnumMap<enumFileAuthority, Boolean> auths = new EnumMap<enumFileAuthority, Boolean>(enumFileAuthority.class);
			if ((Integer)res.get(0).get("canRemove") == 1)
				auths.put(enumFileAuthority.REMOVE, true);
			else
				auths.put(enumFileAuthority.REMOVE, false);

			if ((Integer)res.get(0).get("canDownload")== 1)
				auths.put(enumFileAuthority.DOWNLOAD, true);
			else
				auths.put(enumFileAuthority.DOWNLOAD, false);

			if ((Integer)res.get(0).get("canUpload") == 1)
				auths.put(enumFileAuthority.UPLOAD, true);
			else
				auths.put(enumFileAuthority.UPLOAD, false);

			if ((Integer)res.get(0).get("canCreate") == 1)
				auths.put(enumFileAuthority.CREATE, true);
			else
				auths.put(enumFileAuthority.CREATE, false);

			
			
			fAuth = new FileAuthority(authId, time, auths);
			authCtl.addEntity(fAuth);
			
		} catch (SQLException e) {

			EnumMap<enumFileAuthority, Boolean> auths = new EnumMap<enumFileAuthority, Boolean>(
					enumFileAuthority.class);
			auths.put(enumFileAuthority.CREATE, false);
			auths.put(enumFileAuthority.DOWNLOAD, false);
			auths.put(enumFileAuthority.REMOVE, false);
			auths.put(enumFileAuthority.UPLOAD, false);

			fAuth = new FileAuthority(-1, new Timestamp(System.currentTimeMillis()), auths);

			e.printStackTrace();
		}

		return fAuth;
	}

	public BandAuthority getBandAuthority(int bandId) {

		BandAuthority gAuth = null;

		try {

			PreparedStatement ps = conn.prepareStatement("select * from bandAuthority where bandId = ?");
			ps.setInt(1, bandId);
			ResultSet rs = ps.executeQuery();

			rs.next();

			bandId = rs.getInt("bandId");

			EnumMap<enumBandAuthority, Boolean> auths = new EnumMap<enumBandAuthority, Boolean>(
					enumBandAuthority.class);

			if (rs.getInt("isFinal") == 1)
				auths.put(enumBandAuthority.FINAL, true);
			else
				auths.put(enumBandAuthority.FINAL, false);

			if (rs.getInt("isRoot") == 1)
				auths.put(enumBandAuthority.ROOT, true);
			else
				auths.put(enumBandAuthority.ROOT, false);

			if (rs.getInt("canJoinMember") == 1)
				auths.put(enumBandAuthority.CAN_JOIN_MEMBER, true);
			else
				auths.put(enumBandAuthority.CAN_JOIN_MEMBER, false);

			gAuth = new BandAuthority(rs.getInt("bandId"), rs.getTimestamp("grantedDate"), auths);

			ps.close();
			rs.close();

		} catch (SQLException e) {

			EnumMap<enumBandAuthority, Boolean> auths = new EnumMap<enumBandAuthority, Boolean>(
					enumBandAuthority.class);
			auths.put(enumBandAuthority.CAN_JOIN_MEMBER, false);
			auths.put(enumBandAuthority.ROOT, false);
			auths.put(enumBandAuthority.FINAL, false);

			gAuth = new BandAuthority(-1, new Timestamp(System.currentTimeMillis()), auths);
			e.printStackTrace();
		}

		return gAuth;

	}

	public MemberAuthority getMemberAuthority(int ownerId, int bandId) throws EntityException {

		MemberAuthority mAuth = null;

		try {

			PreparedStatement ps = conn.prepareStatement("select * from memberAuthority where bandId = ? and memberId = ?");
			ps.setInt(1, bandId);
			ps.setInt(2, ownerId);
			ResultSet rs = ps.executeQuery();
			rs.next();
			
			int memberTypeValue= rs.getInt("memberType");
			enumMemberAuthority memberType = enumMemberAuthority.VIEWER;
			
			boolean isExist = false;
			for(enumMemberAuthority auths : enumMemberAuthority.values())
				if(auths.toInteger() == memberTypeValue ){
					isExist = true;
					memberType = auths;
					break;
				}
			if(!isExist)
				throw new EntityException(enumAuthorityState.NOT_EXIST_AUTHORITY);
			
			int authId = rs.getInt("authorityId");
			Timestamp grantedDate = rs.getTimestamp("grantedDate");
			
			mAuth = new MemberAuthority( authId, grantedDate , memberType);
		
			ps.close();
			rs.close();

		} catch (SQLException e) {
			mAuth = new MemberAuthority(-1, new Timestamp(System.currentTimeMillis()),
					enumMemberAuthority.VIEWER);
			e.printStackTrace();
		}

		return mAuth;
	}

	public MemberAuthority makeMemberAuthority(int memberId, int bandId, enumMemberAuthority auth){
		
		MemberAuthority mAuth = null;
		
		try {
			
			PreparedStatement ps = conn.prepareStatement("insert into memberAuthority (memberId, bandId, memberType) values(?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setInt(1, memberId);
			ps.setInt(2, bandId);
			ps.setInt(3, auth.toInteger());
			ps.executeUpdate();
			
			ResultSet rs =  ps.getGeneratedKeys();
			rs.next();
			int authKey = rs.getInt(1);
			
			mAuth = new MemberAuthority(authKey, new Timestamp(System.currentTimeMillis()), auth);
			
			ps.close();
			rs.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			mAuth = new MemberAuthority(-1,  new Timestamp(System.currentTimeMillis()), enumMemberAuthority.VIEWER);
			e.printStackTrace();
		}
		
		return mAuth;
	}
	
	public BandAuthority makeBandAuthority(int bandId,  EnumMap<enumBandAuthority, Boolean> auths){
		
		BandAuthority bandAuth = null;
		
		try {
			
			PreparedStatement ps = conn.prepareStatement("insert into bandAuthority (bandId, isFinal, isRoot,"
					+ "canJoinMember, canViewUpperBand , canViewSiblingBand, canViewSubBand, canMakeSubBand, canMakeSiblingBand ) values(?,?, ?,?,?, ?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setInt(1, bandId);
			
			if( auths.containsKey(enumBandAuthority.FINAL) )
				ps.setInt(2, auths.get(enumBandAuthority.FINAL) ? 1 : 0);
			else
				ps.setInt(2, 0);
			
			if( auths.containsKey(enumBandAuthority.ROOT) )
				ps.setInt(3, auths.get(enumBandAuthority.ROOT) ? 1 : 0);
			else
				ps.setInt(3, 0);
			
			
			if( auths.containsKey(enumBandAuthority.CAN_JOIN_MEMBER) )
				ps.setInt(4, auths.get(enumBandAuthority.CAN_JOIN_MEMBER) ? 1 : 0);
			else
				ps.setInt(4, 0);
			
			
			if( auths.containsKey(enumBandAuthority.CAN_VIEW_UPPER_BAND) )
				ps.setInt(5, auths.get(enumBandAuthority.CAN_VIEW_UPPER_BAND) ? 1 : 0);
			else
				ps.setInt(5, 0);
			
			if( auths.containsKey(enumBandAuthority.CAN_VIEW_SIBILING_BAND) )
				ps.setInt(6, auths.get(enumBandAuthority.CAN_VIEW_SIBILING_BAND) ? 1 : 0);
			else
				ps.setInt(6, 0);
			
			
			
			if( auths.containsKey(enumBandAuthority.CAN_VIEW_SUB_BAND) )
				ps.setInt(7, auths.get(enumBandAuthority.CAN_VIEW_SUB_BAND) ? 1 : 0);
			else
				ps.setInt(7, 0);
			
			
			if( auths.containsKey(enumBandAuthority.CAN_MAKE_SUB_BAND) )
				ps.setInt(8, auths.get(enumBandAuthority.CAN_MAKE_SUB_BAND) ? 1 : 0);
			else
				ps.setInt(8, 0);
			
			
			if( auths.containsKey(enumBandAuthority.CAN_MAKE_SIBLING_BAND) )
				ps.setInt(9, auths.get(enumBandAuthority.CAN_MAKE_SIBLING_BAND) ? 1 : 0);
			else
				ps.setInt(9, 0);

			ps.executeUpdate();
			
			ResultSet rs =  ps.getGeneratedKeys();
			rs.next();
			int authKey = rs.getInt(1);
			
			bandAuth = new BandAuthority(authKey, new Timestamp(System.currentTimeMillis()), auths);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			
			EnumMap<enumBandAuthority, Boolean> a = new EnumMap<enumBandAuthority, Boolean>(enumBandAuthority.class);
			a.put(enumBandAuthority.CAN_JOIN_MEMBER, true);
			a.put(enumBandAuthority.CAN_MAKE_SIBLING_BAND, true);
			a.put(enumBandAuthority.CAN_MAKE_SUB_BAND, true);
			a.put(enumBandAuthority.CAN_VIEW_SUB_BAND, false);
			a.put(enumBandAuthority.CAN_VIEW_UPPER_BAND, false);
			a.put(enumBandAuthority.FINAL, false);
			a.put(enumBandAuthority.ROOT, false);
			
			
			bandAuth = new BandAuthority(-1,  new Timestamp(System.currentTimeMillis()),a);
			e.printStackTrace();
		}
		
		return bandAuth;
		
	}
	
	public FileAuthority makeFileAuthoirty(int memberId, int bandId,  EnumMap<enumFileAuthority, Boolean> auths){
		

		FileAuthority fAuth = null;
		
		try {
			
			PreparedStatement ps = conn.prepareStatement("insert into fileAuthority (bandId,memberId, canRemove, "
					+ "canCreate, canDownload, canUpload) values(?,?,?, ?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setInt(1, bandId);
			ps.setInt(2, memberId);
			
			if( auths.containsKey(enumFileAuthority.REMOVE) )
				ps.setInt(3, auths.get(enumFileAuthority.REMOVE) ? 1 : 0);
			else
				ps.setInt(3, 0);
			
			if( auths.containsKey(enumFileAuthority.CREATE) )
				ps.setInt(4, auths.get(enumFileAuthority.CREATE) ? 1 : 0);
			else
				ps.setInt(4, 0);
			
			if( auths.containsKey(enumFileAuthority.DOWNLOAD) )
				ps.setInt(5, auths.get(enumFileAuthority.DOWNLOAD) ? 1 : 0);
			else
				ps.setInt(5, 0);
						
			if( auths.containsKey(enumFileAuthority.UPLOAD) )
				ps.setInt(6, auths.get(enumFileAuthority.UPLOAD) ? 1 : 0);
			else
				ps.setInt(6, 0);	
	
			ps.executeUpdate();
			
			ResultSet rs =  ps.getGeneratedKeys();
			rs.next();
			int authKey = rs.getInt(1);
			
			fAuth = new FileAuthority(authKey, new Timestamp(System.currentTimeMillis()), auths);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			
			EnumMap<enumFileAuthority, Boolean> a = new EnumMap<enumFileAuthority, Boolean>(enumFileAuthority.class);
			a.put(enumFileAuthority.CREATE, false);
			a.put(enumFileAuthority.DOWNLOAD, false);
			a.put(enumFileAuthority.REMOVE, false);
			a.put(enumFileAuthority.UPLOAD, false);

			fAuth = new FileAuthority(-1,  new Timestamp(System.currentTimeMillis()),a);
			e.printStackTrace();
		}
		
		return fAuth;
		
	}


}


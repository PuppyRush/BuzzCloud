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
import com.puppyrush.buzzcloud.dbAccess.DBManager.ColumnHelper;
import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.authority.band.BandAuthority;
import com.puppyrush.buzzcloud.entity.authority.band.enumBandAuthority;
import com.puppyrush.buzzcloud.entity.authority.file.FileAuthority;
import com.puppyrush.buzzcloud.entity.authority.file.enumFileAuthority;
import com.puppyrush.buzzcloud.entity.authority.member.MemberAuthority;
import com.puppyrush.buzzcloud.entity.authority.member.enumMemberAuthority;
import com.puppyrush.buzzcloud.entity.band.Band;
import com.puppyrush.buzzcloud.page.enums.enumPage;
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
			ColumnHelper ch = dbMng.getColumnsOfAll("fileAuthority", where);
			
			if(ch.columnSize()!=1)
				throw new SQLException("ColumnHelper has no more 1 or cant empty of  filAuthority");

			int authId = ch.getInteger(0, "authorityId");
			Timestamp time = ch.getTimestamp(0, "grantedDate");
			
			if(authCtl.containsEntity(FileAuthority.class,authId))
				return authCtl.getEntity(FileAuthority.class, authId);
			
			
			EnumMap<enumFileAuthority, Boolean> auths = new EnumMap<enumFileAuthority, Boolean>(enumFileAuthority.class);
			for(enumFileAuthority fileauth : enumFileAuthority.values()){
				if (ch.getInteger(0, fileauth.getString()) == 1)
					auths.put(fileauth, true);
				else
					auths.put(fileauth, false);
			}
						
			fAuth = new FileAuthority(authId, time, auths);
			authCtl.addEntity(fAuth);
			
		} catch (SQLException e) {

			EnumMap<enumFileAuthority, Boolean> auths = new EnumMap<enumFileAuthority, Boolean>(
					enumFileAuthority.class);
			auths.put(enumFileAuthority.CREATES, false);
			auths.put(enumFileAuthority.DOWNLOAD, false);
			auths.put(enumFileAuthority.REMOVE, false);
			auths.put(enumFileAuthority.UPLOAD, false);

			fAuth = new FileAuthority(-1, new Timestamp(System.currentTimeMillis()), auths);
		}

		return fAuth;
	}

	public BandAuthority getBandAuthority(int bandId) throws SQLException {

		BandAuthority gAuth = null;
	
		try{
			Map<String, Object> where = new HashMap<String, Object>();
			where.put("bandId", bandId);
			ColumnHelper ch = dbMng.getColumnsOfAll("bandAuthority", where);
			
			if(ch.columnSize()!=1)
				throw new SQLException("ColumnHelper has no more one or cant empty of bandAuthority");
	
			bandId = ch.getInteger(0, "bandId");
			int authId = ch.getInteger(0, "authorityId");
			Timestamp time = ch.getTimestamp(0, "grantedDate");
			
			if(authCtl.containsEntity(BandAuthority.class,authId))
				return authCtl.getEntity(BandAuthority.class, authId);
			
			
			EnumMap<enumBandAuthority, Boolean> auths = new EnumMap<enumBandAuthority, Boolean>(
					enumBandAuthority.class);
	
			for(enumBandAuthority bandauth : enumBandAuthority.values()){
				if (ch.getInteger(0, bandauth.getString()) == 1)
					auths.put(bandauth, true);
				else
					auths.put(bandauth, false);
			}
		
			gAuth = new BandAuthority(bandId, time, auths);

		}
		catch (SQLException e) {

			EnumMap<enumBandAuthority, Boolean> auths = new EnumMap<enumBandAuthority, Boolean>(
					enumBandAuthority.class);
			auths.put(enumBandAuthority.CAN_JOIN_MEMBER, false);
			auths.put(enumBandAuthority.ROOT, false);
			auths.put(enumBandAuthority.FINAL, false);

			gAuth = new BandAuthority(-1, new Timestamp(System.currentTimeMillis()), auths);
		}
		

		return gAuth;

	}

	public MemberAuthority getMemberAuthority(int ownerId, int bandId) throws EntityException, SQLException {

		MemberAuthority mAuth = null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {

			ps = conn.prepareStatement("select * from memberAuthority where bandId = ? and memberId = ?");
			ps.setInt(1, bandId);
			ps.setInt(2, ownerId);
			rs = ps.executeQuery();
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
				throw (new EntityException.Builder(enumPage.ERROR404))
				.errorString("비 정상적인 접근입니다.")
				.errorCode(enumAuthorityState.NOT_EXIST_AUTHORITY).build(); 
			
			int authId = rs.getInt("authorityId");
			Timestamp grantedDate = rs.getTimestamp("grantedDate");
			
			mAuth = new MemberAuthority( authId, grantedDate , memberType);

		} catch (SQLException e) {
			mAuth = new MemberAuthority(-1, new Timestamp(System.currentTimeMillis()),
					enumMemberAuthority.VIEWER);
		}
		finally{
			if(ps!=null)
				ps.close();
			if(rs!=null)
				rs.close();
		}

		return mAuth;
	}

	public MemberAuthority makeMemberAuthority(int memberId, int bandId, enumMemberAuthority auth) throws SQLException{
		
		MemberAuthority mAuth = null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			
			ps = conn.prepareStatement("insert into memberAuthority (memberId, bandId, memberType) values(?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setInt(1, memberId);
			ps.setInt(2, bandId);
			ps.setInt(3, auth.toInteger());
			ps.executeUpdate();
			
			rs =  ps.getGeneratedKeys();
			rs.next();
			int authKey = rs.getInt(1);
			
			mAuth = new MemberAuthority(authKey, new Timestamp(System.currentTimeMillis()), auth);
			
			ps.close();
			rs.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			mAuth = new MemberAuthority(-1,  new Timestamp(System.currentTimeMillis()), enumMemberAuthority.VIEWER);
		}
		finally{
			if(ps!=null)
				ps.close();
			if(rs!=null)
				rs.close();
		}

		return mAuth;
	}
	
	public BandAuthority makeBandAuthority(int bandId,  Map<enumBandAuthority, Boolean> auths) throws SQLException{
		
		BandAuthority bandAuth = null;
		try {
			
			List<String> col = new ArrayList<String>();
			
			List<List<Object>> values = new ArrayList<List<Object>>();
			List<Object> _col = new ArrayList<Object>();
			
			col.add("bandId");
			_col.add(bandId);
			
			for(enumBandAuthority bAuth : enumBandAuthority.values()){
				col.add(bAuth.getString());
				
				if( auths.containsKey(bAuth) )
					_col.add(auths.get(bAuth) ? 1 : 0);
				else
					_col.add(0);				
			}
			values.add(_col);
			
			List<Integer> keys = dbMng.insertColumn("bandAuthority", col, values);
			if(keys.size() != 1)
				throw new SQLException();
			
			int authKey = keys.get(0);

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
		}

		return bandAuth;
		
	}
	
	public FileAuthority makeFileAuthoirty(int memberId, int bandId,  Map<enumFileAuthority, Boolean> auths) throws SQLException{
		

		FileAuthority fAuth = null;
		try {
			
			List<String> col = new ArrayList<String>();
			
			List<List<Object>> values = new ArrayList<List<Object>>();
			List<Object> _col = new ArrayList<Object>();
			
			col.add("bandId");
			col.add("memberId");
			_col.add(bandId);
			_col.add(memberId);
			
			for(enumFileAuthority fileAuth : enumFileAuthority.values()){
				col.add(fileAuth.getString());
				
				if( auths.containsKey(fileAuth) )
					_col.add(auths.get(fileAuth) ? 1 : 0);
				else
					_col.add(0);				
			}
			values.add(_col);
			
			List<Integer> keys = dbMng.insertColumn("fileAuthority", col, values);
			if(keys.size() != 1)
				throw new SQLException();
			
			int authKey = keys.get(0);
			
			fAuth = new FileAuthority(authKey, new Timestamp(System.currentTimeMillis()), auths);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			
			EnumMap<enumFileAuthority, Boolean> a = new EnumMap<enumFileAuthority, Boolean>(enumFileAuthority.class);
			a.put(enumFileAuthority.CREATES, false);
			a.put(enumFileAuthority.DOWNLOAD, false);
			a.put(enumFileAuthority.REMOVE, false);
			a.put(enumFileAuthority.UPLOAD, false);

			fAuth = new FileAuthority(-1,  new Timestamp(System.currentTimeMillis()),a);
		}
		
		return fAuth;
		
	}


}


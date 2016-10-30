package entity.authority;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import entity.EntityException;
import entity.authority.band.BandAuthority;
import entity.authority.band.enumBandAuthority;
import entity.authority.file.FileAuthority;
import entity.authority.file.enumFileAuthority;
import entity.authority.member.MemberAuthority;
import entity.authority.member.enumMemberAuthority;
import entity.band.Band;
import property.ConnectMysql;

public class AuthorityManager {

	protected Connection conn = ConnectMysql.getConnector();

	private static class Singleton {
		private static final AuthorityManager instance = new AuthorityManager();
	}

	public static AuthorityManager getInstance() {
		return Singleton.instance;
	}
 	public FileAuthority getFileAuthoirty(int memberId, int bandId) {

		FileAuthority fAuth = null;

		try {

			PreparedStatement ps = conn
					.prepareStatement("select * from fileAuthority where bandId = ? and memberId = ?");
			ps.setInt(1, bandId);
			ps.setInt(2, memberId);
			ResultSet rs = ps.executeQuery();
			rs.next();

			bandId = rs.getInt("bandId");

			EnumMap<enumFileAuthority, Boolean> auths = new EnumMap<enumFileAuthority, Boolean>(enumFileAuthority.class);
			if (rs.getInt("canRemove") == 1)
				auths.put(enumFileAuthority.REMOVE, true);
			else
				auths.put(enumFileAuthority.REMOVE, false);

			if (rs.getInt("canDownload") == 1)
				auths.put(enumFileAuthority.DOWNLOAD, true);
			else
				auths.put(enumFileAuthority.DOWNLOAD, false);

			if (rs.getInt("canUpload") == 1)
				auths.put(enumFileAuthority.UPLOAD, true);
			else
				auths.put(enumFileAuthority.UPLOAD, false);

			if (rs.getInt("canCreate") == 1)
				auths.put(enumFileAuthority.CREATE, true);
			else
				auths.put(enumFileAuthority.CREATE, false);

			fAuth = new FileAuthority(rs.getInt("authorityId"), rs.getTimestamp("grantedDate"), auths);

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
			
			String memberTypeString = String.valueOf(rs.getInt("memberType"));
			enumMemberAuthority memberType = enumMemberAuthority.VIEWER;
			
			boolean isExist = false;
			for(enumMemberAuthority auths : enumMemberAuthority.values())
				if(auths.toString().equals( memberTypeString )){
					isExist = true;
					memberType = auths;
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

}

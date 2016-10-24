package entity.group;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;

import entity.EntityControllerImpl;
import entity.member.MemberController;
import entity.ControllerException;
import property.ConnectMysql;

public final class GroupController extends EntityControllerImpl{

	private GroupController(){}

	
	private static class Singleton {
		private static final GroupController instance = new GroupController();
	}
	
	public static GroupController getInstance () {
		return Singleton.instance;
	}

	private Group getGroupFromDB(int gId) throws SQLException{
		
		PreparedStatement ps = conn.prepareStatement("select * from group where groupId = ? ");
		ps.setInt(1, gId);
		ResultSet rs = ps.executeQuery();
		
		rs.next();
		
		int ownerId = rs.getInt("groupOwner");
		String groupName = rs.getString("groupName");
		Timestamp date = rs.getTimestamp("createdDate");
		
		ps.close();
		rs.close();
			
		return new Group.Builder(ownerId, gId, groupName).createdDate(date).build();
				
	
	}

	
}

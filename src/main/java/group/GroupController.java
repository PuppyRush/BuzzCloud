package group;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;

import property.ConnectMysql;
import property.ControllerException;
import property.enums.enumController;

public class GroupController {

	private static HashMap<Integer, Group> groups = new HashMap<Integer, Group>();
	private static Connection conn = ConnectMysql.getConnector();
	
	private GroupController(){}
	

	public static boolean containsGroup(int groupId){
		
		return groups.containsKey(groupId);
	}
	
	public static Group getGroup(int groupId){
		
		if(groups.containsKey(groupId))
			return groups.get(groupId);
		else
			throw new ControllerException(enumController.NOT_EXIST_MEMBER_FROM_MAP);
	}
	
	private static Group getGroupFromDB(int gId) throws SQLException{
		
		PreparedStatement ps = conn.prepareStatement("select * from group where groupId = ? ");
		ps.setInt(1, gId);
		ResultSet rs = ps.executeQuery();
		
		if(rs.next()){
			int ownerId = rs.getInt("groupOwner");
			String groupName = rs.getString("groupName");
			Timestamp date = rs.getTimestamp("createdDate");
			
			ps.close();
			rs.close();
			
						
			return new Group.Builder(ownerId, gId, groupName).createdDate(date).build();
					
					
		}
		
	}

	
}

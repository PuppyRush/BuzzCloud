package entity.band;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import dbAccess.DBManager;
import entity.ControllerException;
import entity.EntityException;
import entity.authority.AuthorityController;
import entity.authority.AuthorityManager;
import entity.authority.band.BandAuthority;
import entity.authority.band.enumBandAuthority;
import entity.authority.file.FileAuthority;
import entity.authority.file.enumFileAuthority;
import entity.authority.member.MemberAuthority;
import entity.authority.member.enumMemberAuthority;
import entity.band.Band.AuthoritedMember;
import entity.member.Member;
import entity.member.MemberController;
import entity.member.MemberDB;
import property.ConnectMysql;
import property.tree.Node;
import property.tree.Tree;

public final class BandManager {

	protected Connection conn = ConnectMysql.getConnector();

	private static class Singleton {
		private static final BandManager instance = new BandManager();
	}

	public static BandManager getInstance() {
		return Singleton.instance;
	}

	public ArrayList<Band> getOwneredBands(int memberId) throws EntityException {

		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<Band> ownerdBands = new ArrayList<Band>();
		try {

			ArrayList<Integer> bandsAry = new ArrayList<Integer>();

			ps = conn.prepareStatement("select bandId from band where owner = ?");
			ps.setInt(1, memberId);
			rs = ps.executeQuery();

			while (rs.next())
				bandsAry.add(rs.getInt(1));

			for (int owneredBandId : bandsAry) {
				if (BandController.getInstance().containsEntity(owneredBandId) == false) {
					Band band = getBand(owneredBandId);
					ownerdBands.add(band);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return ownerdBands;

	}

	public ArrayList<Band> getAdministeredBands(int memberId){
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<Band> bandsAry = new ArrayList<Band>();
		
		try {

			ps = conn.prepareStatement("select bandId from band where administrator = ?");
			ps.setInt(1, memberId);
			rs = ps.executeQuery();

			while (rs.next()){
				int bandId = rs.getInt(1);
				Band band = BandController.getInstance().containsEntity(bandId) ? BandController.getInstance().getEntity(bandId) : BandManager.getInstance().getBand(bandId);
				bandsAry.add(band);
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				ps.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return bandsAry;
		
	}
	
	public ArrayList<Band> getAdministeredBandsOfRoot(int memberId) throws EntityException {

		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<Band> rootBands = new ArrayList<Band>();
		try {

			ArrayList<Integer> bandsAry = new ArrayList<Integer>();

			ps = conn.prepareStatement("select bandId from band where administrator = ?");
			ps.setInt(1, memberId);
			rs = ps.executeQuery();

			while (rs.next())
				bandsAry.add(rs.getInt(1));

			ArrayList<Integer> rootBandsId = getRootBandOf(bandsAry);

			for (int rootBandId : rootBandsId) {
				if (BandController.getInstance().containsEntity(rootBandId) == false) {
					Band band = getBand(rootBandId);
					rootBands.add(band);
				} else
					rootBands.add(BandController.getInstance().getEntity(rootBandId));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return rootBands;

	}

	private ArrayList<Integer> getRootBandOf(ArrayList<Integer> bands) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<Integer> rootBands = new ArrayList<Integer>();

		try {

			for (int bandId : bands) {

				ps = conn.prepareStatement("select isRoot from bandAuthority where bandId = ?");
				ps.setInt(1, bandId);
				rs = ps.executeQuery();
				rs.next();
				if (rs.getInt(1) == 1)
					rootBands.add(bandId);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return rootBands;
	}

	public boolean isRootBand(int bandId) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isEqual = false;
		try {

			ps = conn.prepareStatement("select * from bandRelation where fromBand = ?");
			ps.setInt(1, bandId);
			rs = ps.executeQuery();

			while (rs.next()) {
				if (rs.getInt(1) == rs.getInt(2)) {
					isEqual = true;
					break;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return isEqual;
	}

	public boolean isExistSubBand(int bandId) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExist = false;
		try {

			ps = conn.prepareStatement("select toBand from bandRelation where fromBand = ?");
			ps.setInt(1, bandId);
			rs = ps.executeQuery();

			isExist = rs.next();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return isExist;
	}

	public ArrayList<Member> getMembersOf(int bandId){	

		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<Member> memberAry= new ArrayList<Member>();
		try {

			ps = conn.prepareStatement("select memberId from bandMember where bandId = ?");
			ps.setInt(1, bandId);
			rs = ps.executeQuery();

			while (rs.next()){
				int memberId = rs.getInt(1);
				Member member =  MemberController.getInstance().containsEntity(memberId) ?  MemberController.getInstance().getEntity(memberId) : MemberDB.getInstance().getMember(memberId);
				
				memberAry.add(member);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return memberAry;
	}
	
	public int getUpperBand(int bandId) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		int upperBandId = bandId;
		try {

			ps = conn.prepareStatement("select * from bandRelation where toBand = ?");
			ps.setInt(1, bandId);
			rs = ps.executeQuery();
			rs.next();
			upperBandId = rs.getInt("fromBand");

		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			try {
				ps.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return upperBandId;
	}

	public ArrayList<Integer> getSubBand(int bandId) {

		ArrayList<Integer> subBands = new ArrayList<Integer>();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			ps = conn.prepareStatement("select * from bandRelation where fromBand = ?");
			ps.setInt(1, bandId);
			rs = ps.executeQuery();
			while (rs.next()) {
				int toBand = rs.getInt("toBand");
				if (bandId == toBand)
					continue;

				subBands.add(rs.getInt("toBand"));
			}
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			try {
				ps.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return subBands;

	}

	public int getRootBandOf(int bandId) {

		PreparedStatement ps = null;
		ResultSet rs = null;

		int gId = bandId;

		try {

			while (true) {
				ps = conn.prepareStatement("select fromBand from bandRelation where toBand = ?");
				ps.setInt(1, gId);
				rs = ps.executeQuery();
				if (rs.next())
					gId = rs.getInt("fromBand");
				else
					break;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return gId;
	}

	public Tree<Band> getSubBands(int bandId) throws EntityException {

		Tree<Band> tree = null;

		try {
			Band band = BandController.getInstance().containsEntity(bandId)
					? BandController.getInstance().getEntity(bandId) : BandManager.getInstance().getBand(bandId);
			Node<Band> node = BandManager.getInstance().getSubBandsRecursive(band.getBandId());

			tree = new Tree<Band>(band);
			tree.SetPN(node);

		} catch (ControllerException e) {
			e.printStackTrace();
		}

		return tree;
	}

	private Node<Band> getSubBandsRecursive(int upperBandId) throws EntityException {

		Node<Band> upperNode = null;

		try {

			List<Band> siblingBandsId = new ArrayList<Band>();

			PreparedStatement ps = conn.prepareStatement("select toBand from bandRelation where fromBand = ?");
			ps.setInt(1, upperBandId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				int tobandId = rs.getInt("toBand");
				if (upperBandId == tobandId)
					continue;

				Band _band = BandController.getInstance().containsEntity(tobandId)
						? BandController.getInstance().getEntity(tobandId)
						: BandManager.getInstance().getBand(tobandId);
				siblingBandsId.add(_band);

			}
			if (siblingBandsId.size() > 0) {

				Band band = BandController.getInstance().containsEntity(upperBandId)
						? BandController.getInstance().getEntity(upperBandId)
						: BandManager.getInstance().getBand(upperBandId);
				upperNode = new Node<Band>(band);

				ArrayList<Node<Band>> sibligBands = new ArrayList<Node<Band>>();
				Node<Band> childNodeOfUpperNode = new Node<Band>(siblingBandsId.get(0));

				upperNode.SetChild(childNodeOfUpperNode);
				sibligBands.add(childNodeOfUpperNode);

				for (int i = 1; i < siblingBandsId.size(); i++) {
					Node<Band> newNode = new Node<Band>(siblingBandsId.get(i));
					sibligBands.get(i - 1).SetSibling(newNode);
					sibligBands.add(newNode);

				}

				for (Node<Band> node : sibligBands)
					node.SetChild(getSubBandsRecursive(node.GetData().getBandId()));

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return upperNode == null ? null : upperNode.GetChild();

	}

	public void getUpperBandsTree(int bandId) {

	}

	public Band getBand(int bandId) throws EntityException {

		String bandName = "";
		int ownerId = -1;
		Band band = null;

		try {
			PreparedStatement ps = conn.prepareStatement("select * from band where bandId = ? ");
			ps.setInt(1, bandId);
			ResultSet rs = ps.executeQuery();
			
			rs.next();

			ownerId = rs.getInt("owner");
			bandName = rs.getString("name");

			ps.close();
			rs.close();

			int upperBandId = BandManager.getInstance().getUpperBand(bandId);

			BandAuthority bandAuthority = AuthorityManager.getInstance().getBandAuthority(bandId);
			MemberAuthority mAuth = AuthorityManager.getInstance().getMemberAuthority(ownerId, bandId);
			FileAuthority fAuth = AuthorityManager.getInstance().getFileAuthoirty(ownerId, bandId);

			Member member = MemberController.getInstance().containsEntity(ownerId) ? MemberController.getInstance().getEntity(ownerId) : MemberDB.getInstance().getMember(ownerId);
			HashMap<Integer, AuthoritedMember> members = new HashMap<Integer, AuthoritedMember>();

			members.put(member.getId(), new AuthoritedMember(member, mAuth, fAuth));

			ps.close();
			rs.close();
			
			ps = conn.prepareStatement("select usingCapacity, maxCapacity, driverPath from bandDetail where bandId = ? ");
			ps.setInt(1, bandId);
			rs = ps.executeQuery();
			rs.next();
			
			int maxCapacity = rs.getInt("maxCapacity");
			int usingCapacity = rs.getInt("usingCapacity");
			String driverPath = rs.getString("driverPath");
			
			band = new Band.Builder(bandId, ownerId, bandName).bandAuhority(bandAuthority).members(members).upperBandId(upperBandId).maxCapacity(maxCapacity)
					.usingCapacity(usingCapacity).driverPath(driverPath).build();

			if (BandController.getInstance().containsEntity(bandId) == false)
				BandController.getInstance().addEntity(bandId, band);

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return band;

	}

	
	public boolean updateBand(JSONObject obj){
		
		HashMap<String, Object> where = new HashMap<String, Object>();
		HashMap<String, Object> set = new HashMap<String, Object>();
	
		try{
		
			String bandName = (String)obj.get("bandName");
			int upperBandId = ((Long)obj.get("upperBandId")).intValue();
			int ownerId = MemberDB.getInstance().getIdOfNickname((String)obj.get("bandOwner"));
			int bandId = BandDB.getInstance().getIdOfName(bandName, upperBandId);
			if(upperBandId == -1)
				upperBandId = bandId;
		
			
			if(obj.get("bandName")!=null)
				set.put("name", (String)obj.get("bandName"));		
			
			if(obj.get("bandOwner")!=null)
				set.put("owner", ownerId);
		
			where.put("bandId", bandId);			
			DBManager.getInstance().updateColumn("band", set, where);
						
			where.clear();
			set.clear();
			
			
			if(obj.get("bandCapacity")!=null)
				set.put("maxCapacity", Integer.valueOf((String)obj.get("bandCapacity")));
			
			if(obj.get("bandContain")!=null)
				set.put("contents", (String)obj.get("bandContain"));
			
			where.put("bandId", bandId);
			DBManager.getInstance().updateColumn("bandDetail", set, where);
			
			where.clear();
			set.clear();
			

			JSONArray memberAry = (JSONArray)obj.get("members");
			ArrayList<Integer> memberIds = new ArrayList<Integer>();
			for(int i=0 ; i < memberAry.size() ; i++){
				Long id = (Long)memberAry.get(i);
				memberIds.add( id.intValue() );
			}
			addNewBandMembers(bandId, memberIds);
			
			
			JSONArray bandAuths = (JSONArray)obj.get("bandAuthority");
			JSONArray fileAuths = (JSONArray)obj.get("fileAuthority");
						
			for(int i=0 ; i < bandAuths.size(); i++){
				enumBandAuthority auth = enumBandAuthority.valueOf( (String)bandAuths.get(i));
				set.put(auth.toString(), 1);
			}
			where.put("bandId", bandId);
			DBManager.getInstance().updateColumn("bandAuthority", set, where);
			
			where.clear();
			set.clear();
			
			
			EnumMap<enumFileAuthority ,Boolean> fileAuthMap = new EnumMap<enumFileAuthority ,Boolean>(enumFileAuthority.class);
			for(int i=0 ; i < fileAuths.size(); i++){
				enumFileAuthority auth = enumFileAuthority.valueOf( (String)fileAuths.get(i));
				set.put(auth.toString(), 1);
				fileAuthMap.put(auth, true);
			}
			where.put("bandId", bandId);
			DBManager.getInstance().updateColumn("fileAuthority", set, where);
			
			where.clear();
			set.clear();
		
			
			Band band;
			if( BandController.getInstance().containsEntity(bandId))
				band =  BandController.getInstance().getEntity(bandId);
			else{
				band = BandManager.getInstance().getBand(bandId);
				BandController.getInstance().addEntity(bandId, band);
			}
			
			BandAuthority bandAuthority = AuthorityManager.getInstance().getBandAuthority(bandId);
			if(AuthorityController.getInstance().containsEntity(bandAuthority.getAuthorityId()) == false)
				AuthorityController.getInstance().addEntity(bandAuthority.getAuthorityId(), bandAuthority);
			band.setBandAuthority(bandAuthority);
			
			for(Integer memberId :  band.getMembers().keySet()){				
				AuthoritedMember am = band.getMembers().get(memberId);
				am.getFileAuthority().setAuthorityType(fileAuthMap);		
			}
			
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return false;
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
	

	public boolean makeBand(JSONObject obj){
		
	
		try{
			
			conn.setAutoCommit(false);
		
			String groupName = (String)obj.get("bandName");
			String groupOwner = (String)obj.get("bandOwner");
			String administrator = (String)obj.get("administrator");
		
			int groupCapacity = 	 ((Long)obj.get("bandCapacity")).intValue();
			String groupContain = (String)obj.get("bandContain");
			int upperGroupId = ((Long)obj.get("upperBand")).intValue();
			
			int ownerId = MemberDB.getInstance().getIdOfNickname(groupOwner);
			int adminId = MemberDB.getInstance().getIdOfNickname(administrator);
			int bandId = BandDB.getInstance().makeBand(groupName, ownerId, adminId);
			
			if(upperGroupId == -1)
				upperGroupId = bandId;

			
			JSONArray memberJsonAry = (JSONArray)obj.get("members");
			ArrayList<Integer> memberIds = new ArrayList<Integer>();
			for(int i=0 ; i < memberJsonAry.size() ; i++){
				Long id = (Long)memberJsonAry.get(i);
				memberIds.add( id.intValue() );
			}		
			
			
			String driverPath =  BandDB.getInstance().makeBandDetail(bandId, groupCapacity, groupContain);	
			BandDB.getInstance().makeBandMember(bandId, memberIds);
			BandDB.getInstance().makeBandRelation(upperGroupId, bandId);
			
			JSONArray bandAuths = (JSONArray)obj.get("bandAuthority");
			JSONArray fileAuths = (JSONArray)obj.get("fileAuthority");
			
			EnumMap<enumBandAuthority ,Boolean> bandAuthMap = new EnumMap<enumBandAuthority ,Boolean>(enumBandAuthority.class);
						
			for(int i=0 ; i < bandAuths.size(); i++){
				
				if(enumBandAuthority.valueOf((String)bandAuths.get(i)) != null)
					bandAuthMap.put(enumBandAuthority.valueOf((String)bandAuths.get(i)), true);
			}
	
			EnumMap<enumFileAuthority ,Boolean> fileAuthMap = new EnumMap<enumFileAuthority ,Boolean>(enumFileAuthority.class);
			for(int i=0 ; i < fileAuths.size(); i++){
				if(enumFileAuthority.valueOf((String)fileAuths.get(i)) != null)
					fileAuthMap.put(enumFileAuthority.valueOf((String)fileAuths.get(i)), true);
			}
			
			HashMap<Integer, AuthoritedMember> authoritedMap = new HashMap<Integer, AuthoritedMember>(); 
			for(int i=0 ; i < memberIds.size() ; i++){
				int memberId = memberIds.get(i);
				
				Member member = MemberController.getInstance().containsEntity(memberId) ? MemberController.getInstance().getEntity(memberId) : MemberDB.getInstance().getMember(memberId);
					
				FileAuthority fileAuth = AuthorityManager.getInstance().makeFileAuthoirty(member.getId(), bandId, fileAuthMap);
	
				MemberAuthority memberAuth =null;
				if(memberId == adminId)
					memberAuth = AuthorityManager.getInstance().makeMemberAuthority(member.getId(), bandId, enumMemberAuthority.ADMIN);
				else if(memberId == ownerId)
					memberAuth = AuthorityManager.getInstance().makeMemberAuthority(member.getId(), bandId, enumMemberAuthority.OWNER);
				else
					memberAuth = AuthorityManager.getInstance().makeMemberAuthority(member.getId(), bandId, enumMemberAuthority.MEMBER);
				
				AuthoritedMember am = new Band.AuthoritedMember(member,memberAuth, fileAuth);
				authoritedMap.put(member.getId(), am);
					
				AuthorityController.getInstance().addEntity(fileAuth.getAuthorityId(), fileAuth);
				AuthorityController.getInstance().addEntity(memberAuth.getAuthorityId(), memberAuth);		
			}
			
			BandAuthority bandAuth = AuthorityManager.getInstance().makeBandAuthority(bandId, bandAuthMap);
			AuthorityController.getInstance().addEntity(bandAuth.getAuthorityId(), bandAuth);
			
			Band.Builder bld = new Band.Builder();
			bld.maxCapacity( groupCapacity );
			bld.upperBandId(upperGroupId);
			bld.usingCapacity(0);
			bld.members(authoritedMap);
			bld.bandAuhority(bandAuth);
			bld.driverPath(driverPath);
			
			bld.subBands(BandManager.getInstance().getSubBands(bandId));
			Band band = bld.build();
			
			if(BandController.getInstance().containsEntity(bandId) == false)
				BandController.getInstance().addEntity(bandId, band);
		
			conn.commit();
			
		}catch(ControllerException e){
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return false;
		} catch (EntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return false;
		} 
		
		return true;
	}
	
	public void addNewBandMembers(int bandId, ArrayList<Integer> members){
		
		HashMap<String, Object> where = new HashMap<String, Object>();
		ArrayList<Integer> exMembers = new ArrayList<Integer>();
		
		where.put("bandId", bandId);
		ArrayList<HashMap<String,Object>> memberAry = DBManager.getInstance().getColumnsOfAll("bandMember", where);
		
		for(HashMap<String,Object> member : memberAry)
			exMembers.add( (Integer)member.get("memberId"));
		
		members = (ArrayList<Integer>) CollectionUtils.subtract(members,exMembers);
		
		for(Integer newMemberId : members){
			
			PreparedStatement ps;
			try {
				
				ps = conn.prepareStatement("insert into bandMember (bandId, memberId) values (?,?)");
				ps.setInt(1, bandId);
				ps.setInt(2, newMemberId);
				ps.executeUpdate();
				
				ps.close();				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
						
		}
	}
}

package com.puppyrush.buzzcloud.entity.band;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javadude.annotation.Bean;
import com.puppyrush.buzzcloud.controller.form.BandForm;
import com.puppyrush.buzzcloud.dbAccess.DBManager;
import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.authority.AuthorityController;
import com.puppyrush.buzzcloud.entity.authority.AuthorityManager;
import com.puppyrush.buzzcloud.entity.authority.band.BandAuthority;
import com.puppyrush.buzzcloud.entity.authority.band.enumBandAuthority;
import com.puppyrush.buzzcloud.entity.authority.file.FileAuthority;
import com.puppyrush.buzzcloud.entity.authority.file.enumFileAuthority;
import com.puppyrush.buzzcloud.entity.authority.member.MemberAuthority;
import com.puppyrush.buzzcloud.entity.authority.member.enumMemberAuthority;
import com.puppyrush.buzzcloud.entity.band.Band.AuthoritedMember;
import com.puppyrush.buzzcloud.entity.member.Member;
import com.puppyrush.buzzcloud.entity.member.MemberController;
import com.puppyrush.buzzcloud.entity.member.MemberDB;
import com.puppyrush.buzzcloud.property.ConnectMysql;
import com.puppyrush.buzzcloud.property.enums.enumSystem;
import com.puppyrush.buzzcloud.property.tree.Node;
import com.puppyrush.buzzcloud.property.tree.Tree;

@Service("bandManager")
public final class BandManager {

	protected Connection conn = ConnectMysql.getConnector();

	@Autowired
	private BandController bCtl;
	
	@Autowired
	private BandDB bDB;
	
	@Autowired
	private MemberController mCtl;
	
	@Autowired
	private MemberDB mDB;
	
	@Autowired
	private AuthorityManager authMng;
	
	@Autowired
	private AuthorityController authCtl;
	
	@Autowired
	private DBManager dbMng;
	
	
	public ArrayList<Band> getOwneredBands(int memberId) throws EntityException, ControllerException {

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
				if (bCtl.containsEntity(owneredBandId) == false) {
					Band band = getBand(owneredBandId);
					ownerdBands.add(band);
				}
				else
					ownerdBands.add(bCtl.getEntity(owneredBandId));
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
				Band band = bCtl.containsEntity(bandId) ? bCtl.getEntity(bandId) : getBand(bandId);
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
	
	public ArrayList<Band> getRootOfOwneredBands(int memberId) throws EntityException {

		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<Band> rootBands = new ArrayList<Band>();
		try {

			ArrayList<Integer> bandsAry = new ArrayList<Integer>();

			ps = conn.prepareStatement("select bandId from band where owner = ?");
			ps.setInt(1, memberId);
			rs = ps.executeQuery();

			while (rs.next())
				bandsAry.add(rs.getInt(1));

			ArrayList<Integer> rootBandsId = getRootBandOf(bandsAry);

			for (int rootBandId : rootBandsId) {
				if (bCtl.containsEntity(rootBandId) == false) {
					Band band = getBand(rootBandId);
					rootBands.add(band);
				} else
					rootBands.add(bCtl.getEntity(rootBandId));
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
				Member member =  mCtl.containsEntity(memberId) ?  mCtl.getEntity(memberId) : mDB.getMember(memberId);
				
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
				if (rs.next()){
					int ex_id = gId;
					gId = rs.getInt("fromBand");
					if(gId==ex_id)
						break;
				}
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
			Band band = bCtl.containsEntity(bandId)
					? bCtl.getEntity(bandId) : getBand(bandId);
			Node<Band> node = getSubBandsRecursive(band.getBandId());

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

				Band _band = bCtl.containsEntity(tobandId)
						? bCtl.getEntity(tobandId)
						: getBand(tobandId);
				siblingBandsId.add(_band);

			}
			if (siblingBandsId.size() > 0) {

				Band band = bCtl.containsEntity(upperBandId)
						? bCtl.getEntity(upperBandId)
						: getBand(upperBandId);
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

			int adminId = rs.getInt("administrator");
			ownerId = rs.getInt("owner");
			bandName = rs.getString("name");
			
					
			ps.close();
			rs.close();

			int upperBandId = getUpperBand(bandId);

			BandAuthority bandAuthority = authMng.getBandAuthority(bandId);
			MemberAuthority mAuth = authMng.getMemberAuthority(ownerId, bandId);
			FileAuthority fAuth = authMng.getFileAuthoirty(ownerId, bandId);

			Member member = mCtl.containsEntity(ownerId) ? mCtl.getEntity(ownerId) : mDB.getMember(ownerId);
			HashMap<Integer, AuthoritedMember> members = new HashMap<Integer, AuthoritedMember>();

			members.put(member.getId(), new AuthoritedMember(member, mAuth, fAuth));

			ps.close();
			rs.close();
			
			ps = conn.prepareStatement("select * from bandDetail where bandId = ? ");
			ps.setInt(1, bandId);
			rs = ps.executeQuery();
			rs.next();
			
			int maxCapacity = rs.getInt("maxCapacity");
			int usingCapacity = rs.getInt("usingCapacity");
			String driverPath = rs.getString("driverPath");
			String driverNickname = rs.getString("driverNickname");
			String contents = rs.getString("contents");
			
			band = new Band.Builder(bandId, ownerId, bandName).bandAuhority(bandAuthority).members(members).upperBandId(upperBandId).maxCapacity(maxCapacity)
					.usingCapacity(usingCapacity).driverPath(driverPath).adminId(adminId).driverNickname(driverNickname).contents(contents).build();

			if (bCtl.containsEntity(bandId) == false)
				bCtl.addEntity(bandId, band);

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return band;

	}

	public boolean makeBand(BandForm form){
		
	
		try{
			
			conn.setAutoCommit(false);
			
			int ownerId = mDB.getIdOfNickname(form.getBandOwner());
			int adminId = mDB.getIdOfNickname(form.getAdministrator());
			int bandId = bDB.makeBand(form.getBandName(), ownerId, adminId);
			
			if(form.getUpperBand() == -1)
				form.setUpperBand(bandId);
			String driverPath = makeDirectoryAndGetPath(form.getUpperBand(),bandId);
			
			bDB.makeBandDetail(bandId, form.getBandCapacity(), driverPath, form.getBandContain());	
			bDB.makeBandMember(bandId, form.getMembers());
			bDB.makeBandRelation(form.getUpperBand(), bandId);
			
			EnumMap<enumBandAuthority ,Boolean> bandAuthMap = new EnumMap<enumBandAuthority ,Boolean>(enumBandAuthority.class);
						
			for(int i=0 ; i < form.getBandAuthority().size(); i++){
				
				if(enumBandAuthority.valueOf(form.getBandAuthority().get(i)) != null)
					bandAuthMap.put(enumBandAuthority.valueOf(form.getBandAuthority().get(i)), true);
			}
	
			EnumMap<enumFileAuthority ,Boolean> fileAuthMap = new EnumMap<enumFileAuthority ,Boolean>(enumFileAuthority.class);
			for(int i=0 ; i < form.getFileAuthority().size(); i++){
				if(enumFileAuthority.valueOf(form.getFileAuthority().get(i)) != null)
					fileAuthMap.put(enumFileAuthority.valueOf(form.getFileAuthority().get(i)), true);
			}
			
			HashMap<Integer, AuthoritedMember> authoritedMap = new HashMap<Integer, AuthoritedMember>(); 
			for(int i=0 ; i < form.getMembers().size() ; i++){
				int memberId = form.getMembers().get(i);
				
				Member member = mCtl.containsEntity(memberId) ? mCtl.getEntity(memberId) : mDB.getMember(memberId);
					
				FileAuthority fileAuth = authMng.makeFileAuthoirty(member.getId(), bandId, fileAuthMap);
	
				MemberAuthority memberAuth =null;
				if(memberId == adminId)
					memberAuth = authMng.makeMemberAuthority(member.getId(), bandId, enumMemberAuthority.ADMIN);
				else if(memberId == ownerId)
					memberAuth = authMng.makeMemberAuthority(member.getId(), bandId, enumMemberAuthority.OWNER);
				else
					memberAuth = authMng.makeMemberAuthority(member.getId(), bandId, enumMemberAuthority.MEMBER);
				
				AuthoritedMember am = new Band.AuthoritedMember(member,memberAuth, fileAuth);
				authoritedMap.put(member.getId(), am);
					
				authCtl.addEntity(fileAuth.getAuthorityId(), fileAuth);
				authCtl.addEntity(memberAuth.getAuthorityId(), memberAuth);		
			}
			
			BandAuthority bandAuth = authMng.makeBandAuthority(bandId, bandAuthMap);
			authCtl.addEntity(bandAuth.getAuthorityId(), bandAuth);
			
			Band.Builder bld = new Band.Builder();
			bld.maxCapacity( form.getBandCapacity() );
			bld.contents(form.getBandContain());
			bld.upperBandId(form.getUpperBand());
			bld.usingCapacity(0);
			bld.adminId(adminId);
			bld.members(authoritedMap);
			bld.bandAuhority(bandAuth);
			bld.driverPath(driverPath);
			bld.driverNickname(driverPath);
			
			bld.subBands(getSubBands(bandId));
			Band band = bld.build();
			
			if(bCtl.containsEntity(bandId) == false)
				bCtl.addEntity(bandId, band);
		
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

	public String makeDirectoryAndGetPath(int upperBand, int newBandId) throws SQLException, IOException{
		
		String driverPath = "";
		
		if(upperBand==newBandId){
			driverPath = enumSystem.DEFAULT_DIRVER_ABS_PATH.toString() + UUID.randomUUID().toString();
			File file = new File(driverPath);
			file.mkdirs();
		}
		else{
			PreparedStatement ps =  conn.prepareStatement("select driverPath from bandDetail where bandId = ?");
			ps.setInt(1, upperBand);
			ResultSet rs =  ps.executeQuery();
			
			rs.next();
			String upperPath = rs.getString(1);
			
			File file = new File(upperPath);
			if(!file.exists())
				throw new IOException("failed make directory");
			String newDir = UUID.randomUUID().toString();
			
			driverPath = upperPath+"/"+newDir;
			
			file = new File(driverPath);
			file.mkdirs();
		}
		
		return driverPath;
	}

}

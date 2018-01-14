package com.puppyrush.buzzcloud.entity.band;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.puppyrush.buzzcloud.controller.form.BandForm;
import com.puppyrush.buzzcloud.dbAccess.DBException;
import com.puppyrush.buzzcloud.dbAccess.DBManager;
import com.puppyrush.buzzcloud.dbAccess.enumDBError;
import com.puppyrush.buzzcloud.dbAccess.DBManager.ColumnHelper;
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
import com.puppyrush.buzzcloud.entity.band.enums.enumBandStandard;
import com.puppyrush.buzzcloud.entity.member.Member;
import com.puppyrush.buzzcloud.entity.member.MemberController;
import com.puppyrush.buzzcloud.entity.member.MemberDB;
import com.puppyrush.buzzcloud.entity.message.instanceMessage.enumInstanceMessage;
import com.puppyrush.buzzcloud.page.enums.enumPage;
import com.puppyrush.buzzcloud.property.ConnectMysql;
import com.puppyrush.buzzcloud.property.enumSystem;
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
	
	
	public ArrayList<Band> getOwneredBands(int memberId) throws EntityException, ControllerException, SQLException {

		ArrayList<Band> ownerdBands = new ArrayList<Band>();
		ArrayList<Integer> bandsAry = new ArrayList<Integer>();

		List<String> sel = new ArrayList<String>();
		Map<String, Object> where = new HashMap<String, Object>();
		sel.add("bandId");
		where.put("owner", memberId);

		ColumnHelper ch = dbMng.getColumnsOfPart("band", sel, where);
		
		if(ch.next()){
			bandsAry.add(ch.getInteger(sel.get(0)));
		}

		for (int owneredBandId : bandsAry) {
			if (bCtl.containsEntity(owneredBandId) == false) {
				Band band = getBand(owneredBandId);
				ownerdBands.add(band);
			}
			else
				ownerdBands.add(bCtl.getEntity(owneredBandId));
		}

	
		return ownerdBands;

	}

	public ArrayList<Band> getAdministeredBands(int memberId) throws SQLException, ControllerException, EntityException{
		
		ArrayList<Band> bandsAry = new ArrayList<Band>();
		
		List<String> sel = new ArrayList<String>();
		Map<String, Object> where = new HashMap<String, Object>();
		sel.add("bandId");
		where.put("administrator", memberId);

		ColumnHelper ch = dbMng.getColumnsOfPart("band", sel, where);
		
		while (ch.next()){
			int bandId = ch.getInteger(sel.get(0));
			Band band = bCtl.containsEntity(bandId) ? bCtl.getEntity(bandId) : getBand(bandId);
			bandsAry.add(band);
		}
	
		return bandsAry;
	}
	
	public List<Integer> getRootOfOwneredBandIds(int memberId) throws EntityException, ControllerException, SQLException {

		List<Integer> bandsAry = new ArrayList<Integer>();

		List<String> sel = new ArrayList<String>();
		Map<String, Object> where = new HashMap<String, Object>();
		sel.add("bandId");
		where.put("owner", memberId);
		
		ColumnHelper ch = dbMng.getColumnsOfPart("band", sel, where);
		if(ch.isEmpty())
			return bandsAry;
		
		for(int i=0; i < ch.columnSize() ; i++)
			bandsAry.add(ch.getInteger(i, "bandId"));
	
		return getRootBandOf(bandsAry);

	}

	
	public List<Band> getRootOfOwneredBands(int memberId) throws EntityException, ControllerException, SQLException {

	
		List<Band> rootBands = new ArrayList<Band>();
		

		List<Integer> bandsAry = new ArrayList<Integer>();

		List<String> sel = new ArrayList<String>();
		Map<String, Object> where = new HashMap<String, Object>();
		sel.add("bandId");
		where.put("owner", memberId);
		
		ColumnHelper ch = dbMng.getColumnsOfPart("band", sel, where);
		if(ch.isEmpty())
			return rootBands;
		
		for(int i=0; i < ch.columnSize() ; i++)
			bandsAry.add(ch.getInteger(i, "bandId"));
	
		List<Integer> rootBandsId = getRootBandOf(bandsAry);

		for (int rootBandId : rootBandsId) {
			if (bCtl.containsEntity(rootBandId) == false) {
				Band band = getBand(rootBandId);
				rootBands.add(band);
			} else
				rootBands.add(bCtl.getEntity(rootBandId));
		}

		return rootBands;

	}

	private List<Integer> getRootBandOf(List<Integer> bandsAry) throws SQLException {

		ArrayList<Integer> rootBands = new ArrayList<Integer>();
		List<String> sel = new ArrayList<String>();
		Map<String, Object> where = new HashMap<String, Object>();
		sel.add("Root");

		for (int bandId : bandsAry) {

			where.put("bandId", bandId);
			ColumnHelper ch = dbMng.getColumnsOfPart("bandAuhority", sel, where);
			if(ch.next())
				if(ch.getInteger(sel.get(0))==1)
					rootBands.add(bandId);
			
			where.clear();
		}

		
		return rootBands;
	}

	public boolean isRootBand(int bandId) throws SQLException {

		boolean isEqual = false;
		List<String> sel = new ArrayList<String>();
		Map<String, Object> where = new HashMap<String, Object>();
		where.put("fromBand", bandId);
		sel.add("Root");
		
		ColumnHelper ch = dbMng.getColumnsOfPart("bandAuhority", sel,where);

		if(ch.next()) {
			if(ch.getInteger("Root")==1)
				return true;
		}

		return isEqual;
	}

	public boolean isExistSubBand(int bandId) throws SQLException {

		boolean isExist = false;
		List<String> sel = new ArrayList<String>();
		Map<String, Object> where = new HashMap<String, Object>();
		sel.add("toBand");
		where.put("fromBand", bandId);

		ColumnHelper ch = dbMng.getColumnsOfPart("bandRelation", sel, where);
		isExist = ch.next();		

		return isExist;
	}

	public ArrayList<Member> getMembersOf(int bandId) throws SQLException, ControllerException, EntityException{	

		ArrayList<Member> memberAry= new ArrayList<Member>();

		List<String> sel = new ArrayList<String>();;
		Map<String, Object> where = new HashMap<String, Object>();
		sel.add("memberId");
		where.put("bandId", bandId);

		ColumnHelper ch = dbMng.getColumnsOfPart("bandMember", sel, where);
		
		while (ch.next()){
			int memberId = ch.getInteger(sel.get(0));
			Member member =  mCtl.containsEntity(memberId) ?  mCtl.getEntity(memberId) : mDB.getMember(memberId);
			
			memberAry.add(member);
		}
		return memberAry;
	}
	
	public int getUpperBand(int bandId) throws SQLException {

		int upperBandId = enumBandStandard.NOT_EXIST_BAND.toInt();
		
		List<String> sel = new ArrayList<String>();
		Map<String, Object> where = new HashMap<String, Object>();
		
		sel.add("fromBand");
		where.put("toBand", bandId);
		ColumnHelper ch = dbMng.getColumnsOfPart("bandRelation", sel, where);
		
		upperBandId = ch.getInteger(0, "fromBand");
		return upperBandId;
	}

	public ArrayList<Integer> getSubBand(int bandId) throws SQLException {

		ArrayList<Integer> subBands = new ArrayList<Integer>();
		Map<String, Object> where = new HashMap<String, Object>();
		List<String> sel = new ArrayList<String>();
		where.put("fromBand", bandId);

		ColumnHelper ch = dbMng.getColumnsOfPart("bandRelation", sel, where);
		
		while (ch.next()) {
			int toBand = ch.getInteger("toBand");
			if (bandId == toBand)
				continue;

			subBands.add(ch.getInteger("toBand"));
		}

		return subBands;
	}

	public int getRootBandOf(int bandId) throws SQLException {

		List<String> sel = new ArrayList<String>();
		sel.add("fromBand");
		int gId = bandId;
		while (true) {
			
			Map<String, Object> where = new HashMap<String, Object>();	
			where.put("toBand", gId);
			
			ColumnHelper ch = dbMng.getColumnsOfPart("bandRelation", sel, where);
			if (ch.next()){
				int ex_id = gId;
				gId = ch.getInteger("fromBand");
				if(gId==ex_id)
					break;
			}
			else
				break;
		}

		return gId;
	}

	public Tree<Band> getSubBands(int bandId) throws EntityException, SQLException, ControllerException {

		Tree<Band> tree = null;

		Band band = bCtl.containsEntity(bandId)
				? bCtl.getEntity(bandId) : getBand(bandId);
		Node<Band> node = getSubBandsRecursive(band.getBandId());

		tree = new Tree<Band>(band);
		tree.SetPN(node);
	
		return tree;
	}

	private Node<Band> getSubBandsRecursive(int upperBandId) throws EntityException, SQLException, ControllerException {

		Node<Band> upperNode = null;
		List<Band> siblingBandsId = new ArrayList<Band>();

		List<String> sel = new ArrayList<String>();
		Map<String, Object> where = new HashMap<String, Object>();
		
		sel.add("toBand");
		where.put("fromBand", upperBandId);

		ColumnHelper ch = dbMng.getColumnsOfPart("bandRelation", sel, where);
		
		while (ch.next()) {

			int tobandId = ch.getInteger("toBand");
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
		return upperNode == null ? null : upperNode.GetChild();
	}

	public void getUpperBandsTree(int bandId) {

	}

	public Band getBand(int bandId) throws EntityException, SQLException, ControllerException {

		String bandName = "";
		int ownerId = -1;
		Band band = null;

		List<String> sel = new ArrayList<String>();
		Map<String, Object> where = new HashMap<String, Object>();
		where.put("bandId", bandId);
		
		ColumnHelper ch = dbMng.getColumnsOfAll("band", where);
		if(ch.columnSize()!=1)
			throw new SQLException();
				
		int adminId = ch.getInteger(0, "administrator");
		ownerId = ch.getInteger(0, "owner");
		bandName = ch.getString(0,"name");
		
		int upperBandId = getUpperBand(bandId);

		BandAuthority bandAuthority = authMng.getBandAuthority(bandId);
		MemberAuthority mAuth = authMng.getMemberAuthority(ownerId, bandId);
		FileAuthority fAuth = authMng.getFileAuthoirty(ownerId, bandId);

		Member member = mCtl.containsEntity(ownerId) ? mCtl.getEntity(ownerId) : mDB.getMember(ownerId);
		HashMap<Integer, AuthoritedMember> members = new HashMap<Integer, AuthoritedMember>();

		members.put(member.getId(), new AuthoritedMember(member, mAuth, fAuth));

		ch = dbMng.getColumnsOfAll("bandDetail", where);
		if(ch.columnSize()!=1)
			throw new SQLException();
				
		int maxCapacity = ch.getInteger(0,"maxCapacity");
		int usingCapacity = ch.getInteger(0,"usingCapacity");
		String driverPath = ch.getString(0,"driverPath");
		String driverNickname = ch.getString(0,"driverNickname");
		String contents = ch.getString(0,"contents");
		
		band = new Band.Builder(bandId, ownerId, bandName).bandAuhority(bandAuthority).members(members).upperBandId(upperBandId).maxCapacity(maxCapacity)
				.usingCapacity(usingCapacity).driverPath(driverPath).adminId(adminId).driverNickname(driverNickname).contents(contents).build();

		if (bCtl.containsEntity(bandId) == false)
			bCtl.addEntity(bandId, band);

		return band;

	}

	public boolean makeBand(BandForm form) throws SQLException, IOException, ControllerException, EntityException, DBException{
		
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
		
		Map<enumBandAuthority ,Boolean> bandAuthMap = enumBandAuthority.toEnumMap(form.getBandAuthority());	
		Map<enumFileAuthority ,Boolean> fileAuthMap = enumFileAuthority.toEnumMap(form.getFileAuthority());
		
		
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
		
		return true;
	}

	public String makeDirectoryAndGetPath(int upperBand, int newBandId) throws SQLException, IOException, DBException{
		
		String driverPath = "";
		
		if(upperBand==newBandId || upperBand == -1){
			driverPath = enumSystem.DEFAULT_DIRVER_ABS_PATH.toString() + UUID.randomUUID().toString();
			File file = new File(driverPath);
			file.mkdirs();
		}
		else{
			
			List<String> sel = new ArrayList<String>();
			Map<String, Object> where = new HashMap<String, Object>();
			sel.add("driverPath");
			where.put("bandId", upperBand);

			ColumnHelper ch = dbMng.getColumnsOfPart("bandDetail", sel, where);
			
			if(ch.isEmpty())
				throw (new DBException.Builder(enumPage.GROUP_MANAGER))
				.instanceMessageType(enumInstanceMessage.ERROR)
				.errorCode(enumDBError.NOT_CORRESPOND_WHERE).build();
			
			ch.next();
			String upperPath = ch.getString(sel.get(0));
			
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

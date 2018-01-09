package com.puppyrush.buzzcloud.service.entity.band;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.puppyrush.buzzcloud.controller.form.BandForm;
import com.puppyrush.buzzcloud.dbAccess.DBManager;
import com.puppyrush.buzzcloud.dbAccess.DBManager.ColumnHelper;
import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.authority.AuthorityController;
import com.puppyrush.buzzcloud.entity.authority.AuthorityDB;
import com.puppyrush.buzzcloud.entity.authority.AuthorityManager;
import com.puppyrush.buzzcloud.entity.authority.band.BandAuthority;
import com.puppyrush.buzzcloud.entity.authority.band.enumBandAuthority;
import com.puppyrush.buzzcloud.entity.authority.file.FileAuthority;
import com.puppyrush.buzzcloud.entity.authority.file.enumFileAuthority;
import com.puppyrush.buzzcloud.entity.authority.member.MemberAuthority;
import com.puppyrush.buzzcloud.entity.authority.member.enumMemberAuthority;
import com.puppyrush.buzzcloud.entity.band.Band;
import com.puppyrush.buzzcloud.entity.band.BandManager;
import com.puppyrush.buzzcloud.entity.member.Member;
import com.puppyrush.buzzcloud.entity.member.MemberController;
import com.puppyrush.buzzcloud.entity.member.MemberDB;
import com.puppyrush.buzzcloud.entity.member.MemberManager;
import com.puppyrush.buzzcloud.entity.message.instanceMessage.enumInstanceMessage;
import com.puppyrush.buzzcloud.page.enums.enumPage;
import com.puppyrush.buzzcloud.property.enumSystem;
import com.puppyrush.buzzcloud.entity.band.Band.AuthoritedMember;
import com.puppyrush.buzzcloud.entity.band.enums.enumBandStandard;
import com.puppyrush.buzzcloud.entity.band.enums.enumBandState;
import com.puppyrush.buzzcloud.entity.band.BandController;
import com.puppyrush.buzzcloud.entity.band.BandDB;

@Service("updatingMyBand")
public class UpdatingMyBand{

	private MemberManager mMng;
	
	@Autowired(required=false)
	private MemberController mCtl;
	
	@Autowired(required=false)
	private MemberDB mDB;

	@Autowired(required=false)
	private BandManager bMng;
	
	@Autowired(required=false)
	private BandDB bDB;

	@Autowired(required=false)
	private DBManager dbMng;

	@Autowired(required=false)
	private BandController bCtl;

	@Autowired(required=false)
	private AuthorityController authCtl;
	
	@Autowired(required=false)
	private AuthorityManager authMng;
	
	private int ownerId;
	private int bandId; 
	private BandForm bandForm;
	
	private ColumnHelper exBand;
	private ColumnHelper exBandDetail;
	private ColumnHelper exMembers;

		
	public Map<String, Object> execute(int bandId, BandForm form) throws SQLException, ControllerException, EntityException {
		
		this.bandForm = form;
		this.bandId = bandId;
		this.ownerId = mDB.getIdOfNickname(form.getBandOwner());
		
		Map<String, Object> params = new HashMap<String, Object>();
	
		Map<String, Object> where = new HashMap<String, Object>();
		where.put("bandId" ,bandId);
		
		exBand = dbMng.getColumnsOfAll("band", where);
		exBandDetail = dbMng.getColumnsOfAll("bandDetail", where);
		exMembers = dbMng.getColumnsOfAll("bandMember", where);

		if(exBand.isEmpty() || exBandDetail.isEmpty() || exMembers.isEmpty() )
			throw (new EntityException.Builder(enumPage.GROUP_MANAGER))
			.instanceMessage(enumInstanceMessage.ERROR)
			.errorString("그룹 정보를 찾지 못하였습니다. 관리자에게 문의하세요.")
			.errorCode(enumBandState.NOT_EXIST_BAND).build();
		
		
		checkForm(params);
		updateBandDetail();
		updateMembers();
		updateFileAuthority();
		updatedBandAuthority();
		updatedUpperBand();
		
		return params;
		
	}


	
	void checkForm(Map<String, Object> params) throws ControllerException, SQLException{
		
		boolean isRoot = bMng.isRootBand(bandId); 
		
		if(isRoot){
			if(bandForm.getBandAuthority().contains(enumBandAuthority.ROOT))
				throw new IllegalArgumentException("최상위 그룹은 그룹권한으로 ROOT를 항상 포함시켜야 합니다.");
				
			int realOwner = bCtl.containsEntity(bandId) ? bCtl.getEntity(bandId).getOwnerId() : bDB.getOwnerMemberOf(bandId);
			int realAdmin = bCtl.containsEntity(bandId) ? bCtl.getEntity(bandId).getAdminId() : bDB.getAdminMemberOf(bandId);
			
			boolean existOwner = false;
			boolean existAdmin = false;
			for(int i=0 ; i < bandForm.getMembers().size() && ( !existOwner && !existAdmin) ; i++){
				if(!existOwner && bandForm.getMembers().get(i)==realOwner){
					existOwner = true;
				}
				if(!existAdmin && bandForm.getMembers().get(i)==realAdmin){
					existAdmin = true;
				}
			}
			if(!existOwner && !existAdmin){
				params.put("owenrId", realOwner);
				throw new IllegalArgumentException("그룹원에 그룹 소유주와 관리자는 존재해야 합니다.");
			}
		}
		
		if(bandForm.getBandCapacity()> enumBandStandard.MAX_CAPACITY.toInt()) {
			throw new IllegalArgumentException("그룹에 허용할 수 있는 용량은 최대 " + enumBandStandard.MAX_CAPACITY.toString()+"입니다.");
		}
		
		
	}
	
	void updatedBand() throws SQLException{
		
		Map<String, Object> set = new HashMap<String, Object>();
		
		if(!bandForm.getBandName().equals(exBand.getString(0, "bandName"))){
			set.put("name", bandForm.getBandName());
		}
		
		int adminId = mDB.getIdOfNickname(bandForm.getAdministrator());
		if(adminId!=exBand.getInteger(0, "administrator")){
			set.put("administrator", adminId);
		}
		
		if(set.size()>0){		
			Map<String, Object> where = new HashMap<String, Object>();
			where.put("bandId" ,bandId);
			dbMng.updateColumn("band", set, where);
			
			if(bCtl.containsEntity(bandId)){
				
				if(set.containsKey("name"))
					bCtl.updateProperty(bandId, "bandName", bandForm.getBandName());
				if(set.containsKey("administrator"))
					bCtl.updateProperty(bandId, "adminId", adminId);
			
			}
		}
	}
	
	void updateBandDetail() throws SQLException{
			
		Map<String, Object> set = new HashMap<String, Object>();
		
		if(bandForm.getBandCapacity()!=exBandDetail.getInteger(0, "maxCapacity")){
			set.put("maxCapacity", bandForm.getBandCapacity());
		}
		
		if(!bandForm.getBandContain().equals(exBandDetail.getString(0, "contents"))){
			set.put("contents", bandForm.getBandContain());
		}
		 
		set.put("updatedDate", new Timestamp(System.currentTimeMillis()) );
				
			if(set.size()>0){
			Map<String, Object> where = new HashMap<String, Object>();
			where.put("bandId" ,bandId);
			dbMng.updateColumn("bandDetail", set, where);
				
			if(bCtl.containsEntity(bandId)){
				
				if(set.containsKey("name"))
					bCtl.updateProperty(bandId, "maxCapacity", bandForm.getBandCapacity());
				if(set.containsKey("administrator"))
					bCtl.updateProperty(bandId, "contents", bandForm.getBandContain());
			}
		}
		
				
	}
	
	void updateMembers() throws ControllerException, EntityException, SQLException{
		
		Map<String, Object> where = new HashMap<String, Object>();
		where.put("bandId", bandId);
		
		List<Integer> exList = new ArrayList<Integer>();
		
		for(Map<String,Object> key : exMembers.getColumns()){
			exList.add((int)key.get("memberId"));
		}
		
		List<String> col = new ArrayList<String>();
		col.add("bandId");
		col.add("memberId");
				
		List<List<Object>> val = new ArrayList<List<Object>>();
		
		for(Integer id : bandForm.getMembers()){
			if(!exList.contains(id)){
				List<Object> added = new ArrayList<Object>();
				added.add((Integer)bandId);
				added.add(id);
				val.add(added);
			}
		}
		if(val.size()>0){
			dbMng.insertColumn("bandMember", col, val);
			
			if(bCtl.containsEntity(bandId)){
				
				for( List<Object> obj : val){
					int memberId = (int)obj.get(1);
					
					
					FileAuthority fa = authMng.makeFileAuthoirty(memberId, bandId, FileAuthority.getFileEnumMap(bandForm.getFileAuthority()));
					MemberAuthority ma = authMng.makeMemberAuthority(memberId, bandId, enumMemberAuthority.MEMBER);

					Member member = mCtl.containsEntity(memberId) == true ? mCtl.getEntity(memberId) : mDB.getMember(memberId);
					AuthoritedMember am = new AuthoritedMember(member,ma,fa);
					
					Band band = bCtl.getEntity(bandId);
					band.addMember(am);
				}
			}
		}
				
		where.clear();
		where.put("bandId", bandId);
		List<Integer> removedList = new ArrayList<Integer>();
		for(Integer id : exList){
			if(!bandForm.getMembers().contains(id)){
				where.put("memberId",id);
				dbMng.deleteColumns("bandMember", where);
				
				if(bCtl.containsEntity(bandId)){
					bCtl.getEntity(bandId).removeMember(id);
				}
				
				removedList.add(id);
			}				
		}
		
	}
	
	void updateFileAuthority() throws SQLException{
			
		
		
		Map<String, Object> set = new HashMap<String, Object>();
		
		for(enumFileAuthority fAuth : enumFileAuthority.values()){
			set.put(fAuth.toString(), 0);
		}
		
		for( String s : bandForm.getFileAuthority()){
			set.put(enumFileAuthority.valueOf(s).toString(),1);
		}
			
		set.put("updatedDate", new Timestamp(System.currentTimeMillis()) );
		
		int onwerId = mDB.getIdOfNickname( bandForm.getBandOwner() );
		Map<String, Object> where = new HashMap<String, Object>();
		where.put("bandId" ,bandId);
		where.put("memberId", onwerId);
		dbMng.updateColumn("fileAuthority", set, where);		
		
	}
	
	void updatedBandAuthority() throws SQLException{
		
		Map<String, Object> set = new HashMap<String, Object>();
		
		for(enumBandAuthority bAuth : enumBandAuthority.values()){
			set.put(bAuth.toString(), 0);
		}
		
		for( String s : bandForm.getBandAuthority()){
			set.put(enumBandAuthority.valueOf(s).toString(),1);
		}
			
		set.put("updatedDate", new Timestamp(System.currentTimeMillis()) );
		
		Map<String, Object> where = new HashMap<String, Object>();
		where.put("bandId" ,bandId);
		dbMng.updateColumn("bandAuthority", set, where);
		
	}
	
	void updatedUpperBand() throws SQLException{
				
				
		if(bandForm.getExUpperBand() != bandForm.getUpperBand()){
			
			Map<String, Object> set = new HashMap<String, Object>();
			set.put("fromBand", bandForm.getUpperBand());
						
			Map<String, Object> where = new HashMap<String, Object>();
			where.put("toBand", bandId);
			where.put("fromBand", bandForm.getUpperBand());
			
			dbMng.updateColumn("bandRelation", set, where);			
		}
		
	}
	
	
}


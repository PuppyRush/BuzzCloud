package cn.bluejoe.elfinder.impl;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.puppyrush.buzzcloud.dbAccess.DBManager;
import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.authority.AuthorityManager;
import com.puppyrush.buzzcloud.entity.authority.file.FileAuthority;
import com.puppyrush.buzzcloud.entity.authority.file.enumFileAuthority;
import com.puppyrush.buzzcloud.entity.band.BandController;
import com.puppyrush.buzzcloud.entity.member.MemberController;

import cn.bluejoe.elfinder.impl.DefaultFsMapping.BandMember;
import cn.bluejoe.elfinder.localfs.LocalFsVolume;
import cn.bluejoe.elfinder.service.FsService;
import cn.bluejoe.elfinder.service.FsServiceFactory;

/**
 * A StaticFsServiceFactory always returns one FsService, despite of whatever it
 * is requested
 * 
 * @author bluejoe
 *
 */

public class StaticFsServiceFactory implements FsServiceFactory
{
	
	@Autowired
	AuthorityManager authMng;

	BandMember bm;
	FsService _fsService;

	public StaticFsServiceFactory(BandMember bm) throws SQLException {
		// TODO Auto-generated constructor stub
		
		
		this.bm = bm;
		init();
		
	}
	
	private void init(){
		
		_fsService.initVolume();
		
		for(LocalFsVolume vs : getVolums(bm.getBandId())){
			_fsService.addVolume(vs.getName(), vs);
		}
				
		try {
			_fsService.setSecurityChecker(getFsCheckChain(bm));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private List<LocalFsVolume> getVolums(int bandId){
		
		List<LocalFsVolume> volums = new ArrayList<LocalFsVolume>();
		
		//..Todo
		LocalFsVolume volume = new LocalFsVolume();
		volume.setName("AA");
		volume.setRootDir(new File("/tmp/a"));
		
		return volums;
	}
	

	private FsSecurityCheckerChain getFsCheckChain(BandMember bm) throws SQLException, ControllerException{
		
		FsSecurityCheckForAll security = getFsSecurityCheck(bm);
		
		FsSecurityCheckFilterMapping mapping = new FsSecurityCheckFilterMapping();
		mapping.setChecker(security);
		mapping.setPattern("A_.*");
		
		List<FsSecurityCheckFilterMapping> list = new ArrayList<FsSecurityCheckFilterMapping>();
		list.add(mapping);
		
		FsSecurityCheckerChain chain = new FsSecurityCheckerChain();
		chain.setFilterMappings(list);
		
		return chain;
	}
	

	
	private FsSecurityCheckForAll getFsSecurityCheck(BandMember bm) throws SQLException, ControllerException{
		
		FsSecurityCheckForAll all = new FsSecurityCheckForAll();
		Map<enumFileAuthority,Boolean> map = authMng.getFileAuthoirty(bm.getMemberId(), bm.getBandId()).getAuthorityType();
		
		if(map.get(enumFileAuthority.DOWNLOAD) && 
			!(map.get(enumFileAuthority.CREATE) && map.get(enumFileAuthority.REMOVE) && map.get(enumFileAuthority.UPLOAD)) ){
			all.setReadable(true);
		}
		
		if(map.get(enumFileAuthority.CREATE)&& map.get(enumFileAuthority.DOWNLOAD) && map.get(enumFileAuthority.REMOVE) && map.get(enumFileAuthority.UPLOAD)){
			all.setWritable(true);
			all.setReadable(true);
		}
		
		if(!(map.get(enumFileAuthority.CREATE) || map.get(enumFileAuthority.DOWNLOAD) || map.get(enumFileAuthority.REMOVE) || map.get(enumFileAuthority.UPLOAD)))
			all.setLocked(true);
		
		
		
		return all;
		
	}
	
	@Override
	public FsService getFileService(HttpServletRequest request,
			ServletContext servletContext)
	{
		return _fsService;
	}

	@Override
	public FsService getFileService()
	{
		return _fsService;
	}

	public void setFsService(FsService fsService)
	{
		_fsService = fsService;
	}

	@Override
	public void invalidate() {
		// TODO Auto-generated method stub
		
		init();
		
	}
}

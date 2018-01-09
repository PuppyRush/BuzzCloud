package cn.bluejoe.elfinder.impl;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.enumController;
import com.puppyrush.buzzcloud.entity.authority.AuthorityManager;
import com.puppyrush.buzzcloud.entity.authority.file.enumFileAuthority;
import com.puppyrush.buzzcloud.entity.band.Band;
import com.puppyrush.buzzcloud.entity.band.BandController;
import com.puppyrush.buzzcloud.page.enums.enumPage;

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

//
final public class StaticFsServiceFactory implements FsServiceFactory
{
	
	BandController bandCtl;
	AuthorityManager authMng;

	BandMember bandMember;
	FsService _fsService;

	public StaticFsServiceFactory(BandMember bm, AuthorityManager authMng, BandController bandCtl ) throws SQLException, ControllerException {
		// TODO Auto-generated constructor stub
				
		this.bandCtl = bandCtl;
		this.authMng = authMng;
		this.bandMember = bm;
		_fsService = new DefaultFsService();
		init();
		
	}
	
	private void init() throws ControllerException{
		
		_fsService.initVolume();
		
		for(LocalFsVolume vs : getVolums(bandMember.getBandId() ) ){
			_fsService.addVolume(vs.getName(), vs);
		}
				
		try {
			_fsService.setSecurityChecker(getFsCheckChain(bandMember));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private List<LocalFsVolume> getVolums(int bandId) throws ControllerException{
		
		List<LocalFsVolume> volums = new ArrayList<LocalFsVolume>();
		
		//..Todo
		
		Band band = null;
		if(bandCtl.containsEntity(bandId))
			band = bandCtl.getEntity(bandId);
		else
			throw (new ControllerException.Builder(enumPage.BROWSER))
			.errorCode(enumController.NOT_EXIST_MEMBER_FROM_MAP).build(); 
						
		final String driverPath = band.getDriverPath();
		final String nickname = band.getDriverNickname();
		
		LocalFsVolume volume = new LocalFsVolume();
		volume.setName(nickname);
		volume.setRootDir(new File(driverPath));
		volums.add(volume);
		
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
			!(map.get(enumFileAuthority.CREATES) && map.get(enumFileAuthority.REMOVE) && map.get(enumFileAuthority.UPLOAD)) ){
			all.setReadable(true);
		}
		
		if(map.get(enumFileAuthority.CREATES)&& map.get(enumFileAuthority.DOWNLOAD) && map.get(enumFileAuthority.REMOVE) && map.get(enumFileAuthority.UPLOAD)){
			all.setWritable(true);
			all.setReadable(true);
		}
		
		if(!(map.get(enumFileAuthority.CREATES) || map.get(enumFileAuthority.DOWNLOAD) || map.get(enumFileAuthority.REMOVE) || map.get(enumFileAuthority.UPLOAD)))
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
		
		try {
			init();
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

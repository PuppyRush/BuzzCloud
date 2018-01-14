package cn.bluejoe.elfinder.service;

import java.sql.SQLException;

import com.puppyrush.buzzcloud.entity.ControllerException;

import cn.bluejoe.elfinder.impl.DefaultFsMapping.BandMember;

public interface FsMapping {

	void addFsServiceFactory(BandMember bm) throws ControllerException, SQLException;
	void removeFsServiceFactory(int bandId);
	void removeFsServiceFactory(BandMember bm);
	FsServiceFactory getFsServiceFactory(BandMember bm);
	boolean contains(BandMember bm);
	
}

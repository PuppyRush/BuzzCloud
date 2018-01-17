package cn.bluejoe.elfinder.service;

import java.sql.SQLException;

import com.puppyrush.buzzcloud.dbAccess.DBException;
import com.puppyrush.buzzcloud.entity.ControllerException;

import cn.bluejoe.elfinder.impl.DefaultFsMapping.BandMember;

public interface FsMapping {

	void addFsServiceFactory(BandMember bm) throws ControllerException, DBException;
	void removeFsServiceFactory(int bandId);
	void removeFsServiceFactory(BandMember bm);
	FsServiceFactory getFsServiceFactory(int bandId, int memberId) throws DBException, ControllerException;
	FsServiceFactory getFsServiceFactory(BandMember bm) throws DBException, ControllerException;
	boolean contains(BandMember bm);
	
}

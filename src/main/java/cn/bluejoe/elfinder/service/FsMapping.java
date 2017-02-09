package cn.bluejoe.elfinder.service;

import cn.bluejoe.elfinder.impl.DefaultFsMapping.BandMember;

public interface FsMapping {

	void addFsServiceFactory(BandMember bm);
	void removeFsServiceFactory(int bandId);
	void removeFsServiceFactory(BandMember bm);
	FsServiceFactory getFsServiceFactory(BandMember bm);
	boolean contains(BandMember bm);
	
}

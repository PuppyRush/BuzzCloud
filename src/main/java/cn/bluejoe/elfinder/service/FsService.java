package cn.bluejoe.elfinder.service;

import java.io.IOException;

import cn.bluejoe.elfinder.controller.executor.FsItemEx;
import cn.bluejoe.elfinder.localfs.LocalFsVolume;

public interface FsService
{
	FsItem fromHash(String hash) throws IOException;

	String getHash(FsItem item) throws IOException;

	FsSecurityChecker getSecurityChecker();

	void setSecurityChecker(FsSecurityChecker fs);
	
	String getVolumeId(FsVolume volume);

	FsVolume[] getVolumes();

	void initVolume();
	
	void addVolume(String key, FsVolume val);
	
	
	FsServiceConfig getServiceConfig();
	
	void setServiceConfig(FsServiceConfig serviceConfig);

	/**
	 * find files by name pattern, this provides a simple recursively iteration
	 * based method lucene engines can be introduced to improve it! This
	 * searches across all volumes.
	 *
	 * @param filter
	 *            The filter to apply to select files.
	 * @return A collection of files that match the filter and gave the root as
	 *         a parent.
	 */
	// TODO: bad designs: FsItemEx should not used here top level interfaces
	// should only know FsItem instead of FsItemEx
	FsItemEx[] find(FsItemFilter filter);
}
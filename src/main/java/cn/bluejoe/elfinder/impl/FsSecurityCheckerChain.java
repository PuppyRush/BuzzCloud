package cn.bluejoe.elfinder.impl;

import java.io.IOException;
import java.util.List;

import cn.bluejoe.elfinder.service.FsItem;
import cn.bluejoe.elfinder.service.FsSecurityChecker;
import cn.bluejoe.elfinder.service.FsService;

public class FsSecurityCheckerChain implements FsSecurityChecker
{
	private static final FsSecurityChecker DEFAULT_SECURITY_CHECKER = new FsSecurityCheckForAll();

	List<FsSecurityCheckFilterMapping> _filterMappings;

	private FsSecurityChecker getChecker(FsService fsService, FsItem fsi)
			throws IOException
	{
		String hash = fsService.getHash(fsi);
		for (FsSecurityCheckFilterMapping mapping : _filterMappings)
		{
			if (mapping.matches(hash))
			{
				return mapping.getChecker();
			}
		}

		return DEFAULT_SECURITY_CHECKER;
	}

	public List<FsSecurityCheckFilterMapping> getFilterMappings()
	{
		return _filterMappings;
	}

	@Override
	public boolean isLocked(FsService fsService, FsItem fsi) throws IOException
	{
		return getChecker(fsService, fsi).isLocked(fsService, fsi);
	}

	@Override
	public boolean isDownloable(FsService fsService, FsItem fsi)
			throws IOException
	{
		return getChecker(fsService, fsi).isDownloable(fsService, fsi);
	}

	@Override
	public boolean isUploadable(FsService fsService, FsItem fsi) throws IOException {
		// TODO Auto-generated method stub
		return getChecker(fsService, fsi).isUploadable(fsService, fsi);
	}

	@Override
	public boolean isRemovable(FsService fsService, FsItem fsi) throws IOException {
		return getChecker(fsService, fsi).isRemovable(fsService, fsi);
	}

	@Override
	public boolean isCreatable(FsService fsService, FsItem fsi) throws IOException {
		return getChecker(fsService, fsi).isCreatable(fsService, fsi);
	}
	
	public void setFilterMappings(
			List<FsSecurityCheckFilterMapping> filterMappings)
	{
		_filterMappings = filterMappings;
	}
}

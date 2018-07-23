package cn.bluejoe.elfinder.impl;

import java.io.IOException;

import cn.bluejoe.elfinder.service.FsItem;
import cn.bluejoe.elfinder.service.FsSecurityChecker;
import cn.bluejoe.elfinder.service.FsService;

public class FsSecurityCheckForAll implements FsSecurityChecker
{
	boolean _locked = true;
	boolean _downloadable = false;
	boolean _uploadable = false;
	boolean _removable = false;
	boolean _creatable = false;

	public boolean isRemovable()
	{
		return _removable;
	}

	@Override
	public boolean isRemovable(FsService fsService, FsItem fsi)
	{
		return _removable;
	}
	
	public boolean isUploadable()
	{
		return _uploadable;
	}

	@Override
	public boolean isUploadable(FsService fsService, FsItem fsi)
	{
		return _uploadable;
	}
	
	public boolean isLocked()
	{
		return _locked;
	}

	@Override
	public boolean isLocked(FsService fsService, FsItem fsi)
	{
		return _locked;
	}

	public boolean isDownloadable()
	{
		return _downloadable;
	}

	@Override
	public boolean isDownloable(FsService fsService, FsItem fsi)
	{
		return _downloadable;
	}

	public boolean isCreatable()
	{
		return _creatable;
	}
	
	@Override
	public boolean isCreatable(FsService fsService, FsItem fsi)
	{
		return _creatable;
	}

	
	public void setRemovable(boolean locked)
	{
		_removable = locked;
	}
	
	public void setUploadable(boolean locked)
	{
		_uploadable = locked;
	}
	
	public void setLocked(boolean locked)
	{
		_locked = locked;
	}

	public void setDownloadable(boolean writable)
	{
		_downloadable = writable;
	}

	public void setCreatable(boolean writable)
	{
		_creatable = writable;
	}
}

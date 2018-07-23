package cn.bluejoe.elfinder.service;

import java.io.IOException;

public interface FsSecurityChecker
{

	boolean isLocked(FsService fsService, FsItem fsi) throws IOException;

	boolean isDownloable(FsService fsService, FsItem fsi) throws IOException;
	
	boolean isUploadable(FsService fsService, FsItem fsi) throws IOException;
	
	boolean isRemovable(FsService fsService, FsItem fsi) throws IOException;
	
	boolean isCreatable(FsService fsService, FsItem fsi) throws IOException;

}
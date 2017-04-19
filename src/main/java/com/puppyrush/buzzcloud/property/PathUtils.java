package com.puppyrush.buzzcloud.property;

public class PathUtils {

	private PathUtils(){
		
	}
	
	public static String toAbsolutePathFromImage(int memberId, String imageName){
		
		return new StringBuilder(enumSystem.RESOURCE_FOLDER_ABS_PATH.toString())
				.append(enumSystem.MEMBERS_FOLDER_NAME.toString()).append("/")
				.append(String.valueOf(memberId)).append("/").append(imageName).toString();
		
		
	}
	
	public static String toRelativePathFromImage(int memberId, String imageName){
		
		return new StringBuilder(enumSystem.RESOURCE_FOLDER_RAT_PATH.toString())
				.append(enumSystem.MEMBERS_FOLDER_NAME.toString()).append("/")
				.append(String.valueOf(memberId)).append("/").append(imageName).toString();
		
		
	}
	
}

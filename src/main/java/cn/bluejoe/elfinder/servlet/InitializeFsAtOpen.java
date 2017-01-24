package cn.bluejoe.elfinder.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InitializeFsAtOpen {


	private static String groupName;
	private static String groupDir;
	
	public static String getGroupName() {
		return groupName;
	}

	public static String getGroupDir() {
		return groupDir;
	}
	
	public static void setGroup(String name, String dir){
		groupName = name;
		groupDir = dir;
	
	}
	
}

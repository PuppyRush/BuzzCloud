package com.puppyrush.buzzcloud.entity.message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;

import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.impl.EntityControllerImpl;
import com.puppyrush.buzzcloud.entity.member.MemberController;
import com.puppyrush.buzzcloud.property.ConnectMysql;

public final class MessageController extends EntityControllerImpl<Message>{

	private MessageController(){}

	private static class Singleton {
		private static final MessageController instance = new MessageController();
	}
	
	public static MessageController getInstance () {
		return Singleton.instance;
	}

	
	
}

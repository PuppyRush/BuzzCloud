package entity.message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;

import entity.member.MemberController;
import entity.ControllerException;
import entity.impl.EntityControllerImpl;
import property.ConnectMysql;

public final class MessageController extends EntityControllerImpl<Message>{

	private MessageController(){}

	private static class Singleton {
		private static final MessageController instance = new MessageController();
	}
	
	public static MessageController getInstance () {
		return Singleton.instance;
	}

}

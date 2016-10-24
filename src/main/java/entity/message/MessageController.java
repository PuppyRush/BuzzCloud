package entity.message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;

import entity.EntityControllerImpl;
import entity.member.MemberController;
import entity.member.MemberController.Singleton;
import entity.ControllerException;
import property.ConnectMysql;
import property.enums.enumController;

public final class MessageController extends EntityControllerImpl{

	private MessageController(){}

	private static class Singleton {
		private static final MessageController instance = new MessageController();
	}
	
	public static MessageController getInstance () {
		return Singleton.instance;
	}

}

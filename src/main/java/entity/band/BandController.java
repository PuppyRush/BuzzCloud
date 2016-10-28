package entity.band;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;

import entity.member.Member;
import entity.member.MemberController;
import entity.member.MemberManager;
import entity.ControllerException;
import entity.EntityException;
import entity.authority.AuthorityManager;
import entity.authority.FileAuthority;
import entity.authority.BandAuthority;
import entity.authority.MemberAuthority;
import entity.authority.enums.enumBandAuthority;
import entity.band.Band.AuthoritedMember;
import entity.impl.EntityControllerImpl;
import property.ConnectMysql;
import property.tree.Node;
import property.tree.Tree;

public final class BandController extends EntityControllerImpl<Band>{

	private BandController(){}
	
	private static class Singleton {
		private static final BandController instance = new BandController();
	}
	
	public static BandController getInstance () {
		return Singleton.instance;
	}



}

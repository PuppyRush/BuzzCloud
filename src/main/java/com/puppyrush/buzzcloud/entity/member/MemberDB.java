package com.puppyrush.buzzcloud.entity.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.puppyrush.buzzcloud.dbAccess.DBManager;
import com.puppyrush.buzzcloud.dbAccess.DBManager.ColumnHelper;
import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.enumController;
import com.puppyrush.buzzcloud.entity.enumEntityState;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberAbnormalState;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberStandard;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberState;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberType;
import com.puppyrush.buzzcloud.entity.message.instanceMessage.enumInstanceMessage;
import com.puppyrush.buzzcloud.mail.PostManImple;
import com.puppyrush.buzzcloud.mail.PostManImple.Builder;
import com.puppyrush.buzzcloud.mail.PostMan;
import com.puppyrush.buzzcloud.mail.enumMail;
import com.puppyrush.buzzcloud.mail.enumMailType;
import com.puppyrush.buzzcloud.page.enums.enumPage;
import com.puppyrush.buzzcloud.property.ConnectMysql;

@Service("memerDb")
public class MemberDB {

	private static Connection conn = ConnectMysql.getConnector();
	
	@Autowired
	private PostManImple postman;
	
	@Autowired
	private MemberController mCtl;
	
	@Autowired
	private DBManager dbMng;
	
	
	
	public  EnumMap<enumMemberAbnormalState, Boolean> getMemberAbnormalStates(int memberId) throws SQLException{

		EnumMap<enumMemberAbnormalState, Boolean> stateMap = new EnumMap<>(enumMemberAbnormalState.class);

		Map<String, Object> where = new HashMap<String, Object>();
		where.put("memberId", memberId);
		ColumnHelper ch = dbMng.getColumnsOfAll("memberState", where);
		if(!ch.isUnique())
			throw new SQLException();
		
		for(enumMemberAbnormalState e : enumMemberAbnormalState.values()){												
			String _attributeName = e.toString();
			if(ch.getInteger(0, _attributeName)==1)
				stateMap.put(e, true);
			else
				stateMap.put(e, false);
		}
		

		return stateMap;		
	}

	
	public  Member getMember(int memberId) throws SQLException, EntityException, ControllerException{
		
		Member member = null;

		Map<String, Object> where = new HashMap<String, Object>();
		where.put("memberId", memberId);
		ColumnHelper ch = dbMng.getColumnsOfAll("member", where);
		
		if(!ch.isUnique())
			throw (new EntityException.Builder(enumPage.LOGIN_MANAGER))
			.errorCode(enumController.NOT_EXIST_MEMBER_FROM_MAP).build();
		
		String email = ch.getString(0,"email");
		EnumMap<enumMemberAbnormalState, Boolean> state = getMemberAbnormalStates(memberId);
		enumMemberType idType = enumMemberType.valueOf(ch.getString(0,"registrationKind"));
		String nickname = ch.getString(0,"nickname");
		
		member = new Member.Builder(memberId,email).abnormalState(state).registrationKind(idType).nickname(nickname).build();
		
		if(mCtl.containsEntity(memberId) == false)
			mCtl.addEntity(memberId, member);
		
		return member;
	}
	
	public Member getMember(String email) throws EntityException, ControllerException, SQLException{
	
		Member member = null;

		Map<String, Object> where = new HashMap<String, Object>();
		where.put("email", email);
		
		ColumnHelper ch = dbMng.getColumnsOfAll("member",  where);
				
		if(ch.isEmpty())
			throw (new EntityException.Builder(enumPage.JOIN))
			.instanceMessage("가입 후 로그인 바랍니다")
			.instanceMessageType(enumInstanceMessage.ERROR)
			.errorCode(enumMemberState.NOT_JOIN).build();
		else if(ch.columnSize()>1)
			throw new SQLException();
		
		int memberId= ch.getInteger(0, "memberId");
		EnumMap<enumMemberAbnormalState, Boolean> state = getMemberAbnormalStates(memberId);
		enumMemberType idType = enumMemberType.valueOf(ch.getString(0, "registrationKind"));
		String nickname = ch.getString(0, "nickname");
		
		member = new Member.Builder(memberId,email).abnormalState(state).registrationKind(idType).nickname(nickname).build();

		return member;
	}
		
	public boolean isExistNickname(String nickname, int memberId) throws SQLException{
		
		List<String> sel = new ArrayList<String>();
		sel.add("memberId");
		Map<String, Object> where = new HashMap<String, Object>();
		where.put("nickname", nickname);
		where.put("memberId", memberId);

		ColumnHelper ch = dbMng.getColumnsOfPart("member", sel, where);
		
		if(ch.isEmpty())
			return false;
		else
			return true;
	}
	
	public  boolean isJoin(int memberId) throws SQLException{
		
		List<String> sel = new ArrayList<String>();
		sel.add("memberId");
		Map<String, Object> where = new HashMap<String, Object>();
		where.put("memberId", memberId);

		ColumnHelper ch = dbMng.getColumnsOfPart("member", sel, where);
		
		if(ch.isEmpty())
			return false;
		else
			return true;
	}
	
	public  boolean isJoin(String email) throws SQLException{
		
		List<String> sel = new ArrayList<String>();
		sel.add("email");
		Map<String, Object> where = new HashMap<String, Object>();
		where.put("email", email);

		ColumnHelper ch = dbMng.getColumnsOfPart("member", sel, where);
		
		if(ch.isEmpty())
			return false;
		else
			return true;
	}
	
	public int getIdOfNickname(String nickname) throws SQLException{

		List<String> sel = new ArrayList<String>();
		Map<String, Object> where = new HashMap<String, Object>();
		sel.add("memberid");
		
		where.put("nickname", nickname);
		ColumnHelper ch = dbMng.getColumnsOfPart("member", sel, where);
		
		if(ch.isUnique())
			return ch.getInteger(0, "memberId");
		else
			return enumMemberStandard.NOT_JOIN_MEMBER.toInt();
		
	}

	public int getIdOfEmail(String email) throws SQLException{
		
		List<String> sel = new ArrayList<String>();
		Map<String, Object> where = new HashMap<String, Object>();
		
		sel.add("memberId");
		where.put("email", email);

		ColumnHelper ch = dbMng.getColumnsOfPart("member", sel, where);
		if(ch.isUnique())
			return ch.getInteger(0, sel.get(0));
		else
			return enumMemberStandard.NOT_JOIN_MEMBER.toInt();
	}

	public String getNicknameOfId(int id) throws SQLException{
		
		List<String> sel = new ArrayList<String>();
		Map<String, Object> where = new HashMap<String, Object>();
		
		sel.add("nickname");
		where.put("memberId", id);

		ColumnHelper ch = dbMng.getColumnsOfPart("member", sel, where);
		if(ch.isUnique())
			return ch.getString(0, sel.get(0));
		else
			throw new SQLException();
	}
	
/*
	
	public  ArrayList<Member> getAllMember() throws SQLException{
		
		ArrayList<Member> _members = new ArrayList<Member>();

		
		
		 _ps = conn.prepareStatement("select * from member");
		 _rs = _ps.executeQuery();
		
		while(_rs.next()){
										
		
						
			__ps = conn.prepareStatement("select * from memberState where memberId = ?");
			__ps.setInt(1, _rs.getInt("memberId"));
			__rs = __ps.executeQuery();
			__rs.next();
			
			EnumMap<enumMemberAbnormalState, Boolean> state = new EnumMap<>(enumMemberAbnormalState.class);
			
			
			if(__rs.getInt("isAbnormal")==1){
	
				
				if(__rs.getInt(enumMemberAbnormalState.LOST_PASSWORD.toString())==1)
					state.put(enumMemberAbnormalState.LOST_PASSWORD, true);
				else
					state.put(enumMemberAbnormalState.LOST_PASSWORD, false);
				
				if(__rs.getInt(enumMemberAbnormalState.EXCEEDED_LOGIN_COUNT.toString())==1)
					state.put(enumMemberAbnormalState.EXCEEDED_LOGIN_COUNT, true);
				else
					state.put(enumMemberAbnormalState.EXCEEDED_LOGIN_COUNT, false);
				
				if(__rs.getInt(enumMemberAbnormalState.SLEEP.toString())==1)
					state.put(enumMemberAbnormalState.SLEEP, true);
				else
					state.put(enumMemberAbnormalState.SLEEP, false);
				
				if(__rs.getInt(enumMemberAbnormalState.OLD_PASSWORD.toString())==1)
					state.put(enumMemberAbnormalState.OLD_PASSWORD, true);
				else
					state.put(enumMemberAbnormalState.OLD_PASSWORD, false);
				
				if(__rs.getInt(enumMemberAbnormalState.JOIN_CERTIFICATION.toString())==1)
					state.put(enumMemberAbnormalState.JOIN_CERTIFICATION, true);
				else
					state.put(enumMemberAbnormalState.JOIN_CERTIFICATION, false);
			}
	
			Member member = (new Member.Builder(_rs.getInt("memberId"), _rs.getString("email"))).registrationKind(enumMemberType.valueOf(_rs.getString("idType"))  )
					.nickname(_rs.getString("nickname")).abnormalState(state).build();
			
			_members.add(member);
			
		}
		
		return _members;
	}*/
		
	public boolean isCertificatingJoin(int uId) throws EntityException, SQLException{
		
		boolean isDoing = false;
			
		Map<String, Object> where = new HashMap<String, Object>();
		where.put("memberId", uId);
		
		ColumnHelper ch = dbMng.getColumnsOfAll("memberId", where);
		if(!ch.isUnique())
			return false;
			
		if(ch.getInteger(0, "joinCertification")==1){

			ch = dbMng.getColumnsOfAll("joinCertification", where);

			if(ch.isUnique())
				isDoing = true;
			else
				isDoing = false;
			
		}else
			throw (new EntityException.Builder(enumPage.LOGIN))
			.instanceMessageType(enumInstanceMessage.ERROR)
			.instanceMessage("가입 인증 메일을 보낸 상태입니다. 인증메일을 다시 요청하고 싶으시면 같은 메일로 다시 가입 하세요.")
			.errorCode(enumMemberState.ALREADY_CERTIFICATION).build();
		
		return isDoing;
	}


	public boolean isOnSiteAccount(String email) throws SQLException{
		
		Map<String, Object> where = new HashMap<String, Object>();
		List<String> sel = new ArrayList<String>();
		int id = getIdOfEmail(email);
		where.put("memberId", id);
		sel.add("registrationKind");
		ColumnHelper ch = dbMng.getColumnsOfPart("member", sel, where);
		
		if(!ch.isUnique())
			return false;
		else{
			if(enumMemberType.valueOf(ch.getString(0, "registrationKind")).equals(enumMemberType.NOTHING))
				return true;
			else 
				return false;
				
		}
		
	}
	
}


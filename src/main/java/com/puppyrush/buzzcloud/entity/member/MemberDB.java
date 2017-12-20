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

		PreparedStatement ps = null;
		ResultSet rs = null;
		EnumMap<enumMemberAbnormalState, Boolean> stateMap = new EnumMap<>(enumMemberAbnormalState.class);

		ps = conn.prepareStatement("select * from memberState where memberId = ?");
		ps.setInt(1, memberId);			
		rs = ps.executeQuery();
		rs.next();
	
		if(rs.getInt("isAbnormal")==1){
		
			for(enumMemberAbnormalState e : enumMemberAbnormalState.values()){												
				String _attributeName = e.getString();
				if(rs.getInt(_attributeName)==1)
					stateMap.put(e, true);
				else
					stateMap.put(e, false);
			}
		}

		return stateMap;		
	}

	
	public  Member getMember(int memberId){
		
		PreparedStatement ps = null;
		ResultSet rs= null;
		Member member = null;
		
		try{
			
			ps = conn.prepareStatement("select * from member where memberId = ?");
			ps.setInt(1, memberId);
			rs = ps.executeQuery();
			if(!rs.next())
				throw (new EntityException.Builder(enumPage.LOGIN_MANAGER))
				.errorCode(enumController.NOT_EXIST_MEMBER_FROM_MAP).build();
				
			
			String email = rs.getString("email");
			EnumMap<enumMemberAbnormalState, Boolean> state = getMemberAbnormalStates(memberId);
			enumMemberType idType = enumMemberType.valueOf(rs.getString("registrationKind"));
			String nickname = rs.getString("nickname");
			
			member = new Member.Builder(memberId,email).abnormalState(state).registrationKind(idType).nickname(nickname).build();
			
			if(mCtl.containsEntity(memberId) == false)
				mCtl.addEntity(memberId, member);
			
		}catch(SQLException e){
			e.printStackTrace();
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
			try {
				ps.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return member;
	}
	
	public Member getMember(String email) throws EntityException, ControllerException, SQLException{
	
		Member member = null;

		Map<String, Object> where = new HashMap<String, Object>();
		where.put("email", email);
		
		ColumnHelper ch = dbMng.getColumnsOfAll("member",  where);
				
		if(ch.isEmpty())
			throw (new EntityException.Builder(enumPage.JOIN))
			.errorString("가입 후 로그인 바랍니다")
			.instanceMessage(enumInstanceMessage.ERROR)
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
		
	public boolean isExistNickname(String nickname){
		
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean result = false;
		try{
			ps = conn.prepareStatement("select memberId from member where nickname = ?");
			ps.setString(1, nickname);
			rs = ps.executeQuery();
			
			if(rs.next())			
				result = true;	
			else
				result =false;
			
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
		} finally {
			try {
				ps.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result = false;
			}
		}
		
		return result;
		
	}
	
	public  boolean isJoin(int uId) throws SQLException{
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean result = false;
		try{
			ps = conn.prepareStatement("select * from member where memberId = ?");
			ps.setInt(1, uId);
			rs = ps.executeQuery();
			
			if(rs.next())			
				result = true;	
			else
				result =false;
			
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
		} finally {
			try {
				ps.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result = false;
			}
		}
		
		return result;
		
	}
	
	public  boolean isJoin(String email) throws SQLException{
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean result = false;
		try{
			ps = conn.prepareStatement("select * from member where email = ?");
			ps.setString(1, email);
			rs = ps.executeQuery();
			
			if(rs.next())			
				result = true;
				
			else
				result =false;
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
		} finally {
			try {
				ps.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result = false;
			}
		}

		return result;
	}
	
	public int getIdOfNickname(String nickname) throws SQLException{
		
		int id= -1;
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(" select memberId from member where nickname = ? ");
			ps.setString(1,nickname);
			ResultSet rs = ps.executeQuery();
			rs.next();
			id = rs.getInt("memberId");		
			
			ps.close();
			rs.close();
					
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return id;
		
	}

	public int getIdOfEmail(String email){
		
		int id= -1;
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(" select memberId from member where email = ? ");
			ps.setString(1,email);
			ResultSet rs = ps.executeQuery();
			rs.next();
			id = rs.getInt("memberId");		
			
			ps.close();
			rs.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return id;
	}

	public String getNicknameOfId(int id){
		
		String nickname = "";
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(" select nickname from member where memberId = ? ");
			ps.setInt(1,id);
			ResultSet rs = ps.executeQuery();
			rs.next();
			nickname = rs.getString("nickname");		
			
			rs.close();
			ps.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return nickname;
		
	}
	
	public  boolean changePassword(Member member, String newPassword) throws Throwable{
		
		PreparedStatement _sp = conn.prepareStatement("update member set password = ? where memberId = ?");
		
		String _hashPassword = BCrypt.hashpw( newPassword, BCrypt.gensalt());
		_sp.setString(1, _hashPassword);
		_sp.setInt(2, member.getId());
				
		return true;
		
	}

	public  void withdrawMember(int uId, String nickname, String email, String reason) throws SQLException, AddressException, MessagingException{
		
		PreparedStatement _ps= null;
		try{
			
			conn.setAutoCommit(false);
		
		
			_ps= conn.prepareStatement("delete from member where memberId = ?");
			_ps.setInt(1, uId);
			_ps.executeQuery();			
			
			String subject = "[WidetStore] 탈퇴 ";
			String content = new StringBuilder("안녕하세요.  버즈클라우드에서 알립니다. ").append(nickname)
					.append("님이 버즈클라우드에서 탈퇴 됐습니다.\n 만일 탈퇴를 요청하지 않은 경우면 관리자에게 문의하세요.\n").append("탈퇴사유 : ")
					.append(reason).toString();
						
			Builder bld = new PostManImple.Builder(enumMail.gmailID.toString(), email).subject(subject).content(content).build();
			postman.send(bld);
			
			
			conn.commit();
		}finally{
			
			_ps.close();
		}
		
	}
	
	public  ArrayList<Member> getAllMember(){
		
		ArrayList<Member> _members = new ArrayList<Member>();
		PreparedStatement _ps=null, __ps=null;
		ResultSet _rs=null, __rs=null;
		
		try{
			
			 _ps = conn.prepareStatement("select * from member");
			 _rs = _ps.executeQuery();
			
			while(_rs.next()){
											
			
							
				__ps = conn.prepareStatement("select * from memberState where memberId = ?");
				__ps.setInt(1, _rs.getInt("memberId"));
				__rs = __ps.executeQuery();
				__rs.next();
				
				EnumMap<enumMemberAbnormalState, Boolean> state = new EnumMap<>(enumMemberAbnormalState.class);
				
				
				if(__rs.getInt("isAbnormal")==1){
		
					
					if(__rs.getInt(enumMemberAbnormalState.LOST_PASSWORD.getString())==1)
						state.put(enumMemberAbnormalState.LOST_PASSWORD, true);
					else
						state.put(enumMemberAbnormalState.LOST_PASSWORD, false);
					
					if(__rs.getInt(enumMemberAbnormalState.EXCEEDED_LOGIN_COUNT.getString())==1)
						state.put(enumMemberAbnormalState.EXCEEDED_LOGIN_COUNT, true);
					else
						state.put(enumMemberAbnormalState.EXCEEDED_LOGIN_COUNT, false);
					
					if(__rs.getInt(enumMemberAbnormalState.SLEEP.getString())==1)
						state.put(enumMemberAbnormalState.SLEEP, true);
					else
						state.put(enumMemberAbnormalState.SLEEP, false);
					
					if(__rs.getInt(enumMemberAbnormalState.OLD_PASSWORD.getString())==1)
						state.put(enumMemberAbnormalState.OLD_PASSWORD, true);
					else
						state.put(enumMemberAbnormalState.OLD_PASSWORD, false);
					
					if(__rs.getInt(enumMemberAbnormalState.JOIN_CERTIFICATION.getString())==1)
						state.put(enumMemberAbnormalState.JOIN_CERTIFICATION, true);
					else
						state.put(enumMemberAbnormalState.JOIN_CERTIFICATION, false);
				}
		
				Member member = (new Member.Builder(_rs.getInt("memberId"), _rs.getString("email"))).registrationKind(enumMemberType.valueOf(_rs.getString("idType"))  )
						.nickname(_rs.getString("nickname")).abnormalState(state).build();
				
				_members.add(member);
				
			}
			
		
		}catch(Throwable e){
			
		}finally{
			if (_ps != null)
				try {
					_ps.close();
				} catch (SQLException ex) {
				}
			if (_rs != null)
				try {
					_rs.close();
				} catch (SQLException ex) {
				}
			if (__ps != null)
				try {
					__ps.close();
				} catch (SQLException ex) {
				}
			if (__rs != null)
				try {
					__rs.close();
				} catch (SQLException ex) {
				}
		}
		return _members;
	}
		
	public boolean isCertificatingJoin(int uId) throws EntityException{
		
		boolean isDoing = false;
		
		try{
				
			PreparedStatement ps = conn.prepareStatement(" select * from memberState where memberId = ? ");
			ps.setInt(1, uId);
			ResultSet rs = ps.executeQuery();
			
			if(!rs.next())
				throw (new EntityException.Builder(enumPage.JOIN))
				.errorCode(enumMemberState.NOT_JOIN).build();
						
			if(rs.getInt("joinCertification")==1){
				
				ps.close();
				rs.close();
				
				ps = conn.prepareStatement("select * from joinCertification where memberId = ?");
				ps.setInt(1, uId);
				rs = ps.executeQuery();
				
				if(rs.next())					
					isDoing = true;
				else
					isDoing = false;
				
			}else
				throw (new EntityException.Builder(enumPage.LOGIN))
				.instanceMessage(enumInstanceMessage.ERROR)
				.errorString("가입 인증 메일을 보낸 상태입니다. 인증메일을 다시 요청하고 싶으시면 같은 메일로 다시 가입 하세요.")
				.errorCode(enumMemberState.ALREADY_CERTIFICATION).build();


			
		}catch(SQLException e){
			
			e.printStackTrace();
			
		}
		
		return isDoing;
	}


	public boolean isOnSiteAccount(String email){
		
		Map<String, Object> where = new HashMap<String, Object>();
		List<String> sel = new ArrayList<String>();
		int id = getIdOfEmail(email);
		where.put("memberId", id);
		sel.add("registrationKind");
		ColumnHelper ch = dbMng.getColumnsOfPart("member", sel, where);
		
		if(ch.isEmpty())
			return false;
		else if(enumMemberType.valueOf(ch.getString(0, "registrationKind")).equals(enumMemberType.NOTHING)){
			return true;
		}
		return false;
		
	}
	
}


package com.puppyrush.buzzcloud.entity.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EnumMap;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.enumEntityState;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberAbnormalState;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberState;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberType;
import com.puppyrush.buzzcloud.mail.PostManImple;
import com.puppyrush.buzzcloud.mail.PostMan;
import com.puppyrush.buzzcloud.mail.enumMail;
import com.puppyrush.buzzcloud.mail.enumMailType;
import com.puppyrush.buzzcloud.page.enums.enumPage;
import com.puppyrush.buzzcloud.property.ConnectMysql;

@Service("memerDb")
public class MemberDB {

	private static Connection conn = ConnectMysql.getConnector();
	
	
	@Autowired
	private MemberController mCtl;
	
	public static void setConn(Connection conn) {
		MemberDB.conn = conn;
	}


	public void setMemberController(MemberController mCtl) {
		this.mCtl = mCtl;
	}
	
	
	public  EnumMap<enumMemberAbnormalState, Boolean> getMemberAbnormalStates(int memberId) throws Throwable{

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
				throw new EntityException(enumEntityState.NOT_EXIST_IN_MAP);
			
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
	
	public Member getMember(String email){
			
		PreparedStatement ps = null;
		ResultSet rs= null;
		Member member = null;
		
		try{
			
			ps = conn.prepareStatement("select * from member where email = ?");
			ps.setString(1, email);
			rs = ps.executeQuery();
			if(!rs.next())
				throw new EntityException(enumEntityState.NOT_EXIST_IN_MAP);
			
			int memberId= rs.getInt("memberId");
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
			
			PostMan man = new PostManImple.Builder(enumMail.gmailID.toString(), email).subject(subject).content(content).build();
			man.send();
			
			
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
					
					if(__rs.getInt(enumMemberAbnormalState.FAILD_LOGIN.getString())==1)
						state.put(enumMemberAbnormalState.FAILD_LOGIN, true);
					else
						state.put(enumMemberAbnormalState.FAILD_LOGIN, false);
					
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
				throw new EntityException(enumMemberState.NOT_JOIN, enumPage.JOIN);
						
			if(rs.getInt("joinCertification")==1){
				
				ps.close();
				rs.close();
				
				ps = conn.prepareStatement("select * from mail where memberId = ? and certificationKind = ?");
				ps.setInt(1, uId);
				ps.setInt(2, Integer.valueOf(enumMailType.JOIN.toString()));
				rs = ps.executeQuery();
				
				if(rs.next())					
					isDoing = true;
				else
					isDoing = false;
				
			}else
				throw new EntityException("가입 인증 메일을 보낸 상태입니다. 인증메일을 다시 요청하고 싶으시면 같은 메일로 다시 가입 하세요.",
						enumMemberState.ALREADY_CERTIFICATION, enumPage.LOGIN);

			
		}catch(SQLException e){
			
			e.printStackTrace();
			
		}
		
		return isDoing;
	}


	
}


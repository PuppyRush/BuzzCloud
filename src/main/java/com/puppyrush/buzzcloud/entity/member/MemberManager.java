
package com.puppyrush.buzzcloud.entity.member;


import java.sql.*;
import java.util.*;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.metadata.Db2CallMetaDataProvider;
import org.springframework.stereotype.Service;

import com.puppyrush.buzzcloud.dbAccess.DBManager;
import com.puppyrush.buzzcloud.dbAccess.DBManager.ColumnHelper;
import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberAbnormalState;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberStandard;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberState;
import com.puppyrush.buzzcloud.mail.MailManager;
import com.puppyrush.buzzcloud.mail.PostMan;
import com.puppyrush.buzzcloud.mail.PostManImple;
import com.puppyrush.buzzcloud.mail.enumMail;
import com.puppyrush.buzzcloud.mail.enumMailState;
import com.puppyrush.buzzcloud.mail.enumMailType;
import com.puppyrush.buzzcloud.mail.PostManImple.Builder;
import com.puppyrush.buzzcloud.page.enums.enumPage;
import com.puppyrush.buzzcloud.property.ConnectMysql;

@Service("memberManager")
public final class MemberManager {
	
	private static Connection conn = ConnectMysql.getConnector();
	
	@Autowired
	private MemberDB mDB;
	
	@Autowired
	private MemberController mCtl;

	@Autowired
	private DBManager dbMng;
	
	@Autowired
	private PostMan postman;
	
	@Autowired
	private MailManager mailMng;
	
	
	
	/**
	 * 	가입 후 인증메일로 받은 email과 인증번호를 비교하여 결과를 반환한다.
	 * @param email 가입당시 사용한 메
	 * @param hashedUUID  가입인증을 위해 발급된 인증번호(해시된 비번)
	 * @return
	 * @throws Throwable 
	 */
	public  boolean resolveCertificateJoin(String sId, String email, String hashedUUID) throws Throwable {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		if(mDB.isJoin(email) == false)
			throw (new EntityException.Builder(enumPage.JOIN))
			.errorCode(enumMemberState.NOT_JOIN).build(); 


		Member member;
		if(mCtl.containsEntity(sId))
			member = mCtl.getMember(sId);
		else{
			member = mDB.getMember(email);
			mCtl.addMember(member, sId);
		}
		 
		int memberId = member.getId();
		
		List<String> sel = new ArrayList<String>();
		sel.add("certificationNumber");
		
		Map<String, Object> where = new HashMap<String, Object>();
		where.put("memberId", memberId);
		
		ColumnHelper ch = dbMng.getColumnsOfPart("joinCertification", sel, where);
		
		if(ch.columnSize() != 1)
			throw new SQLException(email+","+memberId+"joinCertification테이블에 id가 둘 이상 존재하거나 한개도 존재하지 않습니다.");
		 			
		String planeUUID = ch.getString(0, "certificationNumber");

		if(BCrypt.checkpw( planeUUID, hashedUUID )){
			
			mailMng.invalidateMailOf(email, enumMailType.JOIN_CERTIFICATION);
			
			Map<String, Object> set = new HashMap<String, Object>();
			set.put("joinCertification", enumMemberState.RESOLVE_JOIN.toInt());
			
			dbMng.updateColumn("memberState", set, where);
					
			member.setNickname(mDB.getNicknameOfId(memberId));
			
			String subject = "버즈클라우드에 가입하실것을 환영합니다.";
			String content = member.getNickname() + "의 버즈클라우드 가입을 축하드립니다. 버즈클라우드 서비스의 사용방법은 사이트를 참고해주세요. 감사합니다.";
			
			Builder bld = new PostManImple.Builder(enumMail.gmailID.toString(), email).subject(subject).content(content).build();
			postman.send(bld);
			
		
		}else
			throw new SQLException();
	
		
		return true;
	}
	
	public  boolean requestCertificateJoin(Member member) throws EntityException, NumberFormatException, AddressException, SQLException, MessagingException{
		
		PreparedStatement ps;
		
		String certificationNumber = mailMng.SendCertificationMail(member, enumMailType.JOIN_CERTIFICATION);
		
		List<String> col = new ArrayList<String>();
		col.add("memberId");
		col.add("certificationNumber");
		
		List<List<Object>> values = new ArrayList<List<Object>>();
		List<Object> list = new ArrayList<Object>();
		list.add(member.getId());
		list.add(certificationNumber);
		values.add(list);
		
		dbMng.insertColumn("joinCertification", col, values);		
		return true;
	}
	
	public void updatePassword(int id, String planePassword) throws ControllerException, SQLException{
				
		String hashedPw = BCrypt.hashpw( planePassword, BCrypt.gensalt(12));
		
		Map<String, Object> set = new HashMap<String, Object>();
		Map<String, Object> where = new HashMap<String, Object>();
		
		set.put("password", hashedPw);
		where.put("memberId", id);
		
		dbMng.updateColumn("member", set, where);
	
		mCtl.updateProperty(id, "planePassword", planePassword);
	}

	public void updateMemberAbnormalState(String email, enumMemberAbnormalState state, int setValue) throws SQLException, EntityException, ControllerException{
			
		Map<String, Object> where = new HashMap<String, Object>();
		Map<String, Object> set = new HashMap<String, Object>();
		
		int id = mDB.getIdOfEmail(email);
		Member member = mDB.getMember(email);
		
		EnumMap<enumMemberAbnormalState, Boolean> mState = member.getAbnormalState();
		mState.put(state, setValue==0?false:true);
		
		boolean isAbnormal=false;
		for(enumMemberAbnormalState _state : mState.keySet()){
			if(!_state.equals(enumMemberAbnormalState.IS_ABNORMAL) && mState.get(_state)){
				isAbnormal=true;
				break;
			}
		}
			
		where.put("memberId", id);
		set.put("isAbnormal", isAbnormal?1:0);
		set.put(state.toString(), setValue);
		
		dbMng.updateColumn("memberState", set, where);		
		set.clear();
		
		mCtl.updateProperty(id, "abnormalState", mState);
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
}



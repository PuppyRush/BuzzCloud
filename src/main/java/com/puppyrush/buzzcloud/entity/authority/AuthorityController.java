package com.puppyrush.buzzcloud.entity.authority;

import java.util.ArrayList;
import java.util.HashMap;
import org.springframework.stereotype.Service;

import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.enumController;
import com.puppyrush.buzzcloud.entity.authority.band.BandAuthority;
import com.puppyrush.buzzcloud.entity.authority.file.FileAuthority;
import com.puppyrush.buzzcloud.entity.authority.member.MemberAuthority;
import com.puppyrush.buzzcloud.entity.impl.EntityControllerImpl;
import com.puppyrush.buzzcloud.entity.interfaces.Entity;
import com.puppyrush.buzzcloud.page.PageException;
import com.puppyrush.buzzcloud.page.enums.enumPage;
import com.puppyrush.buzzcloud.page.enums.enumPageError;

@Service("authorityController")
final public class AuthorityController extends EntityControllerImpl<Authority>{

	private HashMap<Class<?>, HashMap<Integer,Integer>> authorityMap;
	private ArrayList<Class<?>> authorityClassAry;
	
	private int autoIncrementId; 
	
	private AuthorityController(){
		
		authorityMap = new HashMap<Class<?>, HashMap<Integer,Integer>>();
		authorityClassAry = new ArrayList<Class<?>>();
		
		authorityClassAry.add(BandAuthority.class);
		authorityClassAry.add(MemberAuthority.class);
		authorityClassAry.add(FileAuthority.class);

		for(int i=0 ; i < authorityClassAry.size() ; i++){
			System.out.println(authorityClassAry.get(i).getName());
			authorityMap.put(authorityClassAry.get(i), new HashMap<Integer,Integer>());
		}
		
		autoIncrementId = 0;
		
	}
	
	public <E extends Authority> E getEntity(Class<E> entityKind, int authId) throws ControllerException{
				
		E auth = null;
		
		final int entityId = authorityMap.get(entityKind).get(authId); 
		
		auth = (E) getEntity(entityId);
		
		return auth;	
	}
	
	@Override
	@Deprecated
	public Authority getEntity(int id) throws ControllerException {

		if(id<0)
			throw new IllegalArgumentException("userId는 0보다 커야 합니다.");
				
		if(entityMap.containsKey(id))
			return (Authority) entityMap.get(id);
		else
			throw (new ControllerException.Builder(enumPage.ERROR404))
			.instanceMessage("비 정상적인 접근입니다..")
			.errorCode(enumController.NOT_EXIST_MEMBER_FROM_MAP).build(); 		
	}
	
	public <E extends Authority> boolean containsEntity(Class<E> entityKind, int authId){
		
		HashMap<Integer,Integer> _entityMap = authorityMap.get(entityKind);
		
		if(_entityMap.containsKey(authId)==false){
			return false;
		}
		
		int entityId = _entityMap.get(authId);
		
		return containsEntity(entityId);

	}

	@Override
	@Deprecated
	public boolean containsEntity(int id) {
		return entityMap.containsKey(id);
		
	}
	
	public<E extends Authority>  void addEntity(E authority) throws ControllerException{
				
		Class<? extends Authority> clazz = authority.getClass();
		HashMap<Integer,Integer> _entityMap = authorityMap.get(clazz);
		
		if(_entityMap.containsKey(authority.getAuthorityId()))
			throw new IllegalArgumentException("already exist authObj");
				
		final int entityId = autoIncrementId++;
		
		_entityMap.put(authority.getAuthorityId(), entityId);
		
		addEntity(entityId, authority);
		
	}

	@Override
	@Deprecated
	public <V extends Entity> void addEntity(int id, V obj) throws ControllerException {
		
		if(entityMap.containsKey(obj))
			throw (new ControllerException.Builder(enumPage.ERROR404))
			.instanceMessage("비 정상적인 접근입니다.")
			.errorCode(enumController.ALREAY_EXIST_MEMBER_FROM_MAP).build(); 
			
		
		entityMap.put(id, (Authority) obj);
		
	}
	
	public <E extends Authority> void removeEntity(E authority) throws ControllerException{
		
		if(containsEntity((Class<E>) authority.getClass(), authority.getAuthorityId())==false){
			throw (new ControllerException.Builder(enumPage.ERROR404))
			.instanceMessage("비 정상적인 접근입니다.")
			.errorCode(enumController.NOT_EXIST_MEMBER_FROM_MAP).build(); 
		}
		
		Class<? extends Authority> clazz = authority.getClass();		
		
		final int entityId = authorityMap.get(clazz).remove(authority.getAuthorityId()); 
		removeEntity(entityId);
		
	}
	
	
	@Override
	@Deprecated
	public void removeEntity(int id) throws ControllerException {
		
		if(!entityMap.containsKey(id))
			throw (new ControllerException.Builder(enumPage.ERROR404))
			.instanceMessage("비 정상적인 접근입니다.")
			.errorCode(enumController.NOT_EXIST_MEMBER_FROM_MAP).build();		
		
		entityMap.remove(id);
		
	}
	
	
}


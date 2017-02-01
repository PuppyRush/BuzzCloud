package com.puppyrush.buzzcloud.entity.authority;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.impl.EntityControllerImpl;

@Service("authorityController")
final public class AuthorityController extends EntityControllerImpl<Authority>{

	private HashMap<Class<?>, HashMap<Integer,Integer>> authorityMap;
	private ArrayList<Class<?>> AuthorityClassAry;
	
	private int autoIncrementId; 
	
	private AuthorityController(){
		
		authorityMap = new HashMap<Class<?>, HashMap<Integer,Integer>>();
		AuthorityClassAry = new ArrayList<Class<?>>();
		Class<?> clazzs [] =  Authority.class.getClasses();
		
		for(int i=0 ; i < clazzs.length ; i++){
			AuthorityClassAry.add(clazzs[i]);
		}
		
		for(int i=0 ; i < AuthorityClassAry.size() ; i++){
			System.out.println(AuthorityClassAry.get(i).getName());
			authorityMap.put(AuthorityClassAry.get(i).getClass() , new HashMap<Integer,Integer>());
		}
		
		autoIncrementId = 0;
		
	}
	
	public <E extends Authority> E getEntity(Class<E> entityKind, int authId){
				
		E auth = null;
		HashMap<Integer,Integer> _entityMap =  authorityMap.get(entityKind.getClass());
		int entityId = _entityMap.get(authId);
		try {
			auth = (E) getEntity(entityId);
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return auth;	
	}
	
	public <E extends Authority> boolean containsEntity(Class<E> entityKind, int authId){
		
		HashMap<Integer,Integer> _entityMap = authorityMap.get(entityKind);
		int entityId = _entityMap.get(authId);
		
		return containsEntity(entityId);

	}

	@Deprecated
	public <E extends Authority> void addEntity(E authority, int authId){
		
		
		Class<? extends Authority> clazz = authority.getClass();
		HashMap<Integer,Integer> _entityMap = authorityMap.get(clazz);
		while(true){
			
			containsEntity(clazz, authId);
			
		}
		
	}

}


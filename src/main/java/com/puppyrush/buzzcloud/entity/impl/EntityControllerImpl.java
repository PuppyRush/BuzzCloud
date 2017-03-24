package com.puppyrush.buzzcloud.entity.impl;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.enumController;
import com.puppyrush.buzzcloud.entity.band.Band;
import com.puppyrush.buzzcloud.entity.band.BandController;
import com.puppyrush.buzzcloud.entity.interfaces.Entity;
import com.puppyrush.buzzcloud.entity.interfaces.EntityController;
import com.puppyrush.buzzcloud.entity.member.Member;
import com.puppyrush.buzzcloud.property.ConnectMysql;

public abstract class EntityControllerImpl<T extends Entity> implements EntityController{

	protected HashMap<Integer, T> entityMap = new HashMap<Integer, T>();
	protected Connection conn = ConnectMysql.getConnector();
	

	protected EntityControllerImpl() {
	}


	@Override
	public boolean containsEntity(int id) {
		return entityMap.containsKey(id);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getEntity(int id) throws ControllerException {

		if(id<=0)
			throw new IllegalArgumentException("userId는 0보다 커야 합니다.");
				
		if(entityMap.containsKey(id))
			return (T) entityMap.get(id);
		
		throw new ControllerException("비 정상적인 접근입니다.",enumController.NOT_EXIST_MEMBER_FROM_MAP);
		
	}

	@Override
	public <V extends Entity> void addEntity(int id, V obj) throws ControllerException {
		
		if(entityMap.containsKey(obj))
			throw new ControllerException(enumController.ALREAY_EXIST_MEMBER_FROM_MAP);
		
		entityMap.put(id, (T)obj);
		
	}

	@Override
	public void removeEntity(int id) throws ControllerException {
		
		if(!entityMap.containsKey(id))
			throw new ControllerException(enumController.NOT_EXIST_MEMBER_FROM_MAP);
		
		entityMap.remove(id);
		
	}
	
	@Override
	public void updateProperty(int id, String propertyName, Object newValue) {
		// TODO Auto-generated method stub
		
		try{
			
			if(!containsEntity(id)){
				return;
			}
			
			T obj = getEntity(id);
			Field field = obj.getClass().getDeclaredField(propertyName);
			field.setAccessible(true);
			Object value = field.get(obj);
						
			if(!newValue.getClass().isInstance(value)){
				throw new NoSuchFieldException("두 타입이 일치하지 않습니다.");
			}
			else{
				field.set(obj, newValue);				
			}
			
		}catch(NoSuchFieldException e){
			e.printStackTrace();
		}catch(ControllerException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}

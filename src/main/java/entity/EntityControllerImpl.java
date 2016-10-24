package entity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import entity.group.Group;
import entity.group.GroupController;
import entity.group.GroupController.Singleton;
import entity.member.Member;
import property.ConnectMysql;

public class EntityControllerImpl<T extends Entity> implements EntityController{

	protected HashMap<Integer, T> entityMap = new HashMap<Integer, T>();
	protected Connection conn = ConnectMysql.getConnector();
	
	protected EntityControllerImpl() {
	}
	

	@Override
	public boolean containsObject(int id) {
		return entityMap.containsKey(id);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Entity> T getObject(int id) throws ControllerException {

		if(id<=0)
			throw new IllegalArgumentException("userId는 0보다 커야 합니다.");
				
		if(entityMap.containsKey(id))
			return (T) entityMap.get(id);
		
		throw new ControllerException("비 정상적인 접근입니다.",enumController.NOT_EXIST_MEMBER_FROM_MAP);
		
		
	}

	@Override
	public <V extends Entity> void addObject(int id, V obj) throws ControllerException {
		
		if(entityMap.containsKey(obj))
			throw new ControllerException(enumController.ALREAY_EXIST_MEMBER_FROM_MAP);
		
		entityMap.put(id, (T) obj);
		
	}

	@Override
	public void removeObject(int id) throws ControllerException {
		
		if(!entityMap.containsKey(id))
			throw new ControllerException(enumController.NOT_EXIST_MEMBER_FROM_MAP);
		
		entityMap.remove(id);
		
	}
	
}

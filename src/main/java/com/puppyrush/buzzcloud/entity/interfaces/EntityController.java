package com.puppyrush.buzzcloud.entity.interfaces;

import com.puppyrush.buzzcloud.entity.ControllerException;

public interface EntityController {

	public boolean containsEntity(int id) ;
	public <T extends Entity> T getEntity(int id) throws ControllerException;
	public void removeEntity(int id) throws ControllerException;	
	public <T extends Entity> void addEntity(int id, T obj) throws ControllerException;
	public void updateProperty(int id, String propertyname, Object newValue);
	 
	
}

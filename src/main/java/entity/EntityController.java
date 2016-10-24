package entity;

public interface EntityController {

	public boolean containsObject(int id) ;
	public <T extends Entity> T getObject(int id) throws ControllerException;
	public void removeObject(int id) throws ControllerException;	
	public <T extends Entity> void addObject(int id, T obj) throws ControllerException;
	
}

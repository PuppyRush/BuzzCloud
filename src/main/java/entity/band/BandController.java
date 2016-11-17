package entity.band;




import entity.ControllerException;
import entity.EntityException;
import entity.impl.EntityControllerImpl;

public final class BandController extends EntityControllerImpl<Band>{

	private BandController(){}
	
	private static class Singleton {
		private static final BandController instance = new BandController();
	}
	
	public static BandController getInstance () {
		return Singleton.instance;
	}

	
	public Band newBand(int bandId){
		Band band = null;
		try{
			if(containsEntity(bandId))
				band = getEntity(bandId);
		
			else{
				band = BandManager.getInstance().getBand(bandId);						
				addEntity(bandId,band);
				 
			}
		}catch(ControllerException e){
			e.printStackTrace();
		} catch (EntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(band==null)
			throw new NullPointerException();
		
		return band;
	}
	


}

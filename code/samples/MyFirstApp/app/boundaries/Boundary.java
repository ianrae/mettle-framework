package boundaries;

import mef.core.Initializer;

import org.mef.framework.sfx.SfxContext;

import play.Logger;

import boundaries.dals.PhoneDAO;
import boundaries.dals.TaskDAO;
import boundaries.dals.UserDAO;

public class Boundary 
{
	public static SfxContext theCtx;
	
	static void init()
	{
		if (theCtx == null)
		{
			theCtx = Initializer.createContext(false); //fix later!!
			Logger.info("==seed==");
			Initializer.loadSeedData(theCtx);
//			Logger.info("==seed!1!==");
//			Initializer.loadSeedData(theCtx);
		}
	}
	
}

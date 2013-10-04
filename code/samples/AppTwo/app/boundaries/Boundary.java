package boundaries;

import mef.core.Initializer;
import org.mef.framework.sfx.SfxContext;
import play.Logger;

public class Boundary 
{
	public static SfxContext theCtx;
	
	public static void init()
	{
		if (theCtx == null)
		{
			theCtx = Initializer.createContext(false); //fix later!!
			Logger.info("==seed==");
			Initializer.loadSeedData(theCtx);
		}
	}
	
}

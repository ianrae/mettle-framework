package boundaries;

import mef.core.Initializer;

import org.mef.framework.sfx.SfxContext;

import boundaries.dals.PhoneDAL;
import boundaries.dals.TaskDAL;
import boundaries.dals.UserDAL;

public class Boundary 
{
	public static SfxContext theCtx;
	
	private static void init()
	{
		if (theCtx == null)
		{
			theCtx = Initializer.createContext(new TaskDAL(), new UserDAL(), new PhoneDAL()); //fix later!!
		}
	}
	
	public static ApplicationBoundary createApplicationBoundary()
	{
		init();
		return new ApplicationBoundary(theCtx);
	}
	public static UserBoundary createUserBoundary() 
	{
		init();
		return new UserBoundary(theCtx);
	}
	
	

	
}

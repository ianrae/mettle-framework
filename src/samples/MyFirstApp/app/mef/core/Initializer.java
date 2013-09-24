package mef.core;

import org.mef.framework.sfx.SfxContext;

import mef.dals.ITaskDAL;
import mef.dals.IUserDAL;

public class Initializer 
{
	public static SfxContext createContext(ITaskDAL dal, IUserDAL userDAL)
	{
		SfxContext ctx = new SfxContext();
		ctx.getServiceLocator().registerSingleton(ITaskDAL.class, dal);
		ctx.getServiceLocator().registerSingleton(IUserDAL.class, userDAL);
		return ctx;
	}

}

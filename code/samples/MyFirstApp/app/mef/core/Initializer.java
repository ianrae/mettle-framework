package mef.core;

import org.mef.framework.sfx.SfxContext;

import mef.dals.IPhoneDAL;
import mef.dals.ITaskDAL;
import mef.dals.IUserDAL;

public class Initializer 
{
	public static SfxContext createContext(ITaskDAL dal, IUserDAL userDAL, IPhoneDAL phoneDAL)
	{
		SfxContext ctx = new SfxContext();
		ctx.getServiceLocator().registerSingleton(ITaskDAL.class, dal);
		ctx.getServiceLocator().registerSingleton(IUserDAL.class, userDAL);
		ctx.getServiceLocator().registerSingleton(IPhoneDAL.class, phoneDAL);
		return ctx;
	}

}

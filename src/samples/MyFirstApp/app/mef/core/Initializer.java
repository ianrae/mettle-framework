package mef.core;

import org.mef.framework.sfx.SfxContext;

import mef.dals.ITaskDAL;

public class Initializer 
{
	public static SfxContext createContext(ITaskDAL dal)
	{
		SfxContext ctx = new SfxContext();
		ctx.getServiceLocator().registerSingleton(ITaskDAL.class, dal);
		return ctx;
	}

}

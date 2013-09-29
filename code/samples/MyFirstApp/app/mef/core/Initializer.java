package mef.core;

import org.mef.framework.sfx.SfxContext;

import mef.dals.IPhoneDAL;
import mef.dals.ITaskDAL;
import mef.dals.IUserDAL;
import mef.entities.utils.ResourceReader;

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
	
	public static void loadSeedData(SfxContext ctx)
	{
		String json = ResourceReader.readSeedFile("json-user1.txt");
		EntityLoader loader = new EntityLoader(ctx);
		try {
			loader.loadUser(json);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

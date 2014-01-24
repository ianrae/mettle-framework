package mef.core;

//uncomment after you create your first Entity in mef.xml
//import mef.gen.AllKnownDAOs_GEN;

import org.mef.framework.dao.IDAO;
import org.mef.framework.sfx.IServiceFactory;
import org.mef.framework.sfx.SfxContext;
import org.mef.framework.sfx.SfxErrorTracker;

public class MettleInitializer 
{
	public static SfxContext theCtx;
	
	public MettleInitializer()
	{}
	
	public SfxContext createContext()
	{
		theCtx = new SfxContext();
		return theCtx;
	}
	
	//called from global
	public void onStart()
	{
		SfxContext ctx = theCtx;
		registerIfNotAlready(SfxErrorTracker.class, new SfxErrorTracker(ctx));
		
		String var = ctx.getVar("UNITTEST");
		boolean createMocks = (var != null);
//		AllKnownDAOs_GEN knownDAOs = new AllKnownDAOs_GEN();
//		knownDAOs.registerDAOs(ctx, createMocks);
		
		//load seed data
		loadSeedData(); //move to separate loader registered...
	}
	
	private void loadSeedData()
	{
//		String json = ResourceReader.readSeedFile("json-user1.txt");
//		EntityLoader loader = new EntityLoader(ctx);
//		try {
//			loader.loadUser(json);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	@SuppressWarnings("rawtypes")
	public void registerSingleton(Class clazz, Object obj)
	{
		theCtx.getServiceLocator().registerSingleton(clazz, obj);
	}
	@SuppressWarnings("rawtypes")
	public void registerFactory(Class clazz, IServiceFactory factory)
	{
		theCtx.getServiceLocator().registerFactory(clazz, factory);
	}
	
	public static IDAO getDAO(Class clazz)
	{
		IDAO dao = (IDAO) theCtx.getServiceLocator().getInstance(clazz);
		return dao;
	}
	
	
	//-- helpers --
	@SuppressWarnings({ "rawtypes", "unused" })
	private void registerIfNotAlready(Class clazz, Object obj)
	{
		if (theCtx.getServiceLocator().getInstance(clazz) == null)
		{
			this.registerSingleton(clazz, obj);
		}
	}
	@SuppressWarnings({ "rawtypes", "unused" })
	private void registerFactoryIfNotAlready(Class clazz, IServiceFactory factory)
	{
		if (theCtx.getServiceLocator().getFactory(clazz) == null)
		{
			this.registerFactory(clazz, factory);
		}
	}

}

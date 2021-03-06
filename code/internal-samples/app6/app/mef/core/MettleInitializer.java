package mef.core;

//uncomment after you create your first Entity in mef.xml
//import mef.gen.AllKnownDAOs_GEN;

import org.mef.framework.dao.IDAO;
import org.mef.framework.fluent.ProcRegistry;
import org.mef.framework.sfx.IServiceFactory;
import org.mef.framework.sfx.SfxContext;
import org.mef.framework.sfx.SfxErrorTracker;


public class MettleInitializer 
{
	public static SfxContext theCtx;
	public static String appPath;
	
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

		//not needed
//		Logger.info("appPath: " + appPath);
//		ctx.setVar("appPath", appPath);
		
		registerIfNotAlready(SfxErrorTracker.class, new SfxErrorTracker(ctx));
		
		String var = ctx.getVar("UNITTEST");
		boolean createMocks = (var != null);
		
		initProcRegistry();
		
		
		//UNCOMMENT these two lines after you add first entity in mef.xml
		AllKnownDAOs knownDAOs = new AllKnownDAOs();
		knownDAOs.registerDAOs(ctx, createMocks);
		
		//load seed data
		loadSeedData(); //move to separate loader registered...
	}
	
	protected ProcRegistry initProcRegistry()
	{
		ProcRegistry registry = new ProcRegistry();
		theCtx.getServiceLocator().registerSingleton(ProcRegistry.class, registry);
		return registry;
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

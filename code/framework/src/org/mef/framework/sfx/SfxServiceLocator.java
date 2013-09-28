package org.mef.framework.sfx;

import java.util.HashMap;


/*
 * A simple code-based service locator. No xml config file, everything done in code.
 */
public class SfxServiceLocator
{
//	private static SfxServiceLocator theSingleton;
	
	//member vars
	private HashMap<Class, Object> singletonMap; //map of singleton objects.
	private HashMap<Class, IServiceFactory> factoryMap; //map of factory objects, when getInstance needs to return a new object each time
	
	public SfxServiceLocator()
	{
		this.singletonMap = new HashMap<Class, Object>();
		this.factoryMap = new HashMap<Class, IServiceFactory>();
	}
	
	/*
	 * register a singleton object for a given interface.
	 * Only one object per interface class can be registered at a time.
	 */
	public void registerSingleton(Class interfaceClass, Object impl)
	{
		this.singletonMap.put(interfaceClass, impl);
	}
	
	/*
	 * register a factory the a interface.
	 */
	public void registerFactory(Class interfaceClass, IServiceFactory factory)
	{
		this.factoryMap.put(interfaceClass, factory);
	}

	/*
	 * Get an instance of the required interface.  
	 * For now is a singleton object, but later we'll support factories
	 */
	public Object getInstance(Class interfaceClass) 
	{
		IServiceFactory factory = this.factoryMap.get(interfaceClass);
		if (factory != null)
		{
			return factory.create(interfaceClass);
		}
		return this.singletonMap.get(interfaceClass);
	}

	//unit tests sometimes need to get the factory and adjust it
	public IServiceFactory getFactory(Class interfaceClass) 
	{
		IServiceFactory factory = this.factoryMap.get(interfaceClass);
		return factory;
	}

//	/*
//	 * Example of a typesafe get method.
//	 */
//	public ISfxLogger getLogger()
//	{
//		return (ISfxLogger) getInstance(ISfxLogger.class);
//	}
//
//	public static SfxServiceLocator getSingleton() 
//	{
//		if (theSingleton == null)
//		{
//			theSingleton = new SfxServiceLocator();
//			theSingleton.init();
//		}
//		return theSingleton;
//	}
//	public static void destroySingleton() 
//	{
//		if (theSingleton != null)
//		{
//			theSingleton = null;
//		}
//	}
//	
//	private void init()
//	{
//		//put all code-based init here
//		registerSingleton(ISfxLogger.class, new SfxLogger());
//	}
	
	
}
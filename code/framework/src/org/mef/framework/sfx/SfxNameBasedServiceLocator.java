package org.mef.framework.sfx;

import java.util.HashMap;


/*
 * A simple code-based service locator. No xml config file, everything done in code.
 */
public class SfxNameBasedServiceLocator
{
	
	//member vars
	private HashMap<String, Object> serviceMap;
	
	public SfxNameBasedServiceLocator()
	{
		this.serviceMap = new HashMap<String, Object>();
	}
	
	/*
	 * register a singleton object for a given interface.
	 * Only one object per interface class can be registered at a time.
	 */
	public void registerSingleton(String key, Object impl)
	{
		this.serviceMap.put(key, impl);
	}

	/*
	 * Get an instance of the required interface.  
	 * For now is a singleton object, but later we'll support factories
	 */
	public Object getInstance(String key) 
	{
		return this.serviceMap.get(key);
	}

	
}
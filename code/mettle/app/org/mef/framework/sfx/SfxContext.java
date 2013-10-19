package org.mef.framework.sfx;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * We use a form of dependency injection, in order to avoid singletons.
 * SfxContext is an object that contains all the useful common objects needed by the system.
 * 
 * It is passed into the constructor of (nearly) all new objects.
 *
 */
public class SfxContext {
	private HashMap<String,String> _varMap = new HashMap<String,String>();
	private HashMap<String,Object> _objMap = new HashMap<String,Object>();
	
	protected SfxServiceLocator _loc;
	private ISfxLogger _logger;

	public SfxContext()
	{
		
		_loc = new SfxServiceLocator();
		_loc.registerSingleton(ISfxLogger.class, new SfxLogger());

		_logger = (ISfxLogger) _loc.getInstance(ISfxLogger.class);
	}
	
	public ISfxLogger getLogger()
	{
		return _logger;
	}

	public void log(String msg)
	{
		_logger.log(msg);
	}
	public SfxServiceLocator getServiceLocator()
	{
		return _loc;
	}
	
	//key-value pairs
	public String getVar(String varName)
	{
		String value = doGetVar(varName);
		getLogger().logDebug("GET: " + varName + ": " + value);
		return value;
	}
	public String getVarNoLog(String varName)
	{
		String value = doGetVar(varName);
		return value;
	}
	private String doGetVar(String varName)
	{
		String value =_varMap.get(varName);
		return value;
	}
	public int getVarInt(String varName)
	{
		return Integer.parseInt(getVar(varName));
	}
	public boolean getVarBool(String varName)
	{
		String s = getVar(varName);
		return s.equalsIgnoreCase("true");
	}
	public void setVar(String varName, String value)
	{
		doSetVar(varName, value);
		log("SET: " + varName + ": " + value);
	}
	//no logging
	private void doSetVar(String varName, String value)
	{
		_varMap.put(varName, value);
	}

	public Object getObj(String objName)
	{
		return _objMap.get(objName);
	}
	public void setObj(String objName, Object obj)
	{
		_objMap.put(objName, obj);
	}
	
	public void Dump()
	{
		for (String entry : _varMap.keySet()) 
		{
			log(" " + entry + ": " + _varMap.get(entry));
		}
	}
	
	public String findVar(String target, String defaultValue)
	{
		for (String entry : _varMap.keySet()) 
		{
			if (entry.equalsIgnoreCase(target))
			{
				return entry; 
			}
		}
		return defaultValue;
	}
	
	public void copyFrom(SfxContext other)
	{
		for (String entry : other._varMap.keySet()) 
		{
			doSetVar(entry, other._varMap.get(entry));
		}
	
		for (String entry : other._objMap.keySet()) 
		{
			setObj(entry, other._objMap.get(entry));
		}
	}
}

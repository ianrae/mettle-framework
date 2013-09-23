package org.mef.framework.sfx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SfxParamObj extends SfxBaseObj
{
	private HashMap<String,String> _varMap = new HashMap<String,String>();
	private HashMap<String,Object> _objMap = new HashMap<String,Object>();
	private boolean logGetsAndSets = false;

	public SfxParamObj(SfxContext ctx)
	{
		super(ctx);
	}
	
	public void enableLogging()
	{
		logGetsAndSets = true;
	}
	
	//key-value pairs
	public String getVar(String varName)
	{
		String value = doGetVar(varName);
		if (logGetsAndSets)
		{
			logDebug("GET: " + varName + ": " + value);
		}
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
		if (logGetsAndSets)
		{
			log("SET: " + varName + ": " + value);
		}
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
	
	public List<String> getSortedKeys()
	{
		List<String> L = new ArrayList(_varMap.keySet());
		Collections.sort(L);
		return L;
	}
	
	public String dumpToString() throws Exception
	{
		SfxTrail trail = new SfxTrail();
		List<String> keys = getSortedKeys();
		for(String key : keys)
		{
			trail.add(String.format("%s:%s", key, getVar(key)));
		}
		return trail.getTrail();
	}
	
	
//	public void copyFrom(SfxContext other)
//	{
//		for (String entry : other._varMap.keySet()) 
//		{
//			doSetVar(entry, other._varMap.get(entry));
//		}
//	
//		for (String entry : other._objMap.keySet()) 
//		{
//			setObj(entry, other._objMap.get(entry));
//		}
//	}

}

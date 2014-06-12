package org.mef.framework.loaders.sprig;

import java.util.Date;
import java.util.Map;

import org.mef.sprig.SprigLoader;

public class EntityDataLoader<T> implements SprigLoader<T>
{
	private Class<T> clazz;
	
	public EntityDataLoader(Class clazz)
	{
		this.clazz = clazz;
	}
	@Override
	public Class<T> getClassBeingLoaded() 
	{
		return clazz;
	}

	@Override
	public void parse(T arg0, Map<String, Object> arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resolve(T arg0, String arg1, Object arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save(T arg0) {
		// TODO Auto-generated method stub
		
	}

	
	protected String getString(Map<String,Object> map, String name)
	{
		String s = (String) map.get(name);
		return s;
	}
	protected Integer getInt(Map<String,Object> map, String name)
	{
		Integer n = (Integer) map.get(name);
		return n;
	}
	protected Long getLong(Map<String,Object> map, String name)
	{
		Long n = 0L;
		Object obj = map.get(name);
		if (obj instanceof Integer)
		{
			Integer intVal = (Integer)obj;
			n = new Long(intVal);
		}
		else
		{
			n = (Long) map.get(name);
		}
		return n;
	}
	protected Boolean getBoolean(Map<String,Object> map, String name)
	{
		Boolean b = (Boolean) map.get(name);
		return b;
	}
	protected Date getDate(Map<String,Object> map, String name)
	{
		Long n = Long.parseLong((String) map.get(name));
		Date dt = new Date(n);
		return dt;
	}
	protected Character getChar(Map<String,Object> map, String name)
	{
		String s = (String)map.get(name);
		Character ch = (s.isEmpty()) ? 0 : s.charAt(0);
		return ch;
	}
	
}

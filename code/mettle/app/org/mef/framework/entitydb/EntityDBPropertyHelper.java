package org.mef.framework.entitydb;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;


import static org.reflections.ReflectionUtils.*;


public class EntityDBPropertyHelper<T> 
{
	public boolean debug;


	public Object getPropertyValue(T obj, String fieldName) 
	{
		if (fieldName == null)
		{
			return null;
		}
		if (obj == null)
		{
			return null;
		}

		Method prop = null;
		Object value = getProperty(obj, prop, fieldName);

		return value;
	}
	public Class getPropertyType(T obj, String fieldName) 
	{
		if (fieldName == null)
		{
			return null;
		}
		if (obj == null)
		{
			return null;
		}

		Method m = getGetter(obj, fieldName);
		return m.getReturnType();
	}
	private Object getProperty(Object obj, Method field, String fieldName)
	{
		Object value = null;

		Method m = getGetter(obj, fieldName);
		if (m != null)
		{
			try 
			{
				value = m.invoke(obj);
			} catch (IllegalArgumentException e) 
			{
				if (debug)
				{
					e.printStackTrace();
				}
			} catch (IllegalAccessException e) 
			{
				if (debug)
				{
					e.printStackTrace();
				}
			} catch (InvocationTargetException e) 
			{
				if (debug)
				{
					e.printStackTrace();
				}
			}
		}

		//			field = obj.getClass().getDeclaredMethod(fieldName); //all members of this class
		//			field.setAccessible(true);
		//			value = field.get(obj);

		return value;
	}

	private Method getGetter(Object obj, String fieldName)
	{
		Set<Method> list = getAllMethods(obj.getClass(), withPrefix("get"));
		String targetName = "get" + uppify(fieldName);

		for(Method m : list)
		{
			if (m.getName().equals(targetName))
			{
				return m;
			}
		}
		return null;
	}

	private String uppify(String name) 
	{
		String upper = name.toUpperCase();
		String s = upper.substring(0, 1);
		s += name.substring(1);
		return s;
	}

}

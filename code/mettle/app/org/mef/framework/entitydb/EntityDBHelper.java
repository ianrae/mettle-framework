package org.mef.framework.entitydb;

import java.lang.reflect.Field;

public class EntityDBHelper<T> 
{
	public boolean debug;
	
	
	public Object getFieldValue(T obj, String fieldName) 
	{
		if (fieldName == null)
		{
			return false;
		}
		
		Field field = null;
		Object value = getDeclaredField(obj, field, fieldName);
		if (value == null)
		{
			value = getField(obj, field, fieldName);
		}
		
		return value;
	}
	private Object getDeclaredField(Object obj, Field field, String fieldName)
	{
		Object value = null;
		try 
		{
//			Class cz = obj.getClass();
			
			field = obj.getClass().getDeclaredField(fieldName); //all members of this class
			field.setAccessible(true);
			value = field.get(obj);
		}
		catch (SecurityException e) 
		{
			if (debug)
			{
				e.printStackTrace();
			}
		} 
		catch (NoSuchFieldException e) 
		{
			if (debug)
			{
				e.printStackTrace();
			}
		} catch (IllegalArgumentException e) 
		{
			if (debug)
			{
				e.printStackTrace();
			}
		} 
		catch (IllegalAccessException e) 
		{
			if (debug)
			{
				e.printStackTrace();
			}
		}

		return value;
	}
	private Object getField(Object obj, Field field, String fieldName)
	{
		Object value = null;
		try 
		{
//			Class cz = obj.getClass();
			
			field = obj.getClass().getField(fieldName); //declared or inherited publics
			field.setAccessible(true);
			value = field.get(obj);
		}
		catch (SecurityException e) 
		{
			if (debug)
			{
				e.printStackTrace();
			}
		} 
		catch (NoSuchFieldException e) 
		{
			if (debug)
			{
				e.printStackTrace();
			}
		} catch (IllegalArgumentException e) 
		{
			if (debug)
			{
				e.printStackTrace();
			}
		} 
		catch (IllegalAccessException e) 
		{
			if (debug)
			{
				e.printStackTrace();
			}
		}

		return value;
	}

}

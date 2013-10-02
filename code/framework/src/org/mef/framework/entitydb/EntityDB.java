package org.mef.framework.entitydb;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

//Whole idea is we don't need a fully emulated sql db like H2.
//(a)we are dealing with objects (which can be assumed to be fully eagerly loaded)
//(b)Mock DAL has the actual objects, and doesn't make copies, so object instances
//   are unique. Never would get two object with same .id.



public class EntityDB<T>
	{
		//hmm should union just work in whole objects. If same object (Flight 55) in both
		//lists, shouldn't result only be in result once!!
		public List<T> union(List<T> L, List<T> L2) 
		{
			ArrayList<T> L3 = new ArrayList<T>();
			for(T f : L)
			{
				L3.add(f);
			}
			for(T f : L2)
			{
				if (! L3.contains(f))
				{
					L3.add(f);
				}
			}
			
			return L3;
		}
		
		public List<T> intersection(List<T> L, List<T> L2) 
		{
			ArrayList<T> L3 = new ArrayList<T>();
			for(T f : L)
			{
				if (L2.contains(f))
				{
					L3.add(f);
				}
			}
			
			return L3;
		}
		
		public boolean isMatchStr(T obj, String fieldName, String valueToMatch) 
		{
			if (fieldName == null)
			{
				return false;
			}
			
			Object value = getFieldValue(obj, fieldName);
			if (value == null)
			{
				return false;
			}
			String s = value.toString();
			return (s.equalsIgnoreCase(valueToMatch));
		}
		public boolean isMatchInt(T obj, String fieldName, Integer valueToMatch) 
		{
			if (fieldName == null)
			{
				return false;
			}
			
			Object value = getFieldValue(obj, fieldName);
			if (value == null)
			{
				return false;
			}
			
			if (value instanceof Integer)
			{
				Integer n = (Integer)value;
				return (n.compareTo(valueToMatch) == 0);
			}
			else
			{
				return false;
			}
		}


		public Object getFieldValue(T obj, String fieldName) 
		{
			if (fieldName == null)
			{
				return false;
			}
			
			Field field = null;
			Object value = null;
			try 
			{
				field = obj.getClass().getDeclaredField(fieldName);
				field.setAccessible(true);
				value = field.get(obj);
			}
			catch (SecurityException e) 
			{
				// TODO Auto-generated catch block
//				e.printStackTrace();
			} 
			catch (NoSuchFieldException e) 
			{
				// TODO Auto-generated catch block
//				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			}

			return value;
		}
		
		public T findFirstMatch(List<T> L, String fieldName, String valueToMatch) 
		{
			for(T f : L)
			{
				if (isMatchStr(f, fieldName, valueToMatch))
				{
					return f;
				}
			}
			return null;
		}
	}
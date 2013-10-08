package org.mef.framework.entitydb;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;

//Whole idea is we don't need a fully emulated sql db like H2.
//(a)we are dealing with objects (which can be assumed to be fully eagerly loaded)
//(b)Mock DAO has the actual objects, and doesn't make copies, so object instances
//   are unique. Never would get two object with same .id.



public class EntityDB<T>
	{
		public boolean debug = false;
	
		HashMap<Class, IValueMatcher> matcherMap = new HashMap<Class, IValueMatcher>();
		
		
		public EntityDB()
		{
			matcherMap.put(String.class, new StringValueMatcher());
			matcherMap.put(Integer.class, new IntegerValueMatcher());
			matcherMap.put(Long.class, new LongValueMatcher());
			
		}
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
		
		public boolean isMatchz(T obj, String fieldName, Object valueToMatch, Class clazz)
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
			
			IValueMatcher matcher = matcherMap.get(clazz);
			if (matcher != null)
			{
				return matcher.isMatch(value, valueToMatch);
			}
			else
			{
				throw new NotImplementedException();
			}
			
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
		public boolean isMatchLike(T obj, String fieldName, String valueToMatch) 
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
			
			String target = valueToMatch.replace("%", "");
			return (s.indexOf(target) >= 0);
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
		public boolean isMatchLong(T obj, String fieldName, Long valueToMatch) 
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
			
			if (value instanceof Long)
			{
				Long n = (Long)value;
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
//				Class cz = obj.getClass();
				
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
//				Class cz = obj.getClass();
				
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
		
		public T findFirstMatch(List<T> L, String fieldName, String valueToMatch) 
		{
			for(T f : L)
			{
				if (isMatchz(f, fieldName, valueToMatch, String.class))
				{
					return f;
				}
			}
			return null;
		}

		public T findFirstMatch(List<T> L, String fieldName, Integer valueToMatch) 
		{
			for(T f : L)
			{
				if (isMatchz(f, fieldName, valueToMatch, Integer.class))
				{
					return f;
				}
			}
			return null;
		}

		public T findFirstMatch(List<T> L, String fieldName, Long valueToMatch) 
		{
			for(T f : L)
			{
				if (isMatchz(f, fieldName, valueToMatch, Long.class))
				{
					return f;
				}
			}
			return null;
		}

		public List<T> findMatches(List<T> L, String fieldName, String valueToMatch)
		{
			List<T> resultL = new ArrayList<T>();
			
			for(T f : L)
			{
				if (isMatchStr(f, fieldName, valueToMatch))
				{
					resultL.add(f);
				}
			}
			return resultL;
		}
		public List<T> findMatches(List<T> L, String fieldName, Integer valueToMatch)
		{
			List<T> resultL = new ArrayList<T>();
			
			for(T f : L)
			{
				if (isMatchInt(f, fieldName, valueToMatch))
				{
					resultL.add(f);
				}
			}
			return resultL;
		}
		public List<T> findMatches(List<T> L, String fieldName, Long valueToMatch)
		{
			List<T> resultL = new ArrayList<T>();
			
			for(T f : L)
			{
				if (isMatchLong(f, fieldName, valueToMatch))
				{
					resultL.add(f);
				}
			}
			return resultL;
		}
	}
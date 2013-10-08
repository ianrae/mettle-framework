package org.mef.framework.entitydb;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
		
		public boolean isMatchObject(T obj, String fieldName, Object valueToMatch, Class clazz, int matchType)
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
			
			IValueMatcher matcher = this.getMatcher(clazz);
			return matcher.isMatch(value, valueToMatch, matchType);
		}
		
		private IValueMatcher getMatcher(Class clazz)
		{
			IValueMatcher matcher = matcherMap.get(clazz);
			if (matcher != null)
			{
				return matcher;
			}
			else
			{
				throw new NotImplementedException();
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
			return findFirstMatch(L, fieldName, valueToMatch, IValueMatcher.EXACT);
		}
		public T findFirstMatch(List<T> L, String fieldName, String valueToMatch, int matchType) 
		{
			for(T f : L)
			{
				if (isMatchObject(f, fieldName, valueToMatch, String.class, matchType))
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
				if (isMatchObject(f, fieldName, valueToMatch, Integer.class, IValueMatcher.EXACT))
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
				if (isMatchObject(f, fieldName, valueToMatch, Long.class, IValueMatcher.EXACT))
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
				if (isMatchObject(f, fieldName, valueToMatch, String.class, IValueMatcher.EXACT))
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
				if (isMatchObject(f, fieldName, valueToMatch, Integer.class, IValueMatcher.EXACT))
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
				if (isMatchObject(f, fieldName, valueToMatch, Long.class, IValueMatcher.EXACT))
				{
					resultL.add(f);
				}
			}
			return resultL;
		}
		
		private boolean isAscending(String orderBy)
		{
			return orderBy.equalsIgnoreCase(orderBy);
		}
		
		public List<T> orderBy(List<T> L, String fieldName, String orderBy, Class clazz)
		{
			boolean ascending = isAscending(orderBy);
			IValueMatcher matcher = this.getMatcher(clazz);
			ValueComparator<T> comparator = new ValueComparator<T>(this, fieldName, ascending, matcher, IValueMatcher.EXACT);
            Collections.sort(L, comparator);
			return L;
		}
	}
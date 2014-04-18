package org.mef.framework.entitydb;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.mef.framework.entities.Entity;
import org.mef.framework.fluent.FluentException;

//Whole idea is we don't need a fully emulated sql db like H2.
//(a)we are dealing with objects (which can be assumed to be fully eagerly loaded)
//(b)Mock DAO has the actual objects, and doesn't make copies, so object instances
//   are unique. Never would get two object with same .id.



public class EntityDB<T>
	{
		public boolean debug = false;
//		EntityDBHelper<T> helper;
		EntityDBPropertyHelper<T> helper;
	
		HashMap<Class, IValueMatcher> matcherMap = new HashMap<Class, IValueMatcher>();
		
		
		public EntityDB()
		{
			matcherMap.put(String.class, new StringValueMatcher());
			matcherMap.put(Integer.class, new IntegerValueMatcher());
			matcherMap.put(Long.class, new LongValueMatcher());
			matcherMap.put(Entity.class, new EntityValueMatcher());
			
			helper = new EntityDBPropertyHelper<T>();
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
			if (fieldName == null || fieldName.isEmpty())
			{
				return false;
			}
			
			Object value = helper.getPropertyValue(obj, fieldName);
			if (value == null)
			{
				return (valueToMatch == null);
			}
			
			IValueMatcher matcher = this.getMatcher(clazz);
			int res;
			boolean isMatch = false;
			
			switch(matchType)
			{
			case IValueMatcher.LT:
				res = matcher.compare(value, valueToMatch, matchType);
				isMatch = (res < 0);
				break;
			case IValueMatcher.LE:
				res = matcher.compare(value, valueToMatch, matchType);
				isMatch = (res <= 0);
				break;
				
			case IValueMatcher.GT:
				res = matcher.compare(value, valueToMatch, matchType);
				isMatch = (res > 0);
				break;
				
			case IValueMatcher.GE:
				res = matcher.compare(value, valueToMatch, matchType);
				isMatch = (res >= 0);
				break;
				
			case IValueMatcher.NEQ:
				res = matcher.compare(value, valueToMatch, matchType);
				isMatch = (res != 0);
				break;
				
			case IValueMatcher.CASE_INSENSITIVE:
			case IValueMatcher.EXACT:
			case IValueMatcher.LIKE:
			case IValueMatcher.ILIKE:
				isMatch = matcher.isMatch(value, valueToMatch, matchType);
				break;
				
			default:
				throw new FluentException(String.format("Unsupported match type: %d", matchType));
			}
			
			return isMatch;
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
		public T findFirstMatchEntity(List<T> L, String fieldName, Entity valueToMatch) 
		{
			for(T f : L)
			{
				if (isMatchObject(f, fieldName, valueToMatch, Entity.class, IValueMatcher.EXACT))
				{
					return f;
				}
			}
			return null;
		}
		

		public List<T> findMatches(List<T> L, String fieldName, String valueToMatch)
		{
			return findMatches(L, fieldName, valueToMatch, IValueMatcher.EXACT);
		}
		
		public List<T> findMatches(List<T> L, String fieldName, String valueToMatch, int matchType)
		{
			List<T> resultL = new ArrayList<T>();
			
			for(T f : L)
			{
				if (isMatchObject(f, fieldName, valueToMatch, String.class, matchType))
				{
					resultL.add(f);
				}
			}
			return resultL;
		}
		public List<T> findMatchesEntity(List<T> L, String fieldName, Entity valueToMatch)
		{
			List<T> resultL = new ArrayList<T>();
			
			for(T f : L)
			{
				if (isMatchObject(f, fieldName, valueToMatch, Entity.class, IValueMatcher.EXACT))
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
		
		public List<T> findCompareMatches(List<T> L, String fieldName, Object valueToMatch, Class clazz, int matchType)
		{
			List<T> resultL = new ArrayList<T>();
			
			for(T f : L)
			{
				if (isMatchObject(f, fieldName, valueToMatch, clazz, matchType))
				{
					resultL.add(f);
				}
			}
			return resultL;
		}
		
		
		private boolean isAscending(String orderBy)
		{
			return orderBy.equalsIgnoreCase("asc");
		}
		
		public List<T> orderBy(List<T> L, String fieldName, String orderBy, Class clazz)
		{
			boolean ascending = isAscending(orderBy);
			IValueMatcher matcher = this.getMatcher(clazz);
			ValueComparator<T> comparator = new ValueComparator<T>(this, fieldName, ascending, matcher, IValueMatcher.EXACT);
            Collections.sort(L, comparator);
			return L;
		}
		
		public Class getFieldType(List<T> L, String fieldName) 
		{
			for(T f : L)
			{
				Class clazz = helper.getPropertyType(f, fieldName);
				return clazz;
			}
			return null;
		}
		
	}
package org.mef.framework.entitydb;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang.NotImplementedException;
import org.mef.framework.entities.Entity;

public class EntityValueMatcher implements IValueMatcher
{
	private EntityDBHelper<Entity> helper = new EntityDBHelper<Entity>();
	
	@Override
	public boolean isMatch(Object value, Object valueToMatch, int matchType)
	{
		Entity s1 = (Entity)value;
		Entity s2 = (Entity) valueToMatch;
		
		if (s1 == s2) //same object?
		{
			return true;
		}
		
		Long lval1 = (Long) helper.getFieldValue(s1,  "id");// this.getId(s1);
		Long lval2 = (Long) helper.getFieldValue(s2,  "id");// this.getId(s1);
		
		if (lval1 == null || lval2 == null)
		{
			return false;
		}
		
		switch(matchType)
		{
		case IValueMatcher.EXACT:
				return lval1.longValue() == lval2.longValue();
//		case IValueMatcher.CASE_INSENSITIVE:
//			return s1.equalsIgnoreCase(s2);
//			
//		case IValueMatcher.LIKE:
//			return like(s1, s2);
//			
//		case IValueMatcher.ILIKE:
//			return ilike(s1, s2);
				
		default:
			throw new NotImplementedException();
		}
	}

//	private Long getId(Entity entity)
//	{
//		Method meth = null;
//		try {
//			meth = entity.getClass().getDeclaredMethod("getId", null);
//		} catch (SecurityException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (NoSuchMethodException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//		
//		if (meth == null)
//		{
//			throw new RuntimeException("getId not found!");
//		}
//		
//		Long lval = null;
//		try {
//			lval = (Long)meth.invoke(entity, null);
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return lval;
//	}
	
	@Override
	public int compare(Object value, Object valueToMatch, int matchType) 
	{
		//fix later!!
		if (isMatch(value, valueToMatch, matchType))
		{
			return 0;
		}
		else
		{
			return -1;
		}
	}	
}

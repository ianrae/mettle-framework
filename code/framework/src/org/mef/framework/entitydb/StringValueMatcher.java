package org.mef.framework.entitydb;

import org.apache.commons.lang.NotImplementedException;

public class StringValueMatcher implements IValueMatcher
{

	@Override
	public boolean isMatch(Object value, Object valueToMatch, int matchType)
	{
		String s1 = (String)value;
		String s2 = (String) valueToMatch;
		
		switch(matchType)
		{
		case IValueMatcher.EXACT:
				return s1.equals(s2);
		case IValueMatcher.CASE_INSENSITIVE:
			return s1.equalsIgnoreCase(s2);
			
		case IValueMatcher.LIKE:
			return like(s1, s2);
			
		case IValueMatcher.ILIKE:
			return ilike(s1, s2);
				
		default:
			throw new NotImplementedException();
		}
	}

	private boolean like(String s1, String s2) 
	{
		int pos = s2.indexOf('%');
		if (pos > 0)
		{
			String sub = s1.substring(0, pos);
			return s2.startsWith(sub);
		}
		else
		{
			throw new NotImplementedException();
		}
	}

	private boolean ilike(String s1, String s2) 
	{
		s1 = s1.toLowerCase();
		s2 = s2.toLowerCase();
		return like(s1, s2);
	}
}

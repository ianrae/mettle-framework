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
				
		default:
			throw new NotImplementedException();
		}
	}

}

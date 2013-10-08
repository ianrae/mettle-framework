package org.mef.framework.entitydb;

public class StringValueMatcher implements IValueMatcher
{

	@Override
	public boolean isMatch(Object value, Object valueToMatch)
	{
		String s1 = (String)value;
		String s2 = (String) valueToMatch;
		
		return s1.equalsIgnoreCase(s2);
	}

}

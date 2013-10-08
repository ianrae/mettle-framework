package org.mef.framework.entitydb;

public class LongValueMatcher implements IValueMatcher
{

	@Override
	public boolean isMatch(Object value, Object valueToMatch)
	{
		Long n1 = (Long)value;
		Long n2 = (Long) valueToMatch;
		
		return (n1 == n2);
	}

}

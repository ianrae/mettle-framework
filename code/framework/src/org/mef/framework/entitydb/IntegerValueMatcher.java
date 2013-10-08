package org.mef.framework.entitydb;

public class IntegerValueMatcher implements IValueMatcher
{

	@Override
	public boolean isMatch(Object value, Object valueToMatch)
	{
		Integer n1 = (Integer)value;
		Integer n2 = (Integer) valueToMatch;
		
		return (n1 == n2);
	}

}

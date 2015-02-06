package org.mef.framework.metadata;

public class DefaultValueHandlers 
{
	//one handle shared by all value objects, so don't put any member variables in here
	public static class IntHandler implements ValueHandler<Integer>
	{

		@Override
		public Object toObj(Integer value) 
		{
			return value;
		}

		@Override
		public Integer fromObj(Object obj) 
		{
			Integer n = (Integer) obj;
			return n;
		}
		
	}
}

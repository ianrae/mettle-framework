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
	
	//one handle shared by all value objects, so don't put any member variables in here
	public static class LongHandler implements ValueHandler<Long>
	{

		@Override
		public Object toObj(Long value) 
		{
			return value;
		}

		@Override
		public Long fromObj(Object obj) 
		{
			Long n = (Long) obj;
			return n;
		}
		
	}
	
	//one handle shared by all value objects, so don't put any member variables in here
	public static class BooleanHandler implements ValueHandler<Boolean>
	{

		@Override
		public Object toObj(Boolean value) 
		{
			return value;
		}

		@Override
		public Boolean fromObj(Object obj) 
		{
			Boolean n = (Boolean) obj;
			return n;
		}
		
	}
	
	
	//one handle shared by all value objects, so don't put any member variables in here
	public static class StringHandler implements ValueHandler<String>
	{

		@Override
		public Object toObj(String value) 
		{
			return value;
		}

		@Override
		public String fromObj(Object obj) 
		{
			String n = (String) obj;
			return n;
		}
		
	}
	
	//one handle shared by all value objects, so don't put any member variables in here
	public static class DoubleHandler implements ValueHandler<Double>
	{

		@Override
		public Object toObj(Double value) 
		{
			return value;
		}

		@Override
		public Double fromObj(Object obj) 
		{
			Double n = (Double) obj;
			return n;
		}
		
	}
}

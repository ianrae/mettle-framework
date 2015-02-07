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
		
		@Override
		public Object copy(Object value)
		{
			return new Integer((Integer)value);			
		}

		@Override
		public Object fromString(String sVal) 
		{
			return Integer.parseInt(sVal);			
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
		
		@Override
		public Object copy(Object value)
		{
			return new Long((Long)value);			
		}

		@Override
		public Object fromString(String sVal) 
		{
			return Long.parseLong(sVal);			
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
		
		@Override
		public Object copy(Object value)
		{
			return new Boolean((Boolean)value);			
		}

		@Override
		public Object fromString(String sVal) 
		{
			return Boolean.parseBoolean(sVal);
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
		
		@Override
		public Object copy(Object value)
		{
			return new String((String)value);			
		}

		@Override
		public Object fromString(String sVal) 
		{
			return sVal;
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
		
		@Override
		public Object copy(Object value)
		{
			return new Double((Double)value);			
		}

		@Override
		public Object fromString(String sVal) 
		{
			return Double.parseDouble(sVal);
		}
	}
	
	//one handle shared by all value objects, so don't put any member variables in here
	public static class TupleValueHandler implements ValueHandler<TupleValue>
	{
		@Override
		public Object toObj(TupleValue value) 
		{
			return value;
		}

		@Override
		public TupleValue fromObj(Object obj) 
		{
			TupleValue n = (TupleValue) obj;
			return n;
		}
		
		@Override
		public Object copy(Object value)
		{
			return new TupleValue((TupleValue)value);			
		}

		@Override
		public Object fromString(String sVal) 
		{
			return null; //!!
		}
	}
	
	//one handle shared by all value objects, so don't put any member variables in here
	public static class ListValueHandler implements ValueHandler<ListValue>
	{
		@Override
		public Object toObj(ListValue value) 
		{
			return value;
		}

		@Override
		public ListValue fromObj(Object obj) 
		{
			ListValue n = (ListValue) obj;
			return n;
		}
		
		@Override
		public Object copy(Object value)
		{
			return new ListValue((ListValue)value);			
		}

		@Override
		public Object fromString(String sVal) 
		{
			return null; //!!
		}
	}
}

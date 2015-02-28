package twixt;

import static org.junit.Assert.*;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.mef.framework.metadata.BooleanValue;
import org.mef.framework.metadata.Converter;
import org.mef.framework.metadata.DateValue;
import org.mef.framework.metadata.DoubleValue;
import org.mef.framework.metadata.IntegerValue;
import org.mef.framework.metadata.ListValue;
import org.mef.framework.metadata.LongValue;
import org.mef.framework.metadata.StringValue;
import org.mef.framework.metadata.Value;
import org.mef.framework.metadata.validate.ValidationErrors;

import tools.BaseTest;

public class TwixtTests extends BaseTest
{
//	public interface Converter 
//	{
//		String print(Object obj);
//		Object parse(String s);
//	}	
//	public interface ValContext
//	{
//		
//	}
//	
//	public interface Validator
//	{
//		boolean validate(ValContext valctx, Object obj);
//	}	
//	
//	public abstract static class Value
//	{
//		protected Object obj;
//		protected Converter converter;
//		protected Validator validator;
//		
//		public Value()
//		{}
//		public Value(Object obj)
//		{
//			this.obj = obj;
//		}
//		
//		//??deep copy needed!!
//		
//		public Object getUnderlyingValue()
//		{
//			return obj;
//		}
//		public void setUnderlyingValue(Object obj)
//		{
//			this.obj = obj;
//		}
//		
//		public boolean validate(ValContext valctx)
//		{
//			if (validator != null)
//			{
//				return validator.validate(valctx, obj);
//			}
//			return true;
//		}
//		
//		protected abstract void parse(String input);
//		protected abstract String render();
//		@Override
//		public String toString()
//		{
//			if (converter != null)
//			{
//				return converter.print(obj);
//			}
//			else
//			{
//				return render();
//			}
//		}
//		public void fromString(String input)
//		{
//			if (converter != null)
//			{
//				Object object = converter.parse(input);
//				setUnderlyingValue(object);
//			}
//			else
//			{
//				parse(input);
//			}
//		}
//		public Converter getConverter() {
//			return converter;
//		}
//		public void setConverter(Converter converter) {
//			this.converter = converter;
//		}
//		public Validator getValidator() {
//			return validator;
//		}
//		public void setValidator(Validator validator) {
//			this.validator = validator;
//		}
//	}
//	
//	
//	public class IntegerValue extends Value
//	{
//		public IntegerValue()
//		{
//			this(0);
//		}
//		public IntegerValue(Integer n)
//		{
//			super(n);
//		}
//		
//		@Override
//		protected String render()
//		{
//			Integer n = get();
//			return n.toString();
//		}
//		
//		@Override
//		protected void parse(String input)
//		{
//			Integer n = Integer.parseInt(input);
//			this.setUnderlyingValue(n);
//		}
//		
//		//return in our type
//		public Integer get()
//		{
//			Integer nVal = (Integer)obj;
//			return new Integer(nVal.intValue()); //return copy
//		}
//		public void set(Integer n)
//		{
//			Integer nVal = new Integer(n.intValue());
//			setUnderlyingValue(nVal);
//		}
//	}
	
	public class CommaIntegerValue extends IntegerValue
	{
		private class Conv implements Converter
		{

			@Override
			public String print(Object obj) 
			{
				Integer n = get();
				String s = NumberFormat.getNumberInstance(Locale.US).format(n);
				return s;
			}

			@Override
			public Object parse(String s) 
			{
				s = s.replace(",", "");
				Integer n = Integer.parseInt(s);
				return n;
			}
			
		}
		public CommaIntegerValue()
		{
			this(0);
		}
		public CommaIntegerValue(Integer n)
		{
			super(n);
			setConverter(new Conv());
		}
		

	}
	
	@Test
	public void test() 
	{
		IntegerValue v = new IntegerValue();
		v.set(44);
		assertEquals(44, v.get());
		
		LongValue v2 = new LongValue();
		v2.set(456L);
		assertEquals(456L, v2.get());
		
		BooleanValue v3 = new BooleanValue();
		assertEquals(false, v3.get());
		v3.set(true);
		assertEquals(true, v3.get());
		
		StringValue v4 = new StringValue();
		assertEquals("", v4.get());
		v4.set("sdf");
		assertEquals("sdf", v4.get());
		
		DateValue v5 = new DateValue();
		assertNotNull(v5.get());
		
		Date dt = new Date();
		int yr = dt.getYear();
		v5.set(dt);
		assertEquals(yr, v5.get().getYear());
		
		DoubleValue v6 = new DoubleValue();
		v6.set(45.6);
		assertEquals(45.6, v6.get(), 0.001);
		
		List<Value> L = new ArrayList<Value>();
		L.add(v3);
		L.add(v4);
		ListValue v7 = new ListValue();
		assertEquals(0, v7.size());
		v7.set(L);
		assertEquals(2, v7.size());
	}
	
	@Test
	public void testDate() throws Exception
	{
		Date dt = new Date(115, 01, 21, 8, 30); //Thu Feb 21 08:30:00 EST 2015
		log(dt.toString());
		
		DateValue val = new DateValue(dt);
		String s = val.toString();
		assertEquals("2015-02-21", s);
		
		val.fromString("2014-12-25");
		s = val.toString();
		assertEquals("2014-12-25", s);
	}

	@Test
	public void testComma() throws Exception 
	{
		CommaIntegerValue v = new CommaIntegerValue(12345);
		assertEquals("12,345", v.toString());
		
		v.fromString("4,5678");
		assertEquals(45678, v.get());
	}
}

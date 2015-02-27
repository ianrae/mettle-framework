package twixt;

import static org.junit.Assert.*;

import java.text.NumberFormat;
import java.util.Locale;

import org.junit.Test;
import org.mef.framework.metadata.Converter;
import org.mef.framework.metadata.IntegerValue;
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
	}

	@Test
	public void testComma() 
	{
		CommaIntegerValue v = new CommaIntegerValue(12345);
		assertEquals("12,345", v.toString());
		
		v.fromString("4,5678");
		assertEquals(45678, v.get().intValue());
	}
}

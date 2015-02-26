package twixt;

import static org.junit.Assert.*;

import java.text.NumberFormat;
import java.util.Locale;

import org.junit.Test;
import org.mef.framework.metadata.Value;
import org.mef.framework.metadata.validate.ValidationErrors;

import tools.BaseTest;

public class TwixtTests extends BaseTest
{
	public interface ZConverter 
	{
		String print(Object obj);
		Object parse(String s);
	}	
	public interface ZValContext
	{
		
	}
	
	public interface ZValidator
	{
		boolean validate(ZValContext valctx, Object obj);
	}	
	
	public abstract static class ZValue
	{
		protected Object obj;
		protected ZConverter converter;
		protected ZValidator validator;
		
		public ZValue()
		{}
		public ZValue(Object obj)
		{
			this.obj = obj;
		}
		
		public Object getUnderlyingValue()
		{
			return obj;
		}
		public void setUnderlyingValue(Object obj)
		{
			this.obj = obj;
		}
		
		public boolean validate(ZValContext valctx)
		{
			if (validator != null)
			{
				return validator.validate(valctx, obj);
			}
			return true;
		}
		
		protected abstract void parse(String input);
		protected abstract String render();
		@Override
		public String toString()
		{
			if (converter != null)
			{
				return converter.print(obj);
			}
			else
			{
				return render();
			}
		}
		public void fromString(String input)
		{
			if (converter != null)
			{
				Object object = converter.parse(input);
				setUnderlyingValue(object);
			}
			else
			{
				parse(input);
			}
		}
		public ZConverter getConverter() {
			return converter;
		}
		public void setConverter(ZConverter converter) {
			this.converter = converter;
		}
		public ZValidator getValidator() {
			return validator;
		}
		public void setValidator(ZValidator validator) {
			this.validator = validator;
		}
	}
	
	
	public class ZIntegerValue extends ZValue
	{
		public ZIntegerValue()
		{
			this(0);
		}
		public ZIntegerValue(Integer n)
		{
			super(n);
		}
		
		@Override
		protected String render()
		{
			Integer n = get();
			return n.toString();
		}
		
		@Override
		protected void parse(String input)
		{
			Integer n = Integer.parseInt(input);
			this.setUnderlyingValue(n);
		}
		
		//return in our type
		public Integer get()
		{
			return (Integer) obj;
		}
		public void set(Integer n)
		{
			setUnderlyingValue(n);
		}
	}
	
	public class ZCommaIntegerValue extends ZValue
	{
		private class Conv implements ZConverter
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
		public ZCommaIntegerValue()
		{
			this(0);
		}
		public ZCommaIntegerValue(Integer n)
		{
			super(n);
			setConverter(new Conv());
		}
		
		@Override
		protected String render()
		{
			Integer n = get();
			return n.toString();
		}
		
		@Override
		protected void parse(String input)
		{
			Integer n = Integer.parseInt(input);
			this.setUnderlyingValue(n);
		}
		
		//return in our type
		public Integer get()
		{
			return (Integer) obj;
		}
		public void set(Integer n)
		{
			setUnderlyingValue(n);
		}

	}
	
	@Test
	public void test() 
	{
		ZIntegerValue v = new ZIntegerValue();
	}

	@Test
	public void testComma() 
	{
		ZCommaIntegerValue v = new ZCommaIntegerValue(12345);
		assertEquals("12,345", v.toString());
		
		v.fromString("4,5678");
		assertEquals(45678, v.get().intValue());
	}
}

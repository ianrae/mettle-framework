import static org.junit.Assert.*;

import org.junit.Test;
import org.mef.framework.metadata.validate.ValidationErrors;


public class TwixtTests 
{
	public static class PhoneNum extends org.mef.framework.metadata.StringValueAndValidator
	{
		public PhoneNum(String val) 
		{
			super(val, "phoneNum");
		}

		@Override
		public boolean validate(Object val, ValidationErrors errors) 
		{
			String s = (String)val;
			return (s.length() == 8); //258-1833
		}
	}

	@Test
	public void test() 
	{
		String s = "258-9099";
		PhoneNum ph = new PhoneNum(s);
		assertEquals(s, ph.get());
		
		String s2 = ph.toString();
		assertEquals(s, s2);
		
		boolean b = ph.validate(new ValidationErrors());
		assertEquals(true, b);
		
		ph.fromString("555-66");
		assertEquals("555-66", ph.get());
		b = ph.validate(new ValidationErrors());
		assertEquals(false, b);
	}

}

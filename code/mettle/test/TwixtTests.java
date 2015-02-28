import static org.junit.Assert.*;

import org.junit.Test;
import org.mef.framework.metadata.validate.IValidator;
import org.mef.framework.metadata.validate.ValContext;
import org.mef.framework.metadata.validate.ValidationErrors;
import org.mef.framework.metadata.StringValue;

public class TwixtTests 
{
	public static class PhoneNum extends StringValue
	{
		private class Validator implements IValidator
		{

			@Override
			public void validate(ValContext valctx, Object obj) 
			{
				StringValue val = (StringValue) obj;
				String s = val.get();
				if (s.length() != 8) //258-1833
				{
					valctx.addError("sdfdfs");
				}
			}
			
		}
		
		public PhoneNum(String val) 
		{
			super(val);
			this.setValidator(new Validator());
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
		
		ValContext vtx = new ValContext();
		ph.validate(vtx);
		assertEquals(true, vtx.succeeded());
		
		ph.fromString("555-66");
		assertEquals("555-66", ph.get());
		ph.validate(vtx);
		assertEquals(false, vtx.succeeded());
	}

}

import static org.junit.Assert.*;


import org.junit.Test;
import org.mef.framework.metadata.DefaultValueHandlers;
import org.mef.framework.metadata.Value;
import org.mef.framework.metadata.ValueHandler;
import org.mef.framework.metadata.ValueHandlerRegistry;


public class ValueHandlerTests 
{
	@Test
	public void test() 
	{
		DefaultValueHandlers.IntHandler h = new DefaultValueHandlers.IntHandler();
		Integer n = (Integer) h.toObj(45);
		assertEquals(45, n.intValue());
	}
	
	@Test
	public void testReg()
	{
		ValueHandlerRegistry reg = new ValueHandlerRegistry();
		
		ValueHandler h = reg.get(0);
		assertNull(h);
		
		h = reg.get(Value.TYPE_INT);
		assertNotNull(h);
	}
	
	@Test
	public void testAll()
	{
		Value v = new Value(Value.TYPE_BOOLEAN, true);
		Boolean b = v.getBoolean();
		assertEquals(true, b);
		
		v = new Value(Value.TYPE_DOUBLE, 34.5);
		Double d = v.getDouble();
		assertEquals(34.5, d.doubleValue(), 0.1);
		
		v = new Value(Value.TYPE_INT, 18);
		Integer n = v.getInt();
		assertEquals(18, n.intValue());
		
		v = new Value(Value.TYPE_LONG, 3402L);
		Long lval = v.getLong();
		assertEquals(3402L, lval.longValue());
		
		v = new Value(Value.TYPE_STRING, "abc");
		String s = v.getString();
		assertEquals("abc", s);
	}

}

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mef.framework.metadata.Value;


public class ValueHandlerTests 
{
	public interface ValueHandler<T>
	{
		Object toObj(T value);
		T fromObj(Object obj);
	}
	//one handle shared by all value objects, so don't put any member variables in here
	public static class IntValueHandler implements ValueHandler<Integer>
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
	
	public static class ValueHandlerRegistry
	{
		private List<ValueHandler> reg;
		
		public ValueHandlerRegistry()
		{
			ValueHandler[] arregistry = {
					new IntValueHandler(),
					new IntValueHandler()
			};
			
			reg = Arrays.asList(arregistry);
			
//			ValueHandler<Integer> zz = arregistry[0];
//			Object x = zz.toObj(555);
		}
		
		
		//keep all type values sequential, or at least close together!
		public void register(int type, ValueHandler handler)
		{
			int desiredLen = type + 1;
			for(int i = reg.size(); i <= desiredLen; i++)
			{
				reg.add(null);
			}
			
			reg.set(type, handler);
		}
		
		public ValueHandler get(int type)
		{
			return reg.get(type);
		}
		
	}
	
	@Test
	public void test() 
	{
		IntValueHandler h = new IntValueHandler();
		Integer n = (Integer) h.toObj(45);
		assertEquals(45, n.intValue());
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

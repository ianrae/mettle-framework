package mef;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;
import org.mef.framework.commands.DeleteCommand;
import org.mef.framework.commands.Command;
import org.mef.framework.sfx.SfxContext;
import org.mef.framework.utils.MethodInvoker;


public class OtherTests 
{
	@Test
	public void testReflection() 
	{
		SfxContext ctx = new SfxContext();
		MethodInvoker invoker = new MethodInvoker(ctx, this, "myFunc", Command.class);
		assertNotNull(invoker._method);

		_counter = 0;
		String s = null;
		Command msg = new Command();
		s = (String) invoker.call(msg);

		assertEquals(1, _counter);
		assertEquals("abc", s);
		
		log("delete..");
		invoker = new MethodInvoker(ctx, this, "myDeleteFunc", Command.class);
		assertNotNull(invoker._method);

		_counter = 0;
		DeleteCommand delmsg = new DeleteCommand(44L);
		s = (String) invoker.call(delmsg);

		assertEquals(1, _counter);
		assertEquals("abc", s);
	}
	
	@Test
	public void testCommand()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("abc", "def");
		
		Command cmd = new Command();
		cmd.setParameters(map);
		
		assertEquals("def", cmd.getParameter("abc"));
		assertEquals(null, cmd.getParameter("xyz"));
		
	}
	
	private void log(String s)
	{
		System.out.println(s);
	}

	int _counter = 0;
	public String myFunc(Command request)
	{
		_counter++;
		return "abc";
	}

	public String myDeleteFunc(DeleteCommand request)
	{
		_counter++;
		return "abc";
	}
}

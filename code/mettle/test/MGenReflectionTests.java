
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mef.framework.entities.Entity;
import org.mef.framework.sfx.SfxContext;
import org.mef.framework.sfx.SfxErrorTracker;
import org.mef.tools.mgen.parser.FieldDef;
import org.mef.tools.mgen.parser.ModelMethodFinder;
import org.reflections.Reflections;

import testentities.StreetAddress;


public class MGenReflectionTests 
{
	private SfxContext _ctx;

	@Test
	public void test() throws Exception 
	{
		Class clazz = StreetAddress.class; //model
		for(Method meth : clazz.getMethods())
		{
			log(meth.getName());
		}
		
		log("using reflections...");
		ModelMethodFinder finder = new ModelMethodFinder(_ctx);
		Class cz = Class.forName("testentities.StreetAddress");
		
		List<FieldDef> fieldL = finder.getProperties(cz);
		assertEquals(3, fieldL.size());
		
		log("results..");
		for(FieldDef fdef : fieldL)
		{
			log(String.format("%s %s", fdef.typeName, fdef.name));
			assertFalse(fdef.isReadOnly);
		}
		
		//doesn't work! so must still mention each entity in mef.xml
		Reflections reflections = new Reflections("mef.entities");		
		Set<Class<? extends Entity>> allClasses = 
			     reflections.getSubTypesOf(org.mef.framework.entities.Entity.class);
		
		for(Class cztmp : allClasses)
		{
			log("cz: " + cztmp.getCanonicalName());
		}
		//chkErrors(0);
	}

	private void log(String s)
	{
		System.out.println(s);
	}
	
	@Before
	public void init()
	{
		_ctx = new SfxContext();
		_ctx.getServiceLocator().registerSingleton(SfxErrorTracker.class, new SfxErrorTracker(_ctx));
	}
}


import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mef.framework.entities.Entity;
import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;
import org.mef.framework.sfx.SfxErrorTracker;
import org.mef.tools.mgen.parser.FieldDef;
import org.reflections.Reflections;

import testentities.StreetAddress;


public class MGenReflectionTests 
{
	public static class ModelMethodFinder extends SfxBaseObj
	{

		public ModelMethodFinder(SfxContext ctx) 
		{
			super(ctx);
		}
		
		List<FieldDef> getProperties(Class clazz)
		{
			Set<Method> list = Reflections.getAllMethods(clazz, Reflections.withPrefix("get"));

			List<FieldDef> fieldL = new ArrayList<FieldDef>();
			
			for(Method m : list)
			{
				if (! Modifier.isPublic(m.getModifiers()))
				{
					continue;
				}
				else if (shouldSkip(m))
				{
					continue;
				}
				
				log(m.getName());
				FieldDef fdef = new FieldDef();
				fdef.isSeedField = false; //!!fix later
				
				String s = m.getName().substring(3); //skip 'get'
				
				fdef.name = lowify(s);
				fdef.uname = uppify(fdef.name);
				
				Class retClazz = m.getReturnType();
				if (retClazz == null)
				{
					this.addError("getter returns null!");
				}
				
				fdef.typeName = retClazz.getSimpleName();
				
				//!!avoid duplicates
				boolean exists = false;
				for(FieldDef tmp : fieldL)
				{
					if (tmp.name.equals(fdef.name))
					{
						exists = true;
						break;
					}
				}
				
				if (! exists)
				{
					fieldL.add(fdef);
				}
			}
			return fieldL;
		}

		private boolean shouldSkip(Method m) 
		{
			String[] arSkip = { "getUnderlyingModel", "getClass" };
			String methodName = m.getName();
			
			for(String name : arSkip)
			{
				if (methodName.equals(name))
				{
					return true;
				}
			}
			return false;
		}
		
		private String uppify(String name) 
		{
			String upper = name.toUpperCase();
			String s = upper.substring(0, 1);
			s += name.substring(1);
			return s;
		}
		protected String lowify(String name) 
		{
			String upper = name.toLowerCase();
			String s = upper.substring(0, 1);
			s += name.substring(1);
			return s;
		}
	}

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

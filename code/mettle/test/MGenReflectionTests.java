
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
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

import testentities.Hotel;
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
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testOneToMany()
	{
		Class clazz = Hotel.class;
		Set<Method> list = Reflections.getAllMethods(clazz, Reflections.withPrefix("get"));
		
//		Annotation annotation = javax.persistence.OneToMany.class;
		Class annoClazz = javax.persistence.OneToMany.class;
		Set<Field> set = Reflections.getAllFields(clazz, Reflections.withAnnotation(annoClazz));
		assertEquals(1, set.size());
		Field f = getField(set);
		assertEquals("addresses", f.getName());
		
		Class clazzf = f.getType();
		log(clazzf.getCanonicalName());
		log(f.toGenericString());
		
		ModelMethodFinder finder = new ModelMethodFinder(_ctx);
		List<String> genL = finder.getOneToManyFields(clazz);
		assertEquals(1, genL.size());
		assertEquals("public java.util.List<testentities.StreetAddress> testentities.Hotel.addresses", genL.get(0));
	}
	
	private Field getField(Set<Field> set)
	{
		Iterator<Field> itr  = set.iterator();

		while (itr.hasNext()) 
		{
			Field fld = itr.next();
			return fld;
		}		
		
		return null;
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

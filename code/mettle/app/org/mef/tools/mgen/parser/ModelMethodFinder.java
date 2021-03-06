package org.mef.tools.mgen.parser;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;
import org.reflections.Reflections;


public class ModelMethodFinder extends SfxBaseObj
{
	public class FieldComparator implements Comparator<FieldDef>
	{
	    public FieldComparator()
	    {
	    }
	    
	    @Override
	    public int compare(FieldDef obj1, FieldDef obj2)
	    {
	    	return obj1.name.compareTo(obj2.name);
	    }	    	
	    
	}	
	

	public ModelMethodFinder(SfxContext ctx) 
	{
		super(ctx);
	}
	
	public List<FieldDef> getPropertiesFor(String className) throws ClassNotFoundException
	{
		Class clazz = Class.forName(className);
		return this.getProperties(clazz);
	}
	
	public List<FieldDef> getProperties(Class clazz)
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
			
//			log(m.getName());
			FieldDef fdef = new FieldDef();
			fdef.isSeedField = false; //!!fix later
			fdef.isReadOnly = false;
			if (! doesSetMethodExist(clazz, m.getName()))
			{
				fdef.isReadOnly = true;
			}
			
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
		
		Collections.sort(fieldL, new FieldComparator());
		return fieldL;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<String> getOneToManyFields(Class clazz)
	{
		List<String> resultL = new ArrayList<String>();

		Class annoClazz = javax.persistence.OneToMany.class;
		Set<Field> set = Reflections.getAllFields(clazz, Reflections.withAnnotation(annoClazz));
		Iterator<Field> itr  = set.iterator();
		while (itr.hasNext()) 
		{
			Field fld = itr.next();
			String s = fld.toGenericString();
			resultL.add(s);
		}	
		return resultL;
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

	private boolean doesSetMethodExist(Class clazz, String methodName)
	{
		Set<Method> list = Reflections.getAllMethods(clazz, Reflections.withPrefix("set"));
		String target = "set" + methodName.substring(3);
		
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
			
			if (m.getName().equals(target))
			{
				return true;
			}
			
		}
		return false;
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
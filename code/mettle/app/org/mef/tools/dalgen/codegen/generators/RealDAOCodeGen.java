package org.mef.tools.dalgen.codegen.generators;

import java.util.ArrayList;
import java.util.List;


import org.mef.tools.dalgen.parser.EntityDef;
import org.mef.tools.dalgen.parser.FieldDef;
import org.stringtemplate.v4.ST;


import sfx.SfxContext;

public class RealDAOCodeGen extends CodeGenBase
{
	public RealDAOCodeGen(SfxContext ctx)
	{
		super(ctx);
	}
	
	@Override
	public String generate(EntityDef def)
	{
		this.isExtended = def.extendReal;
		String result = genHeader(def.name); 
		ST st = _group.getInstanceOf("classdecl");
		
		st.add("name", getClassName(def));
		st.add("type", def.name);
		result += st.render(); 

		result += genTouchAll(def);
		result += genTouchAll2(def);
		result += genQueries(def);
		result += genMethods(def);
		st = _group.getInstanceOf("endclassdecl");
		result += st.render(); 
		
		return result;
	}
	
	private String genTouchAll(EntityDef def) 
	{
		String result = "";
		ST st = _group.getInstanceOf("touchall1");
		st.add("type", def.name);
		
		List<String> assignsL = new ArrayList<String>();
		for(FieldDef fdef : def.fieldL)
		{
			if (fdef.name.equals("id"))
			{
				continue;
			}

			boolean isEntity = isEntity(def, fdef.typeName);
			String s;
			if (isEntity)
			{
				s = createDAOString(fdef);
				s += String.format("t.set%s(%sDAO.createModelFromEntity(entity.%s));", uppify(fdef.name), fdef.name, fdef.name);
			}
			else
			{
				s = String.format("t.set%s(entity.%s);", uppify(fdef.name), fdef.name);
			}
			assignsL.add(s);
		}
		st.add("assigns", assignsL);
		
		result = st.render(); 
		result += "\n\n";
		return result;
	}
	
	private String createDAOString(FieldDef fdef)
	{
		String name = fdef.name;
		String upname = uppify(fdef.name);
		String s = String.format("%sDAO %sDAO = (%sDAO)Initializer.theCtx.getServiceLocator().getInstance(I%sDAO.class);\n",
				upname, name, upname, upname);
		return s;
	}
	
	private String genTouchAll2(EntityDef def) 
	{
		String result = "";
		ST st = _group.getInstanceOf("touchall2");
		st.add("type", def.name);
		
		List<String> assignsL = new ArrayList<String>();
		for(FieldDef fdef : def.fieldL)
		{
			if (fdef.name.equals("id"))
			{
				continue;
			}
			
			boolean isEntity = isEntity(def, fdef.typeName);
			String s;
			if (isEntity)
			{
				s = createDAOString(fdef);
				s += String.format("entity.%s = %sDAO.createEntityFromModel(t.get%s());", fdef.name, fdef.name, uppify(fdef.name));
			}
			else
			{
				s = String.format("entity.%s = t.get%s();", fdef.name, uppify(fdef.name));
			}
			assignsL.add(s);
		}
		st.add("assigns", assignsL);
		
		result = st.render(); 
		result += "\n\n";
		return result;
	}

	@Override
	public String getClassName(EntityDef def)
	{
		String className = def.name + "DAO";
		className = makeClassName(className, def.extendReal);
		return className;
	}
	
	
	protected String genQueries(EntityDef def)
	{
		String result = "";
		for(String query : def.queryL)
		{
			ST st = _group.getInstanceOf("querydecl");
			String fieldName = getFieldName(query);
			st.add("type", def.name); //getFieldType(def, fieldName));
			st.add("fieldType", getFieldType(def, fieldName));
			st.add("name", fieldName);
			result = st.render(); 
			result += "\n\n";
		}
		return result;
	}
	
	@Override
	protected String buildField(EntityDef def, FieldDef fdef)
	{
		return "";
	}

}
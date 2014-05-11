package org.mef.tools.mgen.codegen.generators;

import org.mef.framework.sfx.SfxContext;
import org.mef.tools.mgen.parser.EntityDef;
import org.mef.tools.mgen.parser.FieldDef;
import org.stringtemplate.v4.ST;


public class MockDAOCodeGen extends CodeGenBase
{
	public MockDAOCodeGen(SfxContext ctx)
	{
		super(ctx);
	}
	
	@Override
	public String generate(EntityDef def)
	{
//		this.isExtended = def.shouldExtend(EntityDef.DAO_MOCK);
		String result = genHeader(! this.isParentOfExtended); 
		ST st = _group.getInstanceOf("classdecl");
		
		st.add("name", getClassName(def));
		st.add("type", def.name);
		st.add("isParentOfExtended", this.isParentOfExtended);
		result += st.render(); 
		
		if (! isParentOfExtended)
		{
			result += genQueries(def);
			result += genMethods(def, true);
		}
		st = _group.getInstanceOf("endclassdecl");
		result += st.render(); 
		
		return result;
	}
	
	@Override
	public String getClassName(EntityDef def)
	{
		String className = "Mock" + def.name + "DAO";
		className = makeClassName(className); //, def.shouldExtend(EntityDef.DAO_MOCK));
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
			
			String fieldType = getFieldType(def, fieldName);
			st.add("fieldType", fieldType);
			if (fieldType.equals("String"))
			{
				st.add("eq", ".equals(val)");
			}
			else
			{
				st.add("eq", " == val");
			}
			st.add("name", fieldName);
			result += st.render(); 
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
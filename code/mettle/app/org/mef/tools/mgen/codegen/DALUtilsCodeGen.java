package org.mef.tools.mgen.codegen;


import org.mef.framework.sfx.SfxContext;
import org.mef.tools.mgen.codegen.generators.CodeGenBase;
import org.mef.tools.mgen.parser.EntityDef;
import org.mef.tools.mgen.parser.FieldDef;
import org.stringtemplate.v4.ST;


public class DALUtilsCodeGen extends CodeGenBase
{
	public DALUtilsCodeGen(SfxContext ctx, String path, String packageName)
	{
		super(ctx, path, packageName);
	}
	
	@Override
	public String generate(EntityDef def)
	{
		String result = genHeader(); 
		ST st = _group.getInstanceOf("classdecl");
		
		st.add("name", getClassName(def));
		st.add("type", def.name);
		result += st.render(); 
		
		st = _group.getInstanceOf("endclassdecl");
		result += st.render(); 
		
		return result;
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

	@Override
	public String getClassName(EntityDef def) 
	{
		return def.name + "DALUtils";
	}

}
package org.mef.dalgen.codegen.generators;

import org.mef.dalgen.parser.EntityDef;
import org.mef.dalgen.parser.FieldDef;
import org.stringtemplate.v4.ST;

import sfx.SfxContext;

public class RealDALCodeGen extends CodeGenBase
{
	public RealDALCodeGen(SfxContext ctx, String path, String packageName)
	{
		super(ctx, path, packageName);
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
		
		result += genQueries(def);
		result += genMethods(def);
		st = _group.getInstanceOf("endclassdecl");
		result += st.render(); 
		
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
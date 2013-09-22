package org.mef.dalgen.codegen;

import org.mef.dalgen.parser.EntityDef;
import org.mef.dalgen.parser.FieldDef;
import org.stringtemplate.v4.ST;

import sfx.SfxContext;

public class MockDALCodeGen extends CodeGenBase
{
	public MockDALCodeGen(SfxContext ctx, String path)
	{
		super(ctx, path);
	}
	
	public String generate(EntityDef def)
	{
		ST st = _group.getInstanceOf("classdecl");
		st.add("name", def.name);
		String result = st.render(); 
		
		result += genQueries(def);
		
		return result;
	}
	
	protected String genQueries(EntityDef def)
	{
		String result = "";
		for(String query : def.queryL)
		{
			ST st = _group.getInstanceOf("querydecl");
			String fieldName = getFieldName(query);
			st.add("type", getFieldType(def, fieldName));
			st.add("name", fieldName);
			result = st.render(); 
			result += "\n\n";
		}
		return result;
	}
	
	@Override
	protected String buildField(FieldDef fdef)
	{
		return "";
	}

}
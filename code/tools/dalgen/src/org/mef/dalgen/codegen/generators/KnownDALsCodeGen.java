package org.mef.dalgen.codegen.generators;

import org.mef.dalgen.parser.EntityDef;
import org.mef.dalgen.parser.FieldDef;
import org.stringtemplate.v4.ST;

import sfx.SfxContext;

public class KnownDALsCodeGen extends CodeGenBase
{
	public KnownDALsCodeGen(SfxContext ctx, String path, String packageName)
	{
		super(ctx, path, packageName);
	}
	
	@Override
	public String generate(EntityDef def)
	{
		this.isExtended = true; //always
		String result = genHeader(); 

		ST st = _group.getInstanceOf("classdecl");
//		st.add("type", def.name);
		st.add("name", getClassName(def));
		result += st.render(); 
		
		for(EntityDef tmp: def.allEntityTypes)
		{
			if (tmp.enabled)
			{
				st = _group.getInstanceOf("adddal");
				st.add("type", tmp.name);
				result += st.render(); 
			}
		}
		
		st = _group.getInstanceOf("endclassdecl");
		result += st.render(); 
		
		return result;
	}
	@Override
	public String getClassName(EntityDef def)
	{
		return makeClassName("AllKnownDALs", true);
	}
	
	
	@Override
	protected String buildField(EntityDef def, FieldDef fdef)
	{
		return "";
	}


}
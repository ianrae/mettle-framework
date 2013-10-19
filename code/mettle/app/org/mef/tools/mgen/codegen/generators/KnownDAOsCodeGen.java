package org.mef.tools.mgen.codegen.generators;

import org.mef.framework.sfx.SfxContext;
import org.mef.tools.mgen.parser.EntityDef;
import org.mef.tools.mgen.parser.FieldDef;
import org.stringtemplate.v4.ST;


public class KnownDAOsCodeGen extends CodeGenBase
{
	public KnownDAOsCodeGen(SfxContext ctx, String path, String packageName)
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
		return makeClassName("AllKnownDAOs", true);
	}
	
	
	@Override
	protected String buildField(EntityDef def, FieldDef fdef)
	{
		return "";
	}


}
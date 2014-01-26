package org.mef.tools.mgen.codegen.generators;

import org.mef.framework.sfx.SfxContext;
import org.mef.tools.mgen.parser.EntityDef;
import org.mef.tools.mgen.parser.FieldDef;
import org.stringtemplate.v4.ST;


public class DaoFinderCodeGen extends CodeGenBase
{
	public DaoFinderCodeGen(SfxContext ctx)
	{
		super(ctx);
	}
	
	@Override
	public String generate(EntityDef def)
	{
//		this.isExtended = false; //always
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
				st.add("type", String.format("I%sDAO", tmp.name));
				st.add("name", String.format("get%sDao", tmp.name));
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
		return makeClassName("DaoFinder"); // false);
	}
	
	
	@Override
	protected String buildField(EntityDef def, FieldDef fdef)
	{
		return "";
	}


}
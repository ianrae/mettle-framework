package org.mef.tools.mgen.codegen.generators;

import java.util.ArrayList;
import java.util.List;


import org.mef.framework.sfx.SfxContext;
import org.mef.tools.mgen.parser.EntityDef;
import org.mef.tools.mgen.parser.FieldDef;
import org.stringtemplate.v4.ST;




public class RealQueryProcCodeGen extends CodeGenBase
{
	public RealQueryProcCodeGen(SfxContext ctx)
	{
		super(ctx);
	}

	@Override
	public String generate(EntityDef def)
	{
		//		this.isExtended = def.shouldExtend(EntityDef.DAO_REAL);
		String result = genHeader(def.name); 
		ST st = _group.getInstanceOf("classdecl");

		st.add("name", getClassName(def));
		st.add("type", def.name);
		st.add("isParentOfExtended", this.isParentOfExtended);
		result += st.render(); 

		st = _group.getInstanceOf("endclassdecl");
		result += st.render(); 

		return result;
	}

	@Override
	public String getClassName(EntityDef def)
	{
		String className = def.name + "EbeanQueryProcessor";
		className = makeClassName(className); //, def.shouldExtend(EntityDef.DAO_REAL));
		return className;
	}



	@Override
	protected String buildField(EntityDef def, FieldDef fdef)
	{
		return "";
	}

}
package org.mef.tools.mgen.codegen.generators;

import org.mef.framework.sfx.SfxContext;
import org.mef.tools.mgen.parser.EntityDef;
import org.mef.tools.mgen.parser.FieldDef;
import org.stringtemplate.v4.ST;


public class SprigCodeGen extends CodeGenBase
{
	public SprigCodeGen(SfxContext ctx)
	{
		super(ctx);
	}
	
	@Override
	public String generate(EntityDef def)
	{
//		this.isExtended = def.shouldExtend(EntityDef.DAO_MOCK);
		String result = genHeader(); 
		ST st = _group.getInstanceOf("classdecl");
		
		st.add("name", getClassName(def));
		st.add("type", def.name);
		st.add("isParentOfExtended", this.isParentOfExtended);
		result += st.render(); 
		
		if (! isParentOfExtended)
		{
			result += genParseBegin(def);
			result += this.genFields(def);
			result += genParseEnd(def);
		}
		st = _group.getInstanceOf("endclassdecl");
		result += st.render(); 
		
		return result;
	}
	
	@Override
	public String getClassName(EntityDef def)
	{
		String className = def.name + "Sprig";
		className = makeClassName(className); //, def.shouldExtend(EntityDef.DAO_MOCK));
		return className;
	}
	
	protected String genParseBegin(EntityDef def)
	{
		ST st = _group.getInstanceOf("parsedecl");
		st.add("type", def.name);
		
		String result = st.render(); 
		return result;
	}
	protected String genParseEnd(EntityDef def)
	{
		ST st = _group.getInstanceOf("parseend");
		
		String result = st.render(); 
		return result;
	}
	
	
	@Override
	protected String buildField(EntityDef def, FieldDef fdef)
	{
		ST st = _group.getInstanceOf("parsefield");
//		st.add("type", def.name);
		st.add("fldName", fdef.name);
		
		String result = st.render(); 
		return result;
	}

}
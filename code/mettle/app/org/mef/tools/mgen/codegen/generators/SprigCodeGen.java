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
		String typeName = uppify(fdef.typeName); //long,Long
		if (fdef.typeName.equals("int"))
		{
			typeName = "Integer";
		}
		else if (fdef.typeName.equals("char"))
		{
			typeName = "Character";
		}
		
		ST st = _group.getInstanceOf("parse" + typeName);
		
		//we don't parse fields that are references to other models because this is handled by
		//the resolve step.
		//And other types (such as JodaTime types) are not supported here. Override parse() and handle
		//them yourself, or extend dao_sprig.stg
		if (st == null)
		{
			return "";
		}
		
//		st.add("type", def.name);
		st.add("fldName", fdef.name);
		st.add("setName", "set" + fdef.uname);
		
		String result = st.render(); 
		return result;
	}

}
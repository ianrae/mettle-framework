package org.mef.tools.scaffoldgen;

import org.mef.framework.sfx.SfxContext;
import org.mef.tools.mgen.codegen.generators.CodeGenBase;
import org.mef.tools.mgen.parser.EntityDef;
import org.mef.tools.mgen.parser.FieldDef;
import org.stringtemplate.v4.ST;

public class BoundaryCodeGen extends CodeGenBase
{
	public static String newline = System.getProperty("line.separator");

	public BoundaryCodeGen(SfxContext ctx)
	{
		super(ctx);
	}

	@Override
	public String generate(EntityDef def)
	{
		String result = genCustomHeader("XPresenter", "YReply", "ZBinder");

		ST st = _group.getInstanceOf("classdecl");
		st.add("type", def.name);
		st.add("name", getClassName(def));
		result += st.render(); 
		result += newline;
		st = _group.getInstanceOf("endclassdecl");
		result += st.render(); 

		return result;
	}
	
	protected String genCustomHeader(String presenter, String reply, String binder)
	{
		ST st = _group.getInstanceOf("header");
		
		st.add("package", _packageName);
		
		st.add("presenter", presenter);
		st.add("reply", reply);
		st.add("binder", binder);
		
		String result = st.render(); 
		return result;
	}

	@Override
	public String getClassName(EntityDef def)
	{
		return  def.name; //already contains boundary
	}

	@Override
	protected String buildField(EntityDef def, FieldDef fdef)
	{
		return "";
	}
}
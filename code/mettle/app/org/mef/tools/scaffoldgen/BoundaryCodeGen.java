package org.mef.tools.scaffoldgen;

import org.mef.framework.sfx.SfxContext;
import org.mef.tools.mgen.codegen.generators.CodeGenBase;
import org.mef.tools.mgen.parser.EntityDef;
import org.mef.tools.mgen.parser.FieldDef;
import org.stringtemplate.v4.ST;

public class BoundaryCodeGen extends CodeGenBase
{
	public static String newline = System.getProperty("line.separator");

	private String presenterName;
	private String replyName;
	private String binderName;

	private String inputName;
	
	public BoundaryCodeGen(SfxContext ctx, String presentNameToUse, String replyName, String binderName, String inputName)
	{
		super(ctx);
		this.presenterName = presentNameToUse;
		this.replyName = replyName;
		this.binderName = binderName;
		this.inputName = inputName;
	}

	@Override
	public String generate(EntityDef def)
	{
		String result = genCustomHeader(presenterName, replyName, binderName);

		//classdecl(type, presenter, reply, binder, inputType) ::= <<

		ST st = _group.getInstanceOf("classdecl");
		st.add("type", def.name);
		st.add("presenter", presenterName);
		st.add("reply", replyName);
		st.add("binder", binderName);
		st.add("inputType", inputName);
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
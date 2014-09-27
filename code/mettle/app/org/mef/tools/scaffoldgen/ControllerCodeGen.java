package org.mef.tools.scaffoldgen;

import org.mef.framework.sfx.SfxContext;
import org.mef.tools.mgen.codegen.generators.CodeGenBase;
import org.mef.tools.mgen.parser.EntityDef;
import org.mef.tools.mgen.parser.FieldDef;
import org.stringtemplate.v4.ST;

public class ControllerCodeGen extends CodeGenBase
{
	public static String newline = System.getProperty("line.separator");

	private String presenterName;
	private String replyName;
	private String boundaryName;

	private String inputName;
	
	public ControllerCodeGen(SfxContext ctx, String presentNameToUse, String replyName, String boundaryName, String inputName)
	{
		super(ctx);
		this.presenterName = presentNameToUse;
		this.replyName = replyName;
		this.boundaryName = boundaryName;
		this.inputName = inputName;
	}

	@Override
	public String generate(EntityDef def)
	{
		String result = genCustomHeader(presenterName, replyName, boundaryName);

		//classdecl(type, presenter, reply, binder, inputType) ::= <<

		ST st = _group.getInstanceOf("classdecl");
		st.add("type", def.name);
		st.add("presenter", presenterName);
		st.add("reply", replyName);
		st.add("boundary", boundaryName);
		result += st.render(); 
		result += newline;
		st = _group.getInstanceOf("endclassdecl");
		result += st.render(); 

		return result;
	}
	
	protected String genCustomHeader(String presenter, String reply, String boundary)
	{
		ST st = _group.getInstanceOf("header");
		
		st.add("package", _packageName);
		
		st.add("presenter", presenter);
		st.add("reply", reply);
		st.add("boundary", boundary);
		
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
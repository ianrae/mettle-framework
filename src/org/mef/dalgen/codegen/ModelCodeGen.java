package org.mef.dalgen.codegen;

import org.mef.dalgen.parser.EntityDef;
import org.mef.dalgen.parser.FieldDef;
import org.stringtemplate.v4.ST;

import sfx.SfxContext;

public class ModelCodeGen extends CodeGenBase
{
	public ModelCodeGen(SfxContext ctx, String path, String packageName)
	{
		super(ctx, path, packageName);
	}
	
	@Override
	public String generate(EntityDef def)
	{
		String result = genHeader(); 

		ST st = _group.getInstanceOf("classdecl");
		st.add("type", def.name);
		st.add("name", getClassName(def));
		result += st.render(); 
		
		result += genFields(def);
		
		st = _group.getInstanceOf("endclassdecl");
		result += st.render(); 
		
		return result;
	}
	@Override
	public String getClassName(EntityDef def)
	{
		String className = makeClassName(def.name + "Model", def.extendModel);
		return className;
	}
	
	
	@Override
	protected String buildField(EntityDef def, FieldDef fdef)
	{
		ST st = _group.getInstanceOf("fielddecl");
		String result = "";
		st.add("type", fdef.typeName);
		st.add("name", fdef.name);
		st.add("bigName", uppify(fdef.name));
		st.add("setName", isId(fdef.name) ? "set" : "set");
		st.add("isEntity", isEntity(def, fdef.name));
		result = st.render(); 

		String s = "";
		for(String ann : fdef.annotationL)
		{
			if (s.isEmpty())
			{
				s += "   ";
			}
			s += ann + " ";
		}
		if (! s.isEmpty())
		{
			s += "\n";
		}
		
		if (forUnitTest)
		{
			s = "//" + s;
		}
		
		result = s + result;
		return result;
	}

	private Object isEntity(EntityDef def, String name) 
	{
		for(EntityDef tmp : def.allEntityTypes)
		{
			if (tmp.name.equals(name))
				return true;
		}
		return false;
	}

}
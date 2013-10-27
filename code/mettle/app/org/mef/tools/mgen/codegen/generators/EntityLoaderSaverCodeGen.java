package org.mef.tools.mgen.codegen.generators;

import java.util.ArrayList;
import java.util.List;

import org.mef.framework.sfx.SfxContext;
import org.mef.tools.mgen.parser.EntityDef;
import org.mef.tools.mgen.parser.FieldDef;
import org.stringtemplate.v4.ST;


public class EntityLoaderSaverCodeGen extends CodeGenBase
{
	public EntityLoaderSaverCodeGen(SfxContext ctx)
	{
		super(ctx);
	}

	@Override
	public String generate(EntityDef def)
	{
		this.isExtended = true; //always
		String result = genHeader(def); 

		ST st = _group.getInstanceOf("classdecl");
		st.add("name", this.getClassName(def));
		result += st.render(); 

		for(EntityDef tmp: def.allEntityTypes)
		{
			if (tmp.enabled)
			{
				result += genReadEntity(tmp);
			}
		}


		st = _group.getInstanceOf("endclassdecl");
		result += st.render(); 

		return result;
	}

	protected String genHeader(EntityDef def)
	{
		ST st = _group.getInstanceOf("header");

		if (isExtended)
		{
			st.add("package", "mef.gen");
		}
		else
		{
			st.add("package", _packageName);
		}

		//			st.add("type", def.name);

		//			List<String> daoTypeL = new ArrayList<String>();
		//			for(FieldDef fdef : def.fieldL)
		//			{
		//				if (! isStandardJavaType(fdef.typeName ))
		//				{
		//					daoTypeL.add(fdef.typeName);
		//				}
		//			}
		//			st.add("types", daoTypeL);
		String result = st.render(); 
		return result;
	}

	private boolean isStandardJavaType(String typeName)
	{
		String[] ar = new String[] { "Integer", "Long", "String", "int", "Integer", "long", "boolean" };
		for(int i = 0; i < ar.length; i++)
		{
			if (ar[i].equals(typeName))
			{
				return true;
			}
		}
		return false;
	}

	protected String genReadEntity(EntityDef def)
	{
		ST st = _group.getInstanceOf("saveorupdate");
		st.add("type", def.name);
		st.add("fields", this.buildFieldsList(def));

		String result = st.render(); 
		return result;
	}
	private Object buildFieldsList(EntityDef def) 
	{
		ArrayList<FieldDef> L = new ArrayList<FieldDef>();
		for(FieldDef fdef : def.fieldL)
		{
			if (fdef.name.equals("id"))
			{}
			else
			{
				L.add(fdef);
			}
		}
		return L;
	}




	@Override
	public String getClassName(EntityDef def)
	{
		return this.makeClassName("EntityLoaderSaver", true);
	}


	@Override
	protected String buildField(EntityDef def, FieldDef fdef)
	{
		return "";
	}
}
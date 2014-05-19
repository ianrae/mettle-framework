package org.mef.tools.mgen.codegen.generators;

import java.util.ArrayList;
import java.util.List;

import org.mef.framework.sfx.SfxContext;
import org.mef.tools.mgen.parser.EntityDef;
import org.mef.tools.mgen.parser.FieldDef;
import org.stringtemplate.v4.ST;


public class DaoEntityLoaderCodeGen extends CodeGenBase
{
	public DaoEntityLoaderCodeGen(SfxContext ctx)
	{
		super(ctx);
	}

	@Override
	public String generate(EntityDef def)
	{
		//		this.isExtended = true; //always
		String result = genHeader(def); 

		ST st = _group.getInstanceOf("classdecl");
		st.add("name", this.getClassName(def));
		st.add("isParentOfExtended", this.isParentOfExtended);
		result += st.render(); 

		if (! this.isParentOfExtended)
		{
			for(EntityDef tmp: def.allEntityTypes)
			{
				if (tmp.enabled)
				{
					result += genReadEntity(tmp);
					result += genLoadEntity(tmp);
					result += genFindWithIdEntity(tmp);
				}
			}
		}

		st = _group.getInstanceOf("endclassdecl");
		result += st.render(); 

		return result;
	}

	protected String genHeader(EntityDef def)
	{
		ST st = _group.getInstanceOf("header");

		if (this.isExtended())
		{
			st.add("package", "mef.gen");
		}
		else
		{
			st.add("package", _packageName);
		}
		st.add("extras", this.extraImportsL);

		//		st.add("type", def.name);

		//		List<String> daoTypeL = new ArrayList<String>();
		//		for(FieldDef fdef : def.fieldL)
		//		{
		//			if (! isStandardJavaType(fdef.typeName ))
		//			{
		//				daoTypeL.add(fdef.typeName);
		//			}
		//		}
		//		st.add("types", daoTypeL);
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
		ST st = _group.getInstanceOf("readentity");
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
			else if (! fdef.isReadOnly)
			{
				L.add(fdef);
			}
		}
		return L;
	}


	protected String genLoadEntity(EntityDef def)
	{
		ST st = _group.getInstanceOf("loadentity");

		st.add("type", def.name);
		String result = st.render(); 
		return result;
	}
	protected String genFindWithIdEntity(EntityDef def)
	{
		ST st = _group.getInstanceOf("findwithidentity");

		st.add("type", def.name);
		String result = st.render(); 
		return result;
	}




	@Override
	public String getClassName(EntityDef def)
	{
		return this.makeClassName("DaoJsonLoader"); //, true);
	}


	@Override
	protected String buildField(EntityDef def, FieldDef fdef)
	{
		return "";
	}
}

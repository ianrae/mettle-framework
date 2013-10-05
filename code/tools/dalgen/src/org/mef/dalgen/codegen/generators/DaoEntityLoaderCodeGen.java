package org.mef.dalgen.codegen.generators;

import java.util.ArrayList;
import java.util.List;

import org.mef.dalgen.parser.EntityDef;
import org.mef.dalgen.parser.FieldDef;
import org.stringtemplate.v4.ST;

import sfx.SfxContext;

public class DaoEntityLoaderCodeGen extends CodeGenBase
{
	public DaoEntityLoaderCodeGen(SfxContext ctx)
	{
		super(ctx);
	}

	@Override
	public String generate(EntityDef def)
	{
		this.isExtended = def.extendEntity;
		String result = genHeader(def); 

		ST st = _group.getInstanceOf("classdecl");
		result += st.render(); 
		result += genReadEntity(def);
		result += genLoadEntity(def);
		result += genFindWithIdEntity(def);

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

		List<String> daoTypeL = new ArrayList<String>();
		for(FieldDef fdef : def.fieldL)
		{
			if (! isStandardJavaType(fdef.typeName ))
			{
				daoTypeL.add(fdef.typeName);
			}
		}
		st.add("types", daoTypeL);
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
		String result = st.render(); 
		return result;
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
		return this.makeClassName(def.name, def.extendEntity);
	}


	@Override
	protected String buildField(EntityDef def, FieldDef fdef)
	{
		return "";
	}
}

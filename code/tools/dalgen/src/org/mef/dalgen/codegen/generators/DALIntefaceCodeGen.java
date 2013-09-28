package org.mef.dalgen.codegen.generators;

import org.mef.dalgen.parser.EntityDef;
import org.mef.dalgen.parser.FieldDef;
import org.stringtemplate.v4.ST;

import sfx.SfxContext;

public class DALIntefaceCodeGen extends CodeGenBase
	{
		public DALIntefaceCodeGen(SfxContext ctx, String path, String packageName)
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
			
			result += genQueries(def);
			result += genMethods(def);
			
			st = _group.getInstanceOf("endclassdecl");
			result += st.render(); 
			
			return result;
		}
		
		
		@Override
		public String getClassName(EntityDef def)
		{
			String s = "I" + uppify(def.name) + "DAL";
			return makeClassName(s, def.extendInterface);
		}
		
		
		protected String genQueries(EntityDef def)
		{
			String result = "";
			for(String query : def.queryL)
			{
				ST st = _group.getInstanceOf("querydecl");
				String fieldName = getFieldName(query);
				st.add("type", def.name); //getFieldType(def, fieldName));
				st.add("fieldType", getFieldType(def, fieldName));
				st.add("fullName", query);
				result = st.render(); 
				result += "\n\n";
			}
			return result;
		}
		
		protected String genMethods(EntityDef def)
		{
			String result = "";
			for(String method : def.methodL)
			{
				ST st = _group.getInstanceOf("methoddecl");
				st.add("meth", method);
				result = st.render(); 
				result += "\n\n";
			}
			return result;
		}

		@Override
		protected String buildField(EntityDef def, FieldDef fdef) {
			// TODO Auto-generated method stub
			return null;
		}
	}
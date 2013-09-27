package org.mef.dalgen.codegen;

import org.mef.dalgen.parser.EntityDef;
import org.mef.dalgen.parser.FieldDef;
import org.stringtemplate.v4.ST;

import sfx.SfxContext;

public class EntityCodeGen extends CodeGenBase
	{
		public EntityCodeGen(SfxContext ctx, String path, String packageName)
		{
			super(ctx, path, packageName);
		}
		
		@Override
		public String generate(EntityDef def)
		{
			String result = genHeader(); 
			
			ST st = _group.getInstanceOf("classdecl");
			st.add("type", "int");
			st.add("name", makeClassName(def.name, def.extendEntity));
			result += st.render(); 
			
			result += genFields(def);
			
			st = _group.getInstanceOf("endclassdecl");
			result += st.render(); 
			
			return result;
		}
		
		
		@Override
		protected String buildField(FieldDef fdef)
		{
			ST st = _group.getInstanceOf("fielddecl");
			String result = "";
			st.add("type", fdef.typeName);
			st.add("name", fdef.name);
			result = st.render(); 

			String s = "";
			result = s + result;
			return result;
		}
	}
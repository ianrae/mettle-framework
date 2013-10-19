package org.mef.tools.mgen.codegen.generators;

import java.util.ArrayList;
import java.util.List;

import org.mef.framework.sfx.SfxContext;
import org.mef.tools.mgen.parser.EntityDef;
import org.mef.tools.mgen.parser.FieldDef;
import org.stringtemplate.v4.ST;


public class EntityCodeGen extends CodeGenBase
	{
		public EntityCodeGen(SfxContext ctx)
		{
			super(ctx);
		}
		
		@Override
		public String generate(EntityDef def)
		{
			this.isExtended = def.extendEntity;
			String result = genHeader(); 
			
			ST st = _group.getInstanceOf("classdecl");
			st.add("type", "int");
			st.add("name", getClassName(def));
			st.add("args", buildArgList(def));
			st.add("inits", buildCtorInitsList(def));
			st.add("isNotExtended", ! this.isExtended);
			
			result += st.render(); 
			
			result += genFields(def);
			
			st = _group.getInstanceOf("endclassdecl");
			result += st.render(); 
			
			return result;
		}
		
		private Object buildCtorInitsList(EntityDef def) 
		{
			ArrayList<String> L = new ArrayList<String>();
			for(FieldDef fdef : def.fieldL)
			{
				if (fdef.name.equals("id"))
				{}
				else
				{
					String s = String.format("this.%s = %s;", fdef.name, fdef.name);
					L.add(s);
				}
			}
			return L;
		}

		private List<String> buildArgList(EntityDef def)
		{
			ArrayList<String> L = new ArrayList<String>();
			for(FieldDef fdef : def.fieldL)
			{
				if (fdef.name.equals("id"))
				{}
				else
				{
					String s = String.format("%s %s", fdef.typeName, fdef.name);
					L.add(s);
				}
			}
			return L;
		}
		
		@Override
		public String getClassName(EntityDef def)
		{
			return this.makeClassName(def.name, def.extendEntity);
		}
		
		
		@Override
		protected String buildField(EntityDef def, FieldDef fdef)
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
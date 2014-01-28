package org.mef.tools.mgen.codegen.generators;

import java.util.ArrayList;
import java.util.List;

import org.mef.framework.sfx.SfxContext;
import org.mef.tools.mgen.parser.EntityDef;
import org.mef.tools.mgen.parser.FieldDef;
import org.stringtemplate.v4.ST;


public class ThingParserCodeGen extends CodeGenBase
	{
		public ThingParserCodeGen(SfxContext ctx)
		{
			super(ctx);
		}
		
		@Override
		public String generate(EntityDef def)
		{
//			this.isExtended = def.shouldExtend(EntityDef.ENTITY);
			String result = genHeader(); 
			
			ST st = _group.getInstanceOf("classdecl");
			st.add("type", "int");
			st.add("name", getClassName(def));
//			st.add("args", buildArgList(def));
//			st.add("inits", buildCtorInitsList(def, false));
//			st.add("copyinits", buildCtorInitsList(def, true));
			st.add("isParentOfExtended", this.isParentOfExtended);
			
			result += st.render(); 
			
			if (! isParentOfExtended)
			{
				result += genFields(def);
			}
			st = _group.getInstanceOf("endclassdecl");
			result += st.render(); 
			
			return result;
		}
		
		
		@Override
		public String getClassName(EntityDef def)
		{
			return this.makeClassName(def.name + "Parser");
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
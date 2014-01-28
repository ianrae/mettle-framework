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
				result += genOnParse(def);
				result += genOnRender(def);
			}
			
			st = _group.getInstanceOf("endclassdecl");
			result += st.render(); 
			
			return result;
		}
		
		private String genOnParse(EntityDef def) 
		{
			String result = "";
			ST st = _group.getInstanceOf("onparse");
			st.add("type", def.name);

			List<String> assignsL = new ArrayList<String>();
			for(FieldDef fdef : def.fieldL)
			{
				if (fdef.name.equals("id"))
				{
					continue;
				}

				String helperName = getHelperName(fdef);
				String s = String.format("target.%s = helper.%s(\"%s\");", fdef.name, helperName, fdef.name);
				assignsL.add(s);
			}
			st.add("assigns", assignsL);

			result += st.render(); 
			result += "\n\n";
			return result;
		}
		
		private String genOnRender(EntityDef def) 
		{
			String result = "";
			ST st = _group.getInstanceOf("onrender");
			st.add("type", def.name);

			List<String> assignsL = new ArrayList<String>();
			for(FieldDef fdef : def.fieldL)
			{
				if (fdef.name.equals("id"))
				{
					continue;
				}

				String s;
				if (fdef.getDateType())
				{
					s = String.format("obj.put(\"%s\", ParserHelper.dateToString(target.%s));", fdef.name, fdef.name);
				}
				else
				{
					s = String.format("obj.put(\"%s\", target.%s);", fdef.name, fdef.name);
				}
				assignsL.add(s);
			}
			st.add("assigns", assignsL);

			result += st.render(); 
			result += "\n\n";
			return result;
		}
		
		private String getHelperName(FieldDef fdef) 
		{
			if (fdef.getBooleanType())
			{
				return "getDate";
			}
			if (fdef.getIntType())
			{
				return "getInt";
			}
			if (fdef.getDateType())
			{
				return "getDate";
			}
			else if (fdef.getStringType())
			{
				return "getString";
			}
			else
			{
				return "getString";
			}
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
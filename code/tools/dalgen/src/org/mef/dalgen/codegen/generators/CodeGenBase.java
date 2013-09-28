package org.mef.dalgen.codegen.generators;

import org.mef.dalgen.parser.EntityDef;
import org.mef.dalgen.parser.FieldDef;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import sfx.SfxBaseObj;
import sfx.SfxContext;

public abstract class CodeGenBase extends SfxBaseObj
	{
//		private String _path;
		protected STGroup _group;
		public String _packageName;
		
		public boolean forUnitTest;

		public CodeGenBase(SfxContext ctx, String path, String packageName)
		{
			super(ctx);
//			_path = path;
			_group = new STGroupFile(path);
			_packageName = packageName;
		}
		
		
		protected String genFields(EntityDef def)
		{
			
			String result = "";
			for(FieldDef fdef : def.fieldL)
			{
				result += this.buildField(def, fdef);
				result += "\n\n";
			}
			return result;
		}
		
		public abstract String generate(EntityDef def);
		
		protected abstract String buildField(EntityDef def, FieldDef fdef);
		public abstract String getClassName(EntityDef def);
		
		protected boolean isId(String name) 
		{
			return (name.equals("id"));
		}

		protected Object uppify(String name) 
		{
			String upper = name.toUpperCase();
			String s = upper.substring(0, 1);
			s += name.substring(1);
			return s;
		}
		
		protected String getFieldType(EntityDef def, String fieldName)
		{
			for(FieldDef fdef : def.fieldL)
			{
				if (fdef.name.equals(fieldName))
				{
					return fdef.typeName;
				}
				
			}
			return "?";
		}
		protected String getFieldName(String query)
		{
			String target = "find_by_"; //support find_all later!
			int pos = query.indexOf(target);
			return query.substring(pos + target.length());
		}
		
		protected String genHeader()
		{
			return genHeader(null);
		}
		protected String genHeader(String type)
		{
			ST st = _group.getInstanceOf("header");
			st.add("package", _packageName);
			if (type != null)
			{
				st.add("type", type);
			}
			String result = st.render(); 
			return result;
		}
		
		
		protected String makeClassName(String name, boolean extend)
		{
			String s = name;
			if (extend)
			{
				s += "_GEN";
			}
			return s;
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
	}
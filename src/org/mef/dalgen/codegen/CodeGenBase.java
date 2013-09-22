package org.mef.dalgen.codegen;

import org.mef.dalgen.parser.EntityDef;
import org.mef.dalgen.parser.FieldDef;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import sfx.SfxBaseObj;
import sfx.SfxContext;

public abstract class CodeGenBase extends SfxBaseObj
	{
//		private String _path;
		protected STGroup _group;

		public CodeGenBase(SfxContext ctx, String path)
		{
			super(ctx);
//			_path = path;
			_group = new STGroupFile(path);
		}
		
		
		protected String genFields(EntityDef def)
		{
			
			String result = "";
			for(FieldDef fdef : def.fieldL)
			{
				result += this.buildField(fdef);
				result += "\n\n";
			}
			return result;
		}
		
		protected abstract String buildField(FieldDef fdef);
		
		
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
	}
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
	}
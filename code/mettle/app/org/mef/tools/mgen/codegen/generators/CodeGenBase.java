package org.mef.tools.mgen.codegen.generators;

import java.util.ArrayList;
import java.util.List;

import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;
import org.mef.tools.mgen.parser.EntityDef;
import org.mef.tools.mgen.parser.FieldDef;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;


public abstract class CodeGenBase extends SfxBaseObj
	{
//		private String _path;
		protected STGroup _group;
		public String _packageName;
		private boolean isExtended;
		protected boolean isParentOfExtended;
		protected boolean userCanModifyFlag;
		public List<String> extraImportsL = new ArrayList<String>();

		public boolean forUnitTest;

		public CodeGenBase(SfxContext ctx, String path, String packageName)
		{
			super(ctx);
//			_path = path;
			_group = new STGroupFile(path);
			_packageName = packageName;
		}
		public CodeGenBase(SfxContext ctx)
		{
			super(ctx);
		}
		
		
		protected List<String> getExtraImports() 
		{
			return extraImportsL;
		}
		
		
		public void init(String path, String packageName)
		{
			_group = new STGroupFile(path);
			_packageName = packageName;
		}
		
		protected String genFields(EntityDef def)
		{
			if (this.isParentOfExtended)
			{
				return ""; //want empty class
			}
			
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
		
		public boolean isExtended()
		{
			return isExtended; //only during generate is this valid
		}
		public void setExtended(boolean b)
		{
			isExtended = b;
		}
		public void setIsParentOfExtended(boolean b)
		{
			isParentOfExtended = b;
		}
		
		protected boolean isId(String name) 
		{
			return (name.equals("id"));
		}

		protected String uppify(String name) 
		{
			String upper = name.toUpperCase();
			String s = upper.substring(0, 1);
			s += name.substring(1);
			return s;
		}
		
		protected String lowify(String name) 
		{
			String upper = name.toLowerCase();
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
			
			if (isExtended)
			{
				st.add("package", "mef.gen");
			}
			else
			{
				st.add("package", _packageName);
			}
			
			if (type != null)
			{
				st.add("type", type);
			}
			
			st.add("extras", this.extraImportsL);
			st.add("willBeOverwritten", ! this.userCanModifyFlag);
			String result = st.render(); 
			return result;
		}
		
		
		protected String makeClassName(String name)
		{
			String s = name;
			if (this.isExtended)
			{
				s += "_GEN";
			}
			return s;
		}
		
		protected String genMethods(EntityDef def, boolean areDefining)
		{
			String result = "";
			for(String method : def.methodL)
			{
				ST st = _group.getInstanceOf("methoddecl");
				st.add("meth", method);
				
				if (areDefining)
				{
					String retVal = caclRetVal(method);
					st.add("retVal", retVal);
				}
				
				result += st.render(); 
				result += "\n\n";
			}
			return result;
		}
		
		private String caclRetVal(String method)
		{
			//List<Task> search_by_name(String name)
			method = method.trim();
			if (method.startsWith("boolean"))
			{
				return "false";
			}
			else if (method.startsWith("int"))
			{
				return "0";
			}
			else if (method.startsWith("long"))
			{
				return "0L";
			}
			else if (method.startsWith("void"))
			{
				return "";
			}
			else 
			{
				return "null";
			}
		}
		
		
		protected boolean isEntity(EntityDef def, String name) 
		{
			for(EntityDef tmp : def.allEntityTypes)
			{
				if (tmp.name.equals(name))
					return true;
			}
			return false;
		}
		public void setUserCanModifyFlag(boolean userCanModifyFlag) 
		{
			this.userCanModifyFlag = userCanModifyFlag;
		}
		
		
		protected String getOneToManyFieldType(String query)
		{
			int pos1 = query.indexOf('<');
			int pos2 = query.indexOf('>');
			while (true)
			{
				int pos3 = query.indexOf('.', pos1);
				if (pos3 < pos2)
				{
					pos1 = pos3 + 1;
				}
				else
				{
					break;
				}
			}
			String fieldType = query.substring(pos1, pos2);
			
			if (fieldType.endsWith("Model"))
			{
				//AbcModel
				fieldType = fieldType.substring(0, fieldType.length() - 5);
			}
			
			return fieldType;
		}
		
		protected String getOneToManyFieldName(String query)
		{
			//and now fieldname
			int pos1 = query.lastIndexOf('.');
			String fieldName = query.substring(pos1 + 1);
			return fieldName;
		}
		
	}
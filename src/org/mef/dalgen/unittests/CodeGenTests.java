package org.mef.dalgen.unittests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mef.dalgen.parser.DalGenXmlParser;
import org.mef.dalgen.parser.EntityDef;
import org.mef.dalgen.parser.FieldDef;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import sfx.SfxBaseObj;
import sfx.SfxContext;

public class CodeGenTests extends BaseTest
{
	public abstract static class CodeGenBase extends SfxBaseObj
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
	
	public static class EntityCodeGen extends CodeGenBase
	{
		public EntityCodeGen(SfxContext ctx, String path)
		{
			super(ctx, path);
		}
		
		public String generate(EntityDef def)
		{
			ST st = _group.getInstanceOf("classdecl");
			st.add("type", "int");
			st.add("name", def.name);
			String result = st.render(); 
			
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
//			for(String ann : fdef.annotationL)
//			{
//				if (s.isEmpty())
//				{
//					s += "   ";
//				}
//				s += ann + " ";
//			}
//			if (! s.isEmpty())
//			{
//				s += "\n";
//			}
			
			result = s + result;
			return result;
		}
	}
	
	
	public static class ModelCodeGen extends CodeGenBase
	{
		public ModelCodeGen(SfxContext ctx, String path)
		{
			super(ctx, path);
		}
		
		public String generate(EntityDef def)
		{
			ST st = _group.getInstanceOf("classdecl");
			st.add("type", "int");
			st.add("name", def.name);
			String result = st.render(); 
			
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
//			for(String ann : fdef.annotationL)
//			{
//				if (s.isEmpty())
//				{
//					s += "   ";
//				}
//				s += ann + " ";
//			}
//			if (! s.isEmpty())
//			{
//				s += "\n";
//			}
			
			result = s + result;
			return result;
		}
	}
	
	
	
	@Test
	public void testEntity() throws Exception
	{
		log("--testEntity--");
		createContext();
		EntityDef def = readEntityDef();
		
		String path = this.getTestFile("entity.stg");
		EntityCodeGen gen = new EntityCodeGen(_ctx, path);
		String code = gen.generate(def);	
		log(code);
		assertEquals(true, 10 < code.length());
	}
	
	@Test
	public void testModel() throws Exception
	{
		log("--testModel--");
		createContext();
		EntityDef def = readEntityDef();
		
		String path = this.getTestFile("entity.stg");
		ModelCodeGen gen = new ModelCodeGen(_ctx, path);
		String code = gen.generate(def);	
		log(code);
		assertEquals(true, 10 < code.length());
	}
	
	//--- helper fns ---
	private EntityDef readEntityDef() throws Exception
	{
		String path = this.getTestFile("dalgen.xml");
		DalGenXmlParser parser = new DalGenXmlParser(_ctx);
		boolean b = parser.parse(path);

		assertEquals(1, parser._entityL.size());
		return parser._entityL.get(0);
	}
}

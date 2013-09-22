package org.mef.dalgen.unittests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mef.dalgen.codegen.CodeGenBase;
import org.mef.dalgen.codegen.EntityCodeGen;
import org.mef.dalgen.parser.DalGenXmlParser;
import org.mef.dalgen.parser.EntityDef;
import org.mef.dalgen.parser.FieldDef;
import org.stringtemplate.v4.ST;

import sfx.SfxContext;

public class CodeGenTests extends BaseTest
{
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
			st.add("name", def.name + "Model");
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
			st.add("bigName", uppify(fdef.name));
			st.add("setName", isId(fdef.name) ? "force" : "set");
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

		private boolean isId(String name) 
		{
			return (name.equals("id"));
		}

		private Object uppify(String name) 
		{
			String upper = name.toUpperCase();
			String s = upper.substring(0, 1);
			s += name.substring(1);
			return s;
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
		
		String path = this.getTestFile("model.stg");
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

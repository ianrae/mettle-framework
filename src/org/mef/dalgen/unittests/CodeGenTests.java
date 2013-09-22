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
			for(String ann : fdef.annotationL)
			{
				if (s.isEmpty())
				{
					s += "   ";
				}
				s += ann + " ";
			}
			if (! s.isEmpty())
			{
				s += "\n";
			}
			
			result = s + result;
			return result;
		}

	}
	
	public static class DALIntefaceCodeGen extends CodeGenBase
	{
		public DALIntefaceCodeGen(SfxContext ctx, String path)
		{
			super(ctx, path);
		}
		
		public String generate(EntityDef def)
		{
			ST st = _group.getInstanceOf("classdecl");
			st.add("type", def.name);
			st.add("bigName", uppify(def.name));
			String result = st.render(); 
			
			result += genQueries(def);
			
//			st = _group.getInstanceOf("endclassdecl");
//			result += st.render(); 
			
			return result;
		}
		
		protected String genQueries(EntityDef def)
		{
			String result = "";
			for(String query : def.queryL)
			{
				ST st = _group.getInstanceOf("querydecl");
				st.add("type", "Stringx");
				st.add("name", "label");
				st.add("fullName", query);
				result = st.render(); 
				result += "\n\n";
			}
			return result;
		}

		@Override
		protected String buildField(FieldDef fdef) {
			// TODO Auto-generated method stub
			return null;
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
	
	@Test
	public void testIDAL() throws Exception
	{
		log("--testIDAL--");
		createContext();
		EntityDef def = readEntityDef();
		
		String path = this.getTestFile("dal_interface.stg");
		DALIntefaceCodeGen gen = new DALIntefaceCodeGen(_ctx, path);
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

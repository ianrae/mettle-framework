package org.mef.dalgen.unittests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mef.dalgen.codegen.DALIntefaceCodeGen;
import org.mef.dalgen.codegen.EntityCodeGen;
import org.mef.dalgen.codegen.MockDALCodeGen;
import org.mef.dalgen.codegen.ModelCodeGen;
import org.mef.dalgen.parser.DalGenXmlParser;
import org.mef.dalgen.parser.EntityDef;

import sfx.SfxTextWriter;


public class CodeGenTests extends BaseTest
{
	private static boolean genFiles = true;
	
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
		
		writeFile("Task", code);
		
	}
	@Test
	public void testModel() throws Exception
	{
		log("--testModel--");
		createContext();
		EntityDef def = readEntityDef();
		
		String path = this.getTestFile("model.stg");
		ModelCodeGen gen = new ModelCodeGen(_ctx, path);
		gen.forUnitTest = true;
		String code = gen.generate(def);	
		log(code);
		assertEquals(true, 10 < code.length());
		
		writeFile("TaskModel", code);
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
		writeFile("ITaskDAL", code);
	}
	
	@Test
	public void testMockDAL() throws Exception
	{
		log("--testMockDAL--");
		createContext();
		EntityDef def = readEntityDef();
		
		String path = this.getTestFile("dal_mock.stg");
		MockDALCodeGen gen = new MockDALCodeGen(_ctx, path);
		String code = gen.generate(def);	
		log(code);
		assertEquals(true, 10 < code.length());
		writeFile("MockTaskDAL", code);
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

	private void writeFile(String fileName, String code)
	{
		if (! genFiles)
		{
			return;
		}
		
		String outPath = this.getUnitTetDir(String.format("gen\\%s.java", fileName));
		log(fileName + ": " + outPath);
		SfxTextWriter w = new SfxTextWriter(outPath, null);
		w.addLine(code);
		boolean b = w.writeFile();
		assertEquals(true, b);
		
	}

}

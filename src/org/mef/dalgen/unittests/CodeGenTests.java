package org.mef.dalgen.unittests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mef.dalgen.codegen.DALIntefaceCodeGen;
import org.mef.dalgen.codegen.DALUtilsCodeGen;
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
		String packageName = "org.mef.dalgen.unittests.gen";
		EntityCodeGen gen = new EntityCodeGen(_ctx, path, packageName);
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
		String packageName = "org.mef.dalgen.unittests.gen";
		ModelCodeGen gen = new ModelCodeGen(_ctx, path, packageName);
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
		String packageName = "org.mef.dalgen.unittests.gen";
		DALIntefaceCodeGen gen = new DALIntefaceCodeGen(_ctx, path, packageName);
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
		String packageName = "org.mef.dalgen.unittests.gen";
		MockDALCodeGen gen = new MockDALCodeGen(_ctx, path, packageName);
		String code = gen.generate(def);	
		log(code);
		assertEquals(true, 10 < code.length());
		writeFile("MockTaskDAL", code);
	}
	
	
//	@Test
//	public void testDALUtils() throws Exception
//	{
//		log("--testDALUtils--");
//		createContext();
//		EntityDef def = readEntityDef();
//		
//		String path = this.getTestFile("dal_utils.stg");
//		String packageName = "org.mef.dalgen.unittests.gen";
//		DALUtilsCodeGen gen = new DALUtilsCodeGen(_ctx, path, packageName);
//		String code = gen.generate(def);	
//		log(code);
//		assertEquals(true, 10 < code.length());
//		writeFile("TaskDALUtils", code);
//	}
	
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
		
//		String outPath = this.getUnitTestDir(String.format("gen\\%s.java", fileName));
//		log(fileName + ": " + outPath);
//		SfxTextWriter w = new SfxTextWriter(outPath, null);
//		w.addLine(code);
//		boolean b = w.writeFile();
//		assertEquals(true, b);
		
	}

}

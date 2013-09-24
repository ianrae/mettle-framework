package org.mef.dalgen.unittests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mef.dalgen.codegen.EntityCodeGen;
import org.mef.dalgen.parser.DalGenXmlParser;
import org.mef.dalgen.parser.EntityDef;

import sfx.SfxBaseObj;
import sfx.SfxContext;
import sfx.SfxTextWriter;

public class SampleAppCodeGenTests extends BaseTest
{
	public static class DalCodeGenerator extends SfxBaseObj
	{
		public DalCodeGenerator(SfxContext ctx)
		{
			super(ctx);
		}
		
		public boolean generate(String appDir, String stDir) throws Exception
		{
			EntityDef def = readEntityDef(appDir);
			
			String path = this.pathCombine(stDir, "entity.stg");
			String packageName = "org.mef.dalgen.unittests.gen";
			EntityCodeGen gen = new EntityCodeGen(_ctx, path, packageName);
			String code = gen.generate(def);	
			log(code);
			
			writeFile(appDir, "app\\mef\\entities", "Task", code);
			
			return true;
		}
		private EntityDef readEntityDef(String appDir) throws Exception
		{
			String path = this.pathCombine(appDir, "mef.xml");
			DalGenXmlParser parser = new DalGenXmlParser(_ctx);
			boolean b = parser.parse(path);

			assertEquals(1, parser._entityL.size());
			return parser._entityL.get(0);
		}

		private void writeFile(String appDir, String subDir, String fileName, String code)
		{
//			if (! genFiles)
//			{
//				return;
//			}
			
			String outPath = this.pathCombine(appDir, subDir);
			outPath = this.pathCombine(outPath, String.format("gen\\%s.java", fileName));
			log(fileName + ": " + outPath);
//			SfxTextWriter w = new SfxTextWriter(outPath, null);
//			w.addLine(code);
//			boolean b = w.writeFile();
//			assertEquals(true, b);
			
		}
		
	}
	
	
	
	
	@Test
	public void testEntity() throws Exception
	{
		log("--testEntity--");
		createContext();
		DalCodeGenerator gen = new DalCodeGenerator(_ctx);
		String appDir = "C:\\Users\\ian\\Documents\\GitHub\\dalgen\\src\\samples\\MyFirstApp";
		String stDir = this.getUnitTestDir("testfiles");
		boolean b = gen.generate(appDir, stDir);
	}

}

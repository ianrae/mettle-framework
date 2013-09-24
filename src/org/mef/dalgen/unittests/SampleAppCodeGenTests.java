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
		
		public boolean generate()
		{
			
			return true;
		}
	}
	
	
	
	
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
//		if (! genFiles)
//		{
//			return;
//		}
		
		String outPath = this.getUnitTetDir(String.format("gen\\%s.java", fileName));
		log(fileName + ": " + outPath);
		SfxTextWriter w = new SfxTextWriter(outPath, null);
		w.addLine(code);
		boolean b = w.writeFile();
		assertEquals(true, b);
		
	}
	
}

package org.mef.dalgen.unittests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mef.dalgen.codegen.CodeGenBase;
import org.mef.dalgen.codegen.DALIntefaceCodeGen;
import org.mef.dalgen.codegen.EntityCodeGen;
import org.mef.dalgen.codegen.ModelCodeGen;
import org.mef.dalgen.parser.DalGenXmlParser;
import org.mef.dalgen.parser.EntityDef;

import sfx.SfxBaseObj;
import sfx.SfxContext;
import sfx.SfxTextWriter;

public class SampleAppCodeGenTests extends BaseTest
{
	public static class DalCodeGenerator extends SfxBaseObj
	{
		private String appDir;
		private String stDir;
		
		public DalCodeGenerator(SfxContext ctx)
		{
			super(ctx);
		}
		
		public boolean generate(String appDir, String stDir) throws Exception
		{
			this.appDir = appDir;
			this.stDir = stDir;
			EntityDef def = readEntityDef(appDir);
			
			String path = this.pathCombine(stDir, "entity.stg");
			EntityCodeGen gen = new EntityCodeGen(_ctx, path, "mef.entities");
			boolean b = generateOneFile(def, gen, "Task", "app\\mef\\entities");
			if (!b )
			{
				return false; //!!
			}

			path = this.pathCombine(stDir, "model.stg");
			ModelCodeGen gen2 = new ModelCodeGen(_ctx, path, "models");
			b = generateOneFile(def, gen2, "TaskModel", "app\\models");
			if (!b )
			{
				return false; //!!
			}
			
			path = this.pathCombine(stDir, "dal_interface.stg");
			DALIntefaceCodeGen gen3 = new DALIntefaceCodeGen(_ctx, path, "mef.dals");
			b = generateOneFile(def, gen3, "ITaskDAL", "app\\mef\\dals");
			if (!b )
			{
				return false; //!!
			}
			
			return b;
		}
		private boolean generateOneFile(EntityDef def, CodeGenBase gen, String className, String relPath) throws Exception
		{
			String code = gen.generate(def);	
			//log(code);
			
			return writeFile(appDir, relPath, className, code);
		}
		private EntityDef readEntityDef(String appDir) throws Exception
		{
			String path = this.pathCombine(appDir, "mef.xml");
			DalGenXmlParser parser = new DalGenXmlParser(_ctx);
			boolean b = parser.parse(path);

			assertEquals(1, parser._entityL.size());
			return parser._entityL.get(0);
		}

		private boolean writeFile(String appDir, String subDir, String fileName, String code)
		{
			String outPath = this.pathCombine(appDir, subDir);
			outPath = this.pathCombine(outPath, String.format("%s.java", fileName));
			log(fileName + ": " + outPath);
			SfxTextWriter w = new SfxTextWriter(outPath, null);
			w.addLine(code);
			boolean b = w.writeFile();
			return b;
		}
		
	}
	
	
	
	
	@Test
	public void testEntity() throws Exception
	{
		log("--testEntity--");
		createContext();
		DalCodeGenerator gen = new DalCodeGenerator(_ctx);
		String appDir = this.getCurrentDir("src\\samples\\MyFirstApp");
		String stDir = this.getUnitTestDir("testfiles");
		boolean b = gen.generate(appDir, stDir);
		
	}

}

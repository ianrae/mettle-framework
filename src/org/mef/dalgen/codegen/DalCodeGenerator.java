package org.mef.dalgen.codegen;

import static org.junit.Assert.assertEquals;

import org.mef.dalgen.parser.DalGenXmlParser;
import org.mef.dalgen.parser.EntityDef;

import sfx.SfxBaseObj;
import sfx.SfxContext;
import sfx.SfxTextWriter;

public class DalCodeGenerator extends SfxBaseObj
{
	private String appDir;
	private String stDir;
	private DalGenXmlParser parser;
	
	public DalCodeGenerator(SfxContext ctx)
	{
		super(ctx);
	}
	
	public int init(String appDir, String stDir) throws Exception
	{
		this.appDir = appDir;
		this.stDir = stDir;
		parser = readEntityDef(appDir);
		return parser._entityL.size();
	}
	
	public boolean generate(int index) throws Exception
	{
		EntityDef def = parser._entityL.get(index);
		String name = def.name;
		
		String path = this.pathCombine(stDir, "entity.stg");
		EntityCodeGen gen = new EntityCodeGen(_ctx, path, "mef.entities");
		boolean b = generateOneFile(def, gen, name, "app\\mef\\entities");
		if (!b )
		{
			return false; //!!
		}

		path = this.pathCombine(stDir, "model.stg");
		ModelCodeGen gen2 = new ModelCodeGen(_ctx, path, "models");
		b = generateOneFile(def, gen2, name + "Model", "app\\models");
		if (!b )
		{
			return false; //!!
		}
		
		path = this.pathCombine(stDir, "dal_interface.stg");
		DALIntefaceCodeGen gen3 = new DALIntefaceCodeGen(_ctx, path, "mef.dals");
		b = generateOneFile(def, gen3, String.format("I%sDAL", name), "app\\mef\\dals");
		if (!b )
		{
			return false; //!!
		}
		
		path = this.pathCombine(stDir, "dal_mock.stg");
		MockDALCodeGen gen4 = new MockDALCodeGen(_ctx, path, "mef.mocks");
		b = generateOneFile(def, gen4, String.format("Mock%sDAL_GEN", name), "test\\mef\\mocks");
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
	private DalGenXmlParser readEntityDef(String appDir) throws Exception
	{
		String path = this.pathCombine(appDir, "mef.xml");
		DalGenXmlParser parser = new DalGenXmlParser(_ctx);
		boolean b = parser.parse(path);

//		return parser._entityL.get(0);
		return parser;
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
package org.mef.dalgen.codegen;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.mef.dalgen.codegen.generators.CodeGenBase;
import org.mef.dalgen.codegen.generators.DAOIntefaceCodeGen;
import org.mef.dalgen.codegen.generators.EntityCodeGen;
import org.mef.dalgen.codegen.generators.KnownDAOsCodeGen;
import org.mef.dalgen.codegen.generators.MockDAOCodeGen;
import org.mef.dalgen.codegen.generators.ModelCodeGen;
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
	private boolean _needParentClass;
	public boolean disableFileIO;
	
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
	
	public boolean generate(String name) throws Exception
	{
		int i = 0;
		for(EntityDef def : parser._entityL)
		{
			if (def.name.equals(name))
			{
				return generate(i);
			}
			i++;
		}
		return false;
	}
	
	public boolean generate(int index) throws Exception
	{
		EntityDef def = parser._entityL.get(index);
		String name = def.name;
		
		String path = this.pathCombine(stDir, "entity.stg");
		EntityCodeGen gen = new EntityCodeGen(_ctx, path, "mef.entities");
		boolean b = generateOneFile(def, gen, "app\\mef\\entities");
		if (!b )
		{
			return false; //!!
		}
		if (_needParentClass)
		{
			path = this.pathCombine(stDir, "entity-based-on-gen.stg");
			gen = new EntityCodeGen(_ctx, path, "mef.entities");
			def.extendEntity = false;
			b = generateOneFile(def, gen, "app\\mef\\entities");
			def.extendEntity = true; //restore
			if (!b )
			{
				return false; //!!
			}			
		}

		path = this.pathCombine(stDir, "model.stg");
		ModelCodeGen gen2 = new ModelCodeGen(_ctx, path, "models");
		b = generateOneFile(def, gen2, "app\\models");
		if (!b )
		{
			return false; //!!
		}
		
		path = this.pathCombine(stDir, "dao_interface.stg");
		DAOIntefaceCodeGen gen3 = new DAOIntefaceCodeGen(_ctx, path, "mef.daos");
		b = generateOneFile(def, gen3, "app\\mef\\daos");
		if (!b )
		{
			return false; //!!
		}
		
		path = this.pathCombine(stDir, "dao_mock.stg");
		MockDAOCodeGen gen4 = new MockDAOCodeGen(_ctx, path, "mef.daos.mocks");
		b = generateOneFile(def, gen4, "app\\mef\\daos\\mocks");
		if (!b )
		{
			return false; //!!
		}
		
		return b;
	}
	public boolean generateOnce() throws Exception
	{
		EntityDef def = parser._entityL.get(0);
		def.enabled = true;

		String path = this.pathCombine(stDir, "dao_all_known.stg");
		KnownDAOsCodeGen gen5 = new KnownDAOsCodeGen(_ctx, path, "mef.gen");
		boolean b = generateOneFile(def, gen5, "mef\\gen");
		if (!b )
		{
			return false; //!!
		}
		return b;
	}
	private boolean generateOneFile(EntityDef def, CodeGenBase gen, String relPath) throws Exception
	{
		if (! def.enabled)
		{
			log(def.name + " disabled -- no files generated.");
			return true; //do nothing
		}
		
		String originalRelPath = relPath;
		
		_needParentClass = false;
		String code = gen.generate(def);	
		
		if (gen.isExtended())
		{
			relPath = "app\\mef\\gen";			
		}
		//log(code);
		String className = gen.getClassName(def);	
		boolean b = writeFile(appDir, relPath, className, code);
		if (!b)
		{
			return false;
		}
		
		//if _GEN and parent class doesn't exist
		if (className.endsWith("_GEN"))
		{
			className = className.replace("_GEN", "");
			String path = this.pathCombine(appDir, originalRelPath);
			path = this.pathCombine(path, className + ".java");
			File f = new File(path);
			if (! f.exists())
			{
				this.log("FFFFF");
				_needParentClass = true;
			}
		}
		return true;
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
		if (disableFileIO)
		{
			return true;
		}
		
		SfxTextWriter w = new SfxTextWriter(outPath, null);
		w.addLine(code);
		boolean b = w.writeFile();
		return b;
	}
	
}
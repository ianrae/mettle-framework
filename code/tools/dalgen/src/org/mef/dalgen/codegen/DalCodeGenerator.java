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
		
		boolean b = doGen(def, "entity.stg", "mef.entities", "app\\mef\\entities", new EntityCodeGen(_ctx));
		if (!b )
		{
			return false; //!!
		}
		
		if (_needParentClass)
		{
			def.extendEntity = false;
			b = doGen(def, "entity-based-on-gen.stg", "mef.entities", "app\\mef\\entities", new EntityCodeGen(_ctx));
			if (!b )
			{
				return false; //!!
			}
		}
		
		b = doGen(def, "model.stg", "models", "app\\models", new ModelCodeGen(_ctx));
		if (!b )
		{
			return false; //!!
		}
		
		b = doGen(def, "dao_interface.stg", "mef.daos", "app\\mef\\daos", new DAOIntefaceCodeGen(_ctx));
		if (!b )
		{
			return false; //!!
		}
		
		b = doGen(def, "dao_mock.stg", "mef.daos.mocks", "app\\mef\\daos\\mocks", new MockDAOCodeGen(_ctx));
		if (!b )
		{
			return false; //!!
		}
		
		return b;
	}

	private boolean doGen(EntityDef def, String stgFilename, String packageName, 
			String relPath, CodeGenBase gen) throws Exception
	{
		String path = this.pathCombine(stDir, stgFilename);
		gen.init(path, packageName);
		boolean b = generateOneFile(def, gen, relPath);
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
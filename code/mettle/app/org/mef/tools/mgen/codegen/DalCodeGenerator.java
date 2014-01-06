package org.mef.tools.mgen.codegen;

//import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;
import org.mef.framework.sfx.SfxErrorTracker;
import org.mef.framework.sfx.SfxTextWriter;
import org.mef.tools.mgen.codegen.generators.CodeGenBase;
import org.mef.tools.mgen.codegen.generators.DAOIntefaceCodeGen;
import org.mef.tools.mgen.codegen.generators.DaoEntityLoaderCodeGen;
import org.mef.tools.mgen.codegen.generators.DaoFinderCodeGen;
import org.mef.tools.mgen.codegen.generators.EntityCodeGen;
import org.mef.tools.mgen.codegen.generators.EntityLoaderSaverCodeGen;
import org.mef.tools.mgen.codegen.generators.KnownDAOsCodeGen;
import org.mef.tools.mgen.codegen.generators.MockDAOCodeGen;
import org.mef.tools.mgen.codegen.generators.ModelCodeGen;
import org.mef.tools.mgen.codegen.generators.RealDAOCodeGen;
import org.mef.tools.mgen.parser.DalGenXmlParser;
import org.mef.tools.mgen.parser.EntityDef;



public class DalCodeGenerator extends CodeGenerator
{
	private DalGenXmlParser parser;
	private boolean _needParentClass;
	public boolean disableFileIO;
	
	public boolean genRealDAO = false; //for now
	public boolean genDaoLoader = false;
	private SfxErrorTracker _tracker;
	private List<String> extraImportsL = new ArrayList<String>();
	
	public DalCodeGenerator(SfxContext ctx)
	{
		super(ctx);
		_tracker = (SfxErrorTracker) _ctx.getServiceLocator().getInstance(SfxErrorTracker.class);
	}
	
	@Override
	public int init(String appDir) throws Exception
	{
		super.init(appDir);
		parser = readEntityDef(appDir);
		
		//check for duplicates
		HashMap<String, String> map = new HashMap<String, String>();
		for(EntityDef def : parser._entityL)
		{
			String existing = map.get(def.name);
			
			if (existing != null && (existing.equals(def.name)))
			{
				_tracker.errorOccurred("duplicate entity - more than one of: " + def.name);
				return 0;
			}
			map.put(def.name, def.name);
			
			if (def.useExistingPackage != null)
			{
				extraImportsL.add(String.format("import %s.%s;", def.useExistingPackage, def.name));
			}
		}
		
		
		return parser._entityL.size();
	}
	
	@Override
	public boolean generateAll() throws Exception
	{
		boolean b = this.generateOnce();
		if (! b)
		{
			return false;
		}
		
		int i = 0;
		for(EntityDef def : parser._entityL)
		{
			if (! generate(i))
			{
				return false;
			}
			i++;
		}
		return true;
	}
	
	@Override
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
	
	protected boolean generate(int index) throws Exception
	{
		EntityDef def = parser._entityL.get(index);
		
		boolean b = this.generateEntityClasses(def);
		if (!b )
		{
			return false; //!!
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
		
		if (genRealDAO)
		{
			b = doGen(def, "dao_real.stg", "boundaries.daos", "app\\boundaries\\daos", new RealDAOCodeGen(_ctx));
			if (!b )
			{
				return false; //!!
			}
		}
		
		return b;
	}
	
	
	protected boolean generateEntityClasses(EntityDef def) throws Exception
	{
		if (def.useExistingPackage != null)
		{
			this.log(String.format("useExistingPackage %s for %s", def.useExistingPackage, def.name));
			return true; //don't generate
		}
		
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
		return true;
	}
	
	private boolean doGen(EntityDef def, String stgFilename, String packageName, 
			String relPath, CodeGenBase gen) throws Exception
	{
		String baseDir = "/mgen/resources/dal/";
		String path = getResourceOrFilePath(baseDir, stgFilename);
		gen.init(path, packageName);
		boolean b = generateOneFile(def, gen, relPath);
		return b;
	}
	
	public boolean generateOnce() throws Exception
	{
		EntityDef def = parser._entityL.get(0);
		def.enabled = true;

		String baseDir = "/mgen/resources/dal/";
		String path = getResourceOrFilePath(baseDir, "dao_all_known.stg");
		KnownDAOsCodeGen gen5 = new KnownDAOsCodeGen(_ctx);
		gen5.init(path, "mef.gen");
		boolean b = generateOneFile(def, gen5, "mef\\gen");
		if (!b )
		{
			return false; //!!
		}

		baseDir = "/mgen/resources/dal/";
		path = getResourceOrFilePath(baseDir, "dao_finder.stg");
		DaoFinderCodeGen gen6 = new DaoFinderCodeGen(_ctx);
		gen6.init(path, "mef.core");
		b = generateOneFile(def, gen6, "app\\mef\\core");
		if (!b )
		{
			return false; //!!
		}

		if (genDaoLoader)
		{
			
			b = doGen(def, "dao_entity_loader.stg", "mef.core", "app\\mef\\core", new DaoEntityLoaderCodeGen(_ctx));
			if (!b )
			{
				return false; //!!
			}

			b = doGen(def, "dao_entity_saver.stg", "mef.gen", "app\\mef\\gen", new EntityLoaderSaverCodeGen(_ctx));
			if (!b )
			{
				return false; //!!
			}
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
		
		if (this.extraImportsL.size() > 0)
		{
			gen.extraImportsL = this.extraImportsL;
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
		boolean b = writeFile(appDir, relPath, className + ".java", code);
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
//				this.log("FFFFF");
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

	
}
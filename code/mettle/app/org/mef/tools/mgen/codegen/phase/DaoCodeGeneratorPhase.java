package org.mef.tools.mgen.codegen.phase;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mef.framework.sfx.SfxContext;
import org.mef.framework.sfx.SfxFileUtils;
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

public class DaoCodeGeneratorPhase extends CodeGeneratorPhase
{
	private static class AddParams
	{
		public String baseDir;
		public String filename;
		public EntityDef def;
		public CodeGenBase inner;
		public boolean isExtended;

		public AddParams(String baseDir, String filename, EntityDef def, CodeGenBase inner, boolean isExtended)
		{
			this.baseDir = baseDir;
			this.filename = filename;
			this.def = def;
			this.inner = inner;
			this.isExtended = isExtended;
//			inner.setExtended(def.shouldExtend(EntityDef.ENTITY));
		}

		public boolean needParentClass(String appDir, String relPath)
		{
//			boolean isExtended = def.shouldExtend(EntityDef.ENTITY);

			if (isExtended)
			{
				String className = inner.getClassName(def);	
				className = className.replace("_GEN", "");
				SfxFileUtils utils = new SfxFileUtils();
				String path = utils.PathCombine(appDir, relPath);
				path = utils.PathCombine(path, className + ".java");
				File f = new File(path);
				if (! f.exists())
				{
					return true;
				}
			}
			return false;
		}
	}

	private DalGenXmlParser parser;
	private List<String> extraImportsL = new ArrayList<String>();
	private boolean _needParentClass;

	public boolean genRealDAO = false; //for now
	public boolean genDaoLoader = false;

	public DaoCodeGeneratorPhase(SfxContext ctx) 
	{
		super(ctx, "dao");
	}

	@Override
	public void initialize(String appDir) throws Exception 
	{
		init(appDir); //done twice. fix later!! but need it initialized here for addGenerators
		parser = readEntityDef(appDir);

		//check for duplicates
		HashMap<String, String> map = new HashMap<String, String>();
		for(EntityDef def : parser._entityL)
		{
			String existing = map.get(def.name);

			if (existing != null && (existing.equals(def.name)))
			{
				this.addError("duplicate entity - more than one of: " + def.name);
				return;
			}
			map.put(def.name, def.name);

			if (def.useExistingPackage != null)
			{
				extraImportsL.add(String.format("import %s.%s;", def.useExistingPackage, def.name));
			}
		}	

		//now add generators
		if (parser._entityL.size() > 0)
		{
			generateOnce(parser._entityL.get(0));
		}


		for(EntityDef def : parser._entityL)
		{
			generateEntityClasses(def);
			generateOtherClasses(def);
		}

		super.initialize(appDir); //it will call initalize of each generator
	}

	private void addOne(AddParams params, String packageName, String relPath)
	{
		addGenerator(params, packageName, relPath);
		
		//if extended and parent class doesn't exist then we'll create it
		if (params.needParentClass(appDir, relPath))
		{
			//add again with extended=false so it creates the parent class
//			params = new AddParams(baseDir, "entity-based-on-gen.stg", def, new EntityCodeGen(_ctx));
			params.isExtended = false;
			addGenerator(params, packageName, relPath);
		}
	}
	private void addGenerator(AddParams params, String packageName, String relPath)
	{
		params.inner.setExtended(params.isExtended); //propogate to codegen
		DaoGenerator gen = new DaoGenerator(_ctx, params.inner, params.baseDir, params.filename, 
				params.def,  packageName, relPath, extraImportsL);
		add(gen);
	}
	
	private void generateOtherClasses(EntityDef def) 
	{
		String baseDir = "/mgen/resources/dal/";

		boolean extend = def.shouldExtend(EntityDef.MODEL);
		AddParams params = new AddParams(baseDir, "model.stg", def, new ModelCodeGen(_ctx), extend);
		addOne(params, "models", "app\\models");

		extend = def.shouldExtend(EntityDef.DAO_INTERFACE);
		params = new AddParams(baseDir, "dao_interface.stg", def, new DAOIntefaceCodeGen(_ctx), extend);
		addOne(params, "mef.daos", "app\\mef\\daos");

		extend = def.shouldExtend(EntityDef.DAO_MOCK);
		params = new AddParams(baseDir, "dao_mock.stg", def, new MockDAOCodeGen(_ctx), extend);
		addOne(params, "mef.daos.mocks", "app\\mef\\daos\\mocks");

		if (genRealDAO)
		{
			extend = def.shouldExtend(EntityDef.DAO_REAL);
			params = new AddParams(baseDir, "dao_real.stg", def, new RealDAOCodeGen(_ctx), extend);
			addOne(params, "boundaries.daos", "app\\boundaries\\daos");
		}
	}

	private void generateOnce(EntityDef def) throws Exception
	{
		def.enabled = true; //why do this??!!

		String baseDir = "/mgen/resources/dal/";

		boolean extend = true;
		AddParams params = new AddParams(baseDir, "dao_all_known.stg", def, new KnownDAOsCodeGen(_ctx), extend);
		addOne(params, "mef.gen", "app\\mef\\gen");

		extend = false;
		params = new AddParams(baseDir, "dao_finder.stg", def, new DaoFinderCodeGen(_ctx), false);
		addOne(params, "mef.core", "app\\mef\\core");

		if (genDaoLoader)
		{
			extend = true;
			params = new AddParams(baseDir, "dao_entity_loader.stg", def, new DaoEntityLoaderCodeGen(_ctx), extend);
			addOne(params, "mef.core", "app\\mef\\core");

			extend = true;
			params = new AddParams(baseDir, "dao_entity_saver.stg", def, new EntityLoaderSaverCodeGen(_ctx), extend);
			addOne(params, "mef.gen", "app\\mef\\gen");
		}
	}

	protected void generateEntityClasses(EntityDef def) throws Exception
	{
		if (def.useExistingPackage != null)
		{
			this.log(String.format("useExistingPackage %s for %s", def.useExistingPackage, def.name));
			return; //don't generate
		}

		String baseDir = "/mgen/resources/dal/";

		boolean extend = def.shouldExtend(EntityDef.ENTITY);
		AddParams params = new AddParams(baseDir, "entity.stg", def, new EntityCodeGen(_ctx), extend);
		String relPath = "app\\mef\\entities";
		addOne(params, "mef.entities", relPath);

	}


}
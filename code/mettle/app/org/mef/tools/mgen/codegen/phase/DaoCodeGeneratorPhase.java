package org.mef.tools.mgen.codegen.phase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mef.framework.sfx.SfxContext;
import org.mef.tools.mgen.codegen.generators.DAOIntefaceCodeGen;
import org.mef.tools.mgen.codegen.generators.DaoEntityLoaderCodeGen;
import org.mef.tools.mgen.codegen.generators.DaoFinderCodeGen;
import org.mef.tools.mgen.codegen.generators.EntityCodeGen;
import org.mef.tools.mgen.codegen.generators.EntityLoaderSaverCodeGen;
import org.mef.tools.mgen.codegen.generators.KnownDAOsCodeGen;
import org.mef.tools.mgen.codegen.generators.MockDAOCodeGen;
import org.mef.tools.mgen.codegen.generators.ModelCodeGen;
import org.mef.tools.mgen.codegen.generators.RealDAOCodeGen;
import org.mef.tools.mgen.codegen.generators.RealQueryProcCodeGen;
import org.mef.tools.mgen.codegen.generators.SprigCodeGen;
import org.mef.tools.mgen.parser.DalGenXmlParser;
import org.mef.tools.mgen.parser.EntityDef;

public class DaoCodeGeneratorPhase extends CodeGeneratorPhase
{
	private DalGenXmlParser parser;
	private List<String> extraImportsL = new ArrayList<String>();
//	private boolean _needParentClass;

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

	private void generateOtherClasses(EntityDef def) 
	{
		String baseDir = "/mgen/resources/dal/";

		//-model is exception. extend is false to avoid trouble with List<UserModel_GEN> and List<UserModel>
		boolean extend = false; //def.shouldExtend(EntityDef.ENTITY);
		AddParams params;
		
		if (def.generateModel)
		{
			params = new AddParams(baseDir, "model.stg", def, new ModelCodeGen(_ctx), extend);
			addOne(params, "models", "app\\models");
		}
		
		extend = def.shouldExtend(EntityDef.ENTITY);
		params = new AddParams(baseDir, "dao_interface.stg", def, new DAOIntefaceCodeGen(_ctx), extend);
		addOne(params, "mef.daos", "app\\mef\\daos");

		extend = def.shouldExtend(EntityDef.ENTITY);
		params = new AddParams(baseDir, "dao_mock.stg", def, new MockDAOCodeGen(_ctx), extend);
		addOne(params, "mef.daos.mocks", "app\\mef\\daos\\mocks");

		extend = def.shouldExtend(EntityDef.ENTITY);
		params = new AddParams(baseDir, "dao_sprig.stg", def, new SprigCodeGen(_ctx), extend);
		addOne(params, "mef.daos.sprigs", "app\\mef\\daos\\sprigs");

		if (genRealDAO)
		{
			extend = def.shouldExtend(EntityDef.ENTITY);
			params = new AddParams(baseDir, "dao_real.stg", def, new RealDAOCodeGen(_ctx), extend);
			addOne(params, "boundaries.daos", "app\\boundaries\\daos");
			
			extend = false;
			params = new AddParams(baseDir, "dao_real_queryproc.stg", def, new RealQueryProcCodeGen(_ctx), extend);
			params.forceUserCanModifyFlag = false;
			addOne(params, "boundaries.daos", "app\\boundaries\\daos");
			
		}
	}

	private void generateOnce(EntityDef def) throws Exception
	{
		def.enabled = true; //why do this??!!

		String baseDir = "/mgen/resources/dal/";

		boolean extend = true;
		AddParams params = new AddParams(baseDir, "dao_all_known.stg", def, new KnownDAOsCodeGen(_ctx), extend);
		addOne(params, "mef.core", "app\\mef\\core");

		extend = false;
		params = new AddParams(baseDir, "dao_finder.stg", def, new DaoFinderCodeGen(_ctx), false);
		params.forceUserCanModifyFlag = false;
		addOne(params, "mef.core", "app\\mef\\core");

		if (genDaoLoader)
		{
			extend = true;
			params = new AddParams(baseDir, "dao_entity_loader.stg", def, new DaoEntityLoaderCodeGen(_ctx), extend);
			params.forceUserCanModifyFlag = false;
			addOne(params, "mef.core", "app\\mef\\core");

			extend = true;
			params = new AddParams(baseDir, "dao_entity_saver.stg", def, new EntityLoaderSaverCodeGen(_ctx), extend);
			addOne(params, "mef.core", "app\\mef\\core");
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

	//-- helpers --
	private void addOne(AddParams params, String packageName, String relPath)
	{
		addGenerator(params, packageName, relPath);
		
		//if extended and parent class doesn't exist then we'll create it
		if (params.needParentClass(appDir, relPath))
		{
			//add again with extended=false so it creates the parent class
//			params = new AddParams(baseDir, "entity-based-on-gen.stg", def, new EntityCodeGen(_ctx));
			params.isExtended = false;
			params.isParentOfExtended = true;
			addGenerator(params, packageName, relPath);
		}
	}
	private void addGenerator(AddParams params, String packageName, String relPath)
	{
//		params.inner.setExtended(params.isExtended); //propogate to codegen
		boolean flag = params.canUserModify();
		DaoGenerator gen = new DaoGenerator(_ctx, params.inner, params.baseDir, params.filename, 
				params.def,  packageName, relPath, extraImportsL, params.isExtended, params.isParentOfExtended, flag);
		add(gen);
	}
	


}
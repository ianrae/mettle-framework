package org.mef.tools.mgen.codegen.phase;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mef.framework.sfx.SfxContext;
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

		private void generateOtherClasses(EntityDef def) 
		{
			String baseDir = "/mgen/resources/dal/";
			String filename = "model.stg";
			CodeGenBase inner = new ModelCodeGen(_ctx);
			DaoGenerator gen = new DaoGenerator(_ctx, inner, baseDir, filename, def,  "models", "app\\models", extraImportsL);
			add(gen);
			
			filename = "dao_interface.stg";
			inner = new DAOIntefaceCodeGen(_ctx);
			gen = new DaoGenerator(_ctx, inner, baseDir, filename, def,  "mef.daos", "app\\mef\\daos", extraImportsL);
			add(gen);
			
			filename = "dao_mock.stg";
			inner = new MockDAOCodeGen(_ctx);
			gen = new DaoGenerator(_ctx, inner, baseDir, filename, def,  "mef.daos.mocks", "app\\mef\\daos\\mocks", extraImportsL);
			add(gen);
			
			if (genRealDAO)
			{
				filename = "dao_real.stg";
				inner = new RealDAOCodeGen(_ctx);
				gen = new DaoGenerator(_ctx, inner, baseDir, filename, def,  "boundaries.daos", "app\\boundaries\\daos", extraImportsL);
				add(gen);
			}
			
		}

		private void generateOnce(EntityDef def) throws Exception
		{
			def.enabled = true; //why do this??!!

			String baseDir = "/mgen/resources/dal/";
			String filename = "dao_all_known.stg";
			CodeGenBase inner = new KnownDAOsCodeGen(_ctx);
			DaoGenerator gen = new DaoGenerator(_ctx, inner, baseDir, filename, def,  "mef.gen", "mef\\gen", extraImportsL);
			add(gen);

			filename = "dao_finder.stg";
			inner = new DaoFinderCodeGen(_ctx);
			gen = new DaoGenerator(_ctx, inner, baseDir, filename, def,  "mef.core", "app\\mef\\core", extraImportsL);
			add(gen);

			if (genDaoLoader)
			{
				filename = "dao_entity_loader.stg";
				inner = new DaoEntityLoaderCodeGen(_ctx);
				gen = new DaoGenerator(_ctx, inner, baseDir, filename, def,  "mef.core", "app\\mef\\core", extraImportsL);
				add(gen);

				filename = "dao_entity_saver.stg";
				inner = new EntityLoaderSaverCodeGen(_ctx);
				gen = new DaoGenerator(_ctx, inner, baseDir, filename, def,  "mef.gen", "app\\mef\\gen", extraImportsL);
				add(gen);
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
			String filename = "entity.stg";
			CodeGenBase inner = new EntityCodeGen(_ctx);
			String relPath = "app\\mef\\entities";
			DaoGenerator gen = new DaoGenerator(_ctx, inner, baseDir, filename, def,  "mef.entities", relPath, extraImportsL);
			add(gen);
			
			//!!fix later -- won't work. can modify def here 'cause get used again in run many times
//			if (needParentClass(def, inner, relPath))
//			{
//				def.setShouldExtend(EntityDef.ENTITY, false);
//				
//				baseDir = "/mgen/resources/dal/";
//				filename = "entity-based-on-gen.stg";
//				inner = new EntityCodeGen(_ctx);
//				gen = new DaoGenerator(_ctx, inner, baseDir, filename, def,  "mef.entities", "app\\mef\\entities", extraImportsL);
//				add(gen);
//			}
		}
		
		private boolean needParentClass(EntityDef def, CodeGenBase gen, String relPath)
		{
			boolean isExtended = def.shouldExtend(EntityDef.ENTITY);
			
			if (isExtended)
			{
				String className = gen.getClassName(def);	
				className = className.replace("_GEN", "");
				String path = this.pathCombine(appDir, relPath);
				path = this.pathCombine(path, className + ".java");
				File f = new File(path);
				if (! f.exists())
				{
					return true;
				}
			}
			return false;
		}
	}
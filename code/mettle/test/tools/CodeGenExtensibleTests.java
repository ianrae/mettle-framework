package tools;

import static org.junit.Assert.*;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.junit.Before;
import org.junit.Test;
import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;
import org.mef.framework.sfx.SfxErrorTracker;
import org.mef.tools.mgen.codegen.CodeGenerator;
import org.mef.tools.mgen.codegen.ICodeGenPhase;
import org.mef.tools.mgen.codegen.ICodeGenerator;
import org.mef.tools.mgen.codegen.generators.BoundaryCodeGen;
import org.mef.tools.mgen.codegen.generators.CodeGenBase;
import org.mef.tools.mgen.codegen.generators.ControllerCodeGen;
import org.mef.tools.mgen.codegen.generators.DAOIntefaceCodeGen;
import org.mef.tools.mgen.codegen.generators.DaoEntityLoaderCodeGen;
import org.mef.tools.mgen.codegen.generators.DaoFinderCodeGen;
import org.mef.tools.mgen.codegen.generators.EntityCodeGen;
import org.mef.tools.mgen.codegen.generators.EntityLoaderSaverCodeGen;
import org.mef.tools.mgen.codegen.generators.FormBinderCodeGen;
import org.mef.tools.mgen.codegen.generators.KnownDAOsCodeGen;
import org.mef.tools.mgen.codegen.generators.MockDAOCodeGen;
import org.mef.tools.mgen.codegen.generators.ModelCodeGen;
import org.mef.tools.mgen.codegen.generators.PresenterCodeGen;
import org.mef.tools.mgen.codegen.generators.PresenterUnitTestCodeGen;
import org.mef.tools.mgen.codegen.generators.RealDAOCodeGen;
import org.mef.tools.mgen.codegen.generators.ReplyCodeGen;
import org.mef.tools.mgen.codegen.generators.ViewCodeGen;
import org.mef.tools.mgen.parser.DalGenXmlParser;
import org.mef.tools.mgen.parser.EntityDef;

public class CodeGenExtensibleTests extends BaseTest
{
	public class DaoGenerator extends CodeGenerator implements ICodeGenerator
	{
		String filename;  //no path
		String baseDir;   //location of source (StringTemplate file)
		String packageName;
		String relPath;
		EntityDef def;
		CodeGenBase gen;
		private List<String> extraImportsL;

		public DaoGenerator(SfxContext ctx, CodeGenBase gen, String baseDir, String filename, 
				EntityDef def, String packageName, String relPath, List<String> extraImportsL) 
		{
			super(ctx);
			this.filename = filename;
			this.baseDir = baseDir;
			this.packageName = packageName;
			this.relPath = relPath;
			this.def = def;
			this.gen = gen;
			this.extraImportsL = extraImportsL;
		}

		@Override
		public String name() 
		{
			return "dao";
		}

		@Override
		public void initialize(String appDir) throws Exception
		{
			init(appDir);
			String path = getResourceOrFilePath(baseDir, filename);
		}
		@Override
		public boolean run() throws Exception 
		{
			String path = getResourceOrFilePath(baseDir, filename);

			gen.init(path, packageName);
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
			
//			//if _GEN and parent class doesn't exist
//			if (className.endsWith("_GEN"))
//			{
//				className = className.replace("_GEN", "");
//				String path = this.pathCombine(appDir, originalRelPath);
//				path = this.pathCombine(path, className + ".java");
//				File f = new File(path);
//				if (! f.exists())
//				{
////					this.log("FFFFF");
//					_needParentClass = true;
//				}
//			}

			return true;
		}

		@Override
		public boolean generateAll() throws Exception {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean generate(String name) throws Exception {
			// TODO Auto-generated method stub
			return false;
		}
	}	


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

	public static class MasterCodeGen extends SfxBaseObj
	{
		private ArrayList<ICodeGenPhase> phaseL = new ArrayList<ICodeGenPhase>();
		private String appDir;

		public MasterCodeGen(SfxContext ctx) 
		{
			super(ctx);
		}
		public void addPhase(ICodeGenPhase phase)
		{
			phaseL.add(phase);
		}

		public void initialize(String appDirParam) throws Exception
		{
			this.appDir = appDirParam;

			for(ICodeGenPhase phase : phaseL)
			{
				phase.initialize(appDir);
			}
		}

		public boolean run() throws Exception 
		{
			for(ICodeGenPhase phase : phaseL)
			{
				if (! phase.run())
				{
					this.addError(String.format("Phase %s failed", phase.name()));
					return false;
				}
			}

			return true;
		}
	}

	public static class MyGenX implements ICodeGenerator
	{
		private String name;
		public boolean resultToReturn = true;

		public MyGenX(String name)
		{
			this.name = name;
		}
		@Override
		public String name() 
		{
			return name;
		}

		@Override
		public void initialize(String appDir) throws Exception
		{
			//			init(appDir);
		}
		@Override
		public boolean run() 
		{
			return resultToReturn;
		}

	}
	public static class MyPhase1 extends SfxBaseObj implements ICodeGenPhase
	{
		private ArrayList<ICodeGenerator> genL = new ArrayList<ICodeGenerator>();
		private String appDir;
		public boolean resultToReturn = true;

		public MyPhase1(SfxContext ctx) 
		{
			super(ctx);
		}


		@Override
		public void initialize(String appDir) throws Exception 
		{
			this.appDir = appDir;
		}

		@Override
		public boolean run() throws Exception 
		{
			for(ICodeGenerator gen : genL)
			{
				if (! gen.run())
				{
					this.addError(String.format("Generator %s failed", gen.name()));
					return false;
				}
			}
			return resultToReturn;
		}

		@Override
		public String name() 
		{
			return "app";
		}

		@Override
		public void add(ICodeGenerator gen) 
		{
			this.genL.add(gen);
		}
	}

	@Test
	public void test() throws Exception 
	{
		assertNotNull(_ctx);
		MasterCodeGen master = new MasterCodeGen(_ctx);
		MyPhase1 phase1 = new MyPhase1(_ctx);

		MyGenX genx = new MyGenX("abc");
		phase1.add(genx);


		master.addPhase(phase1);

		String appDir = "c:\\tmp\\cc";
		master.initialize(appDir);
		boolean b = master.run();
		assertTrue(b);
	}


	@Test
	public void testFail() throws Exception 
	{
		MasterCodeGen master = new MasterCodeGen(_ctx);
		MyPhase1 phase1 = new MyPhase1(_ctx);
		MyGenX genx = new MyGenX("abc");
		genx.resultToReturn = false;
		phase1.add(genx);
		master.addPhase(phase1);

		String appDir = "c:\\tmp\\cc";
		master.initialize(appDir);
		boolean b = master.run();
		assertFalse(b);
	}

	@Test
	public void testNewAppPhase() throws Exception 
	{
		MasterCodeGen master = new MasterCodeGen(_ctx);
		AppCodeGeneratorPhase phase = new AppCodeGeneratorPhase(_ctx);
		master.addPhase(phase);

		String appDir = "c:\\tmp\\cc";
		master.initialize(appDir);
		boolean b = master.run();
		assertTrue(b);
	}

	@Test
	public void testNewPresenterPhase() throws Exception 
	{
		MasterCodeGen master = new MasterCodeGen(_ctx);
		PresenterCodeGeneratorPhase phase = new PresenterCodeGeneratorPhase(_ctx);
		master.addPhase(phase);

		String appDir = "c:\\tmp\\cc";
		master.initialize(appDir);
		boolean b = master.run();
		assertTrue(b);
	}

	@Test
	public void testNewDaoPhase() throws Exception 
	{
		MasterCodeGen master = new MasterCodeGen(_ctx);
		DaoCodeGeneratorPhase phase = new DaoCodeGeneratorPhase(_ctx);
		master.addPhase(phase);

		String appDir = "c:\\tmp\\cc";
		master.initialize(appDir);
		boolean b = master.run();
		assertTrue(b);
	}

	//-- helpers --
	@Before
	public void init()
	{
		this.createContext();
		this._ctx.getServiceLocator().registerSingleton(SfxErrorTracker.class, new SfxErrorTracker(_ctx));
	}

}

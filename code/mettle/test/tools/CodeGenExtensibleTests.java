package tools;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.ArrayList;

import org.apache.commons.lang.NotImplementedException;
import org.junit.Before;
import org.junit.Test;
import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;
import org.mef.framework.sfx.SfxErrorTracker;
import org.mef.tools.mgen.codegen.CodeGenerator;
import org.mef.tools.mgen.codegen.ICodeGenPhase;
import org.mef.tools.mgen.codegen.ICodeGenerator;

public class CodeGenExtensibleTests extends BaseTest
{
	public static class MyCopyGen extends CodeGenerator implements ICodeGenerator
	{
		String filename;  //no path
		String baseDir;   //location of source (StringTemplate file)
		String newExt; //can be null
		String destDir; //can be same as appDir or a directory underneath it

		public MyCopyGen(SfxContext ctx, String baseDir, String filename, String newExt, String destDir) 
		{
			super(ctx);
			this.filename = filename;
			this.baseDir = baseDir;
			this.newExt = newExt;
			this.destDir = destDir;
		}

		@Override
		public String name() 
		{
			return "phase33";
		}

		@Override
		public void initialize(String appDir) throws Exception
		{
			init(appDir);
		}
		@Override
		public boolean run() throws Exception 
		{
			InputStream stream = getSourceFile(baseDir, filename);
			boolean b = copyFile(stream, filename, newExt, destDir);
			return b;
		}

		@Override
		public boolean generateAll() throws Exception 
		{
			throw new NotImplementedException();
		}

		@Override
		public boolean generate(String name) throws Exception 
		{
			throw new NotImplementedException();
		}
	}
	
	public static class CodeGeneratorPhase extends CodeGenerator implements ICodeGenPhase
	{
		private ArrayList<ICodeGenerator> genL = new ArrayList<ICodeGenerator>();
		private String name;
		public CodeGeneratorPhase(SfxContext ctx, String name) 
		{
			super(ctx);
			this.name = name;
		}

		@Override
		public String name() 
		{
			return name;
		}

		@Override
		public void add(ICodeGenerator gen) 
		{
			genL.add(gen);
		}
		
		@Override
		public void initialize(String appDir) throws Exception 
		{
			init(appDir);
			for(ICodeGenerator gen : genL)
			{
				gen.initialize(appDir);
			}
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
			return true;
		}

		@Override
		public boolean generateAll() throws Exception 
		{
			throw new NotImplementedException();
		}

		@Override
		public boolean generate(String name) throws Exception 
		{
			throw new NotImplementedException();
		}

	}
	
	public static class MyAppCodeGeneratorPhase extends CodeGeneratorPhase
	{
		public MyAppCodeGeneratorPhase(SfxContext ctx) 
		{
			super(ctx, "app");
		}

		@Override
		public void initialize(String appDir) throws Exception 
		{
			init(appDir); //done twice. fix later!! but need it initialized here for addGenerators
			addGenerators();
			super.initialize(appDir); //it will call initalize of each generator
		}
		
		private void addGenerators()
		{
			String baseDir = "/mgen/resources/app/copy/";
			addAppGenerators(baseDir);
			
			baseDir = "/mgen/resources/app/copy/mgen/";
			addMGenGenerators(baseDir);
			
			baseDir = "/mgen/resources/app/copy/test/";
			addTestGenerators(baseDir);
			
			baseDir = "/mgen/resources/app/copy";
			addControllerGenerators(baseDir);
			
			baseDir = "/mgen/resources/app/copy";
			addViewGenerators(baseDir);
			
		}
		private void addAppGenerators(String baseDir)
		{
			String filename = "mef.xml";
			addCopyGenerator(baseDir, filename);
			
			filename = "Global.txt";
			String dest = pathCombine(appDir, "app");
			addJavaCopyGenerator(baseDir, filename, dest);
			
			filename = "Boundary.txt";
			dest = pathCombine(appDir, "app\\boundaries");
			addJavaCopyGenerator(baseDir, filename, dest);
			
			filename = "MettleInitializer.txt";
			dest = pathCombine(appDir, "app\\mef\\core");
			addJavaCopyGenerator(baseDir, filename, dest);
		}
		
		private void addMGenGenerators(String baseDir)
		{
			String filename = "MGen.txt";
			String dest = pathCombine(appDir, "mgen");
			addJavaCopyGenerator(baseDir, filename, dest);
		}
		private void addTestGenerators(String baseDir)
		{
			String filename = "BaseTest.txt";
			String dest = pathCombine(appDir, "test/mef");
			addJavaCopyGenerator(baseDir, filename, dest);
			
			filename = "BasePresenterTest.txt";
			dest = pathCombine(appDir, "test/mef");
			addJavaCopyGenerator(baseDir, filename, dest);
		}
		private void addControllerGenerators(String baseDir)
		{
			String filename = "ErrorController.txt";
			String dest = pathCombine(appDir, "app/controllers");
			addJavaCopyGenerator(baseDir, filename, dest);
		}
		private void addViewGenerators(String baseDir)
		{
			String filename = "error.scala.html";
			String dest = pathCombine(appDir, "app/views");
			addJavaCopyGenerator(baseDir, filename, dest);
		}

		
		
		private void addCopyGenerator(String baseDir, String filename)
		{
			MyCopyGen gen = new MyCopyGen(_ctx, baseDir, filename, null, appDir);
			add(gen);
		}
		private void addJavaCopyGenerator(String baseDir, String filename, String destDir)
		{
			MyCopyGen gen = new MyCopyGen(_ctx, baseDir, filename, ".java", destDir);
			add(gen);
		}
		private void addCopyGenerator(String baseDir, String filename, String newExt, String destDir)
		{
			MyCopyGen gen = new MyCopyGen(_ctx, baseDir, filename, newExt, destDir);
			add(gen);
		}

		@Override
		public boolean run() throws Exception
		{
			createDirStructure();
			super.run();
			return true;
		}
		
		
		private void createDirStructure()
		{
			createDir("app\\boundaries");
			createDir("app\\boundaries\\binders");
			createDir("app\\boundaries\\daos");
			createDir("app\\models");
			createDir("app\\mef");
			createDir("app\\mef\\core");
			createDir("app\\mef\\daos");
			createDir("app\\mef\\daos\\mocks");
			createDir("app\\mef\\entities");
			createDir("app\\mef\\gen");
			createDir("app\\mef\\presenters");
			createDir("app\\mef\\presenters\\replies");
			createDir("conf\\mef\\seed");
			createDir("test\\mef");
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
//				phase.initx(appDir);
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
		MyAppCodeGeneratorPhase phase = new MyAppCodeGeneratorPhase(_ctx);
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

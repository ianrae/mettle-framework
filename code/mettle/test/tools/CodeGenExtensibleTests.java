package tools;

import static org.junit.Assert.*;

import java.io.File;
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
import org.mef.tools.mgen.codegen.generators.PresenterCodeGen;
import org.mef.tools.mgen.parser.DalGenXmlParser;
import org.mef.tools.mgen.parser.EntityDef;

public class CodeGenExtensibleTests extends BaseTest
{
	public static class ZGenerator extends CodeGenerator implements ICodeGenerator
	{
		String filename;  //no path
		String baseDir;   //location of source (StringTemplate file)
		String packageName;
		String relPath;
		EntityDef def;

		public ZGenerator(SfxContext ctx, String baseDir, String filename, EntityDef def, String packageName, String relPath) 
		{
			super(ctx);
			this.filename = filename;
			this.baseDir = baseDir;
			this.packageName = packageName;
			this.relPath = relPath;
			this.def = def;
		}

		@Override
		public String name() 
		{
			return "copyFile";
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
			
			PresenterCodeGen gen = new PresenterCodeGen(_ctx); //path, packageName, relPath);
			gen.init(path, packageName);
			if (! def.enabled)
			{
				log(def.name + " disabled -- no files generated.");
				return true; //do nothing
			}
			
			String code = gen.generate(def);	
			String className = gen.getClassName(def);	

			path = this.pathCombine(appDir, relPath);
			this.createDir(relPath);
			
			String filename = className;
			if (! filename.contains(".html"))
			{
				filename += ".java";
			}
			path = this.pathCombine(path, filename);
			File f = new File(path);
			if (f.exists())
			{
				log(prettifyPath(path)  + ": skipping - already exists");
				return true;
			}
			
			boolean b = writeFile(appDir, relPath, filename, code);
			if (!b)
			{
				return false;
			}
			
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
	
	public static class PresenterCodeGeneratorPhase extends CodeGeneratorPhase
	{
		private DalGenXmlParser parser;
		
		public PresenterCodeGeneratorPhase(SfxContext ctx) 
		{
			super(ctx, "app");
		}

		@Override
		public void initialize(String appDir) throws Exception 
		{
			init(appDir); //done twice. fix later!! but need it initialized here for addGenerators
			parser = readEntityDef(appDir);

			for(EntityDef def : parser._entityL)
			{
				if (! def.shouldGenerate(EntityDef.PRESENTER))
				{
					continue;
				}
				
				String baseDir = "/mgen/resources/presenter/";
				String filename = "presenter.stg";
				ZGenerator gen = new ZGenerator(_ctx, baseDir, filename, def,  "mef.presenters", "app\\mef\\presenters");
				this.add(gen);
			}
			
			super.initialize(appDir); //it will call initalize of each generator
		}
		
		
		private DalGenXmlParser readEntityDef(String appDir) throws Exception
		{
			String path = this.pathCombine(appDir, "mef.xml");
			DalGenXmlParser parser = new DalGenXmlParser(_ctx);
			boolean b = parser.parse(path);
			return parser;
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
	
	//-- helpers --
	@Before
	public void init()
	{
		this.createContext();
		this._ctx.getServiceLocator().registerSingleton(SfxErrorTracker.class, new SfxErrorTracker(_ctx));
	}
	
}

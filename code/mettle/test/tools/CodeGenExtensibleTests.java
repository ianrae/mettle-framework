package tools;

import static org.junit.Assert.*;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.NotImplementedException;
import org.junit.Before;
import org.junit.Test;
import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;
import org.mef.framework.sfx.SfxErrorTracker;
import org.mef.tools.mgen.codegen.ICodeGenPhase;
import org.mef.tools.mgen.codegen.ICodeGenerator;
import org.mef.tools.mgen.codegen.generators.BoundaryCodeGen;
import org.mef.tools.mgen.codegen.generators.ControllerCodeGen;
import org.mef.tools.mgen.codegen.generators.FormBinderCodeGen;
import org.mef.tools.mgen.codegen.generators.PresenterCodeGen;
import org.mef.tools.mgen.codegen.generators.PresenterUnitTestCodeGen;
import org.mef.tools.mgen.codegen.generators.ReplyCodeGen;
import org.mef.tools.mgen.codegen.generators.ViewCodeGen;
import org.mef.tools.mgen.codegen.phase.AppCodeGeneratorPhase;
import org.mef.tools.mgen.codegen.phase.DaoCodeGeneratorPhase;
import org.mef.tools.mgen.codegen.phase.MasterCodeGenerator;
import org.mef.tools.mgen.codegen.phase.PresenterCodeGeneratorPhase;

public class CodeGenExtensibleTests extends BaseTest
{
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
		MasterCodeGenerator master = new MasterCodeGenerator(_ctx);
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
		MasterCodeGenerator master = new MasterCodeGenerator(_ctx);
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
		MasterCodeGenerator master = new MasterCodeGenerator(_ctx);
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
		MasterCodeGenerator master = new MasterCodeGenerator(_ctx);
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
		MasterCodeGenerator master = new MasterCodeGenerator(_ctx);
		DaoCodeGeneratorPhase phase = new DaoCodeGeneratorPhase(_ctx);
		master.addPhase(phase);

		String appDir = "c:\\tmp\\cc";
		master.initialize(appDir);
		boolean b = master.run();
		assertTrue(b);
	}
	
	//HERE IS WHERE I GENERATE TO C:\tmp\dd!!
	@Test
	public void testApp6() throws Exception 
	{
		MasterCodeGenerator master = new MasterCodeGenerator(_ctx);
		AppCodeGeneratorPhase phase = new AppCodeGeneratorPhase(_ctx);
		master.addPhase(phase);

		String appDir = "c:\\tmp\\dd\\" + genRandDir();
		master.initialize(appDir);
		boolean b = master.run();
		assertTrue(b);
		
		//now use mef6.xml and do gen
		String srcPath = this.getTestFile("mef6.xml");
		String destPath = this.pathCombine(appDir, "mef.xml");
		FileUtils.copyFile(new File(srcPath), new File(destPath));
		
		log("dao gen..");
		master = new MasterCodeGenerator(_ctx);
		DaoCodeGeneratorPhase phase2 = new DaoCodeGeneratorPhase(_ctx);
		phase2.genRealDAO = true;
		phase2.genDaoLoader = true;
		
		master.addPhase(phase2);
		master.initialize(appDir);
		b = master.run();
		assertTrue(b);
		
		log("presenter gen..");
		master = new MasterCodeGenerator(_ctx);
		PresenterCodeGeneratorPhase phase3 = new PresenterCodeGeneratorPhase(_ctx);
		
		master.addPhase(phase3);
		master.initialize(appDir);
		b = master.run();
		assertTrue(b);
	}
	

	//-- helpers --
	@Before
	public void init()
	{
		this.createContext();
		this._ctx.getServiceLocator().registerSingleton(SfxErrorTracker.class, new SfxErrorTracker(_ctx));
	}

	private String genRandDir()
	{
		int max = 10000;
		int min = 10;
		Integer random = min + (int)(Math.random() * ((max - min) + 1));
		
		String s = String.format("%d", random);
		return s;
	}
	
}

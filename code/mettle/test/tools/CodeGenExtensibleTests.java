package tools;

import static org.junit.Assert.*;

import java.util.ArrayList;

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
	public static class MyAppScaffoldCodeGenerator extends CodeGenerator implements ICodeGenPhase
	{

		public MyAppScaffoldCodeGenerator(SfxContext ctx) 
		{
			super(ctx);
		}

		@Override
		public void initx(String appDir) throws Exception 
		{
			this.init(appDir);
		}

		@Override
		public String name() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void add(ICodeGenerator gen) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean run() {
			// TODO Auto-generated method stub
			return false;
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
	
	public static class MasterCodeGen extends SfxBaseObj
	{
		private ArrayList<ICodeGenPhase> phaseL = new ArrayList<ICodeGenPhase>();

		public MasterCodeGen(SfxContext ctx) 
		{
			super(ctx);
		}
		public void addPhase(ICodeGenPhase phase)
		{
			phaseL.add(phase);
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

		@Override
		public void initx(String appDir) 
		{
			this.appDir = appDir;
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
		
		boolean b = master.run();
		assertFalse(b);
	}

	//-- helpers --
	@Before
	public void init()
	{
		this.createContext();
		this._ctx.getServiceLocator().registerSingleton(SfxErrorTracker.class, new SfxErrorTracker(_ctx));
	}
	
}

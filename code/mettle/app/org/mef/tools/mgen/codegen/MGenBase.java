package org.mef.tools.mgen.codegen;

import java.io.File;

import org.mef.framework.Version;
import org.mef.framework.sfx.SfxContext;
import org.mef.framework.sfx.SfxErrorTracker;
import org.mef.tools.mgen.codegen.phase.AppCodeGeneratorPhase;
import org.mef.tools.mgen.codegen.phase.DaoCodeGeneratorPhase;
import org.mef.tools.mgen.codegen.phase.MasterCodeGenerator;
import org.mef.tools.mgen.codegen.phase.PresenterCodeGeneratorPhase;

public class MGenBase 
{
	protected SfxContext _ctx;
	protected boolean useNewImpl = false;
	
	public void runCodeGeneration() throws Exception
	{
	    createContext();
	    log("MGEN v " + Version.version);

	    if (useNewImpl)
	    {
		    newgenerateAppFiles();
		    newgeneratePresenterFiles();
		    newgenerateDAOFiles();
	    }
	    else
	    {
	    	generateAppFiles();
		    generatePresenterFiles();
		    generateDAOFiles();
	    }
	}

	//------------- OLD ------------
	private void generateAppFiles() throws Exception
	{
	    log("");
	    log("APP files: (will not overwrite)...");
	    
		MasterCodeGenerator master = new MasterCodeGenerator(_ctx);
		AppCodeGeneratorPhase phase = new AppCodeGeneratorPhase(_ctx);
		master.addPhase(phase);

		String appDir = this.getCurrentDirectory();
		master.initialize(appDir);
		boolean b = master.run();
	}

	private void generatePresenterFiles() throws Exception
	{
	    log("");
	    log("PRESENTER files: (will not overwrite)...");
	    
		MasterCodeGenerator master = new MasterCodeGenerator(_ctx);
		PresenterCodeGeneratorPhase phase = new PresenterCodeGeneratorPhase(_ctx);
		master.addPhase(phase);

		String appDir = this.getCurrentDir(".");
		master.initialize(appDir);
		boolean b = master.run();
	}

	private void generateDAOFiles() throws Exception
	{       
	    log("");
	    log("DAO files: (WILL overwrite)...");
	    
		MasterCodeGenerator master = new MasterCodeGenerator(_ctx);
		DaoCodeGeneratorPhase phase = new DaoCodeGeneratorPhase(_ctx);
		phase.genRealDAO = true;
		phase.genDaoLoader = true;
		master.addPhase(phase);

		String appDir = this.getCurrentDir("");
		master.initialize(appDir);
		boolean b = master.run();
	}

	//------------- New ------------
	private void newgenerateAppFiles() throws Exception
	{
	    log("");
	    log("newAPP files: (will not overwrite)...");
	    AppScaffoldCodeGenerator gen1 = new AppScaffoldCodeGenerator();
	    boolean b = gen1.generateAll();
//	    assertTrue(b);
	}

	private void newgeneratePresenterFiles() throws Exception
	{
	    log("");
	    log("newPRESENTER files: (will not overwrite)...");
	    PresenterScaffoldCodeGenerator gen2 = new PresenterScaffoldCodeGenerator(_ctx);
	    String appDir = this.getCurrentDir("");
	    log(appDir);
	    int n = gen2.init(appDir);
	    boolean b = gen2.generateAll();
//	    assertTrue(b);
	}

	private void newgenerateDAOFiles() throws Exception
	{       
	    log("");
	    log("newDAO files: (WILL overwrite)...");
	    DalCodeGenerator gen3 = new DalCodeGenerator(_ctx);
	    gen3.genRealDAO = true;
	    String appDir = this.getCurrentDir("");
	    int n = gen3.init(appDir);
	    boolean b = false;
	    gen3.genDaoLoader = true;
	    b = gen3.generateAll();
//	    assertTrue(b);
	}
	
	
	//--helpers--
	protected void createContext()
	{
		_ctx = new SfxContext();
		_ctx.getServiceLocator().registerSingleton(SfxErrorTracker.class, new SfxErrorTracker(_ctx));
	}
	
	protected void log(String s)
	{
		System.out.println(s);
	}

	protected String getTestFile(String filepath)
	{
		String path = getCurrentDirectory();
		path = pathCombine(path, "test\\unittests\\testfiles");
		path = pathCombine(path, filepath);
		return path;
	}
	protected String getUnitTestDir(String filepath)
	{
		String path = getCurrentDirectory();
		path = pathCombine(path, "test\\unittests");
		if (filepath != null)
		{
			path = pathCombine(path, filepath);
		}
		return path;
	}
	protected String getCurrentDir(String filepath)
	{
		String path = getCurrentDirectory();
		if (filepath != null)
		{
			path = pathCombine(path, filepath);
		}
		return path;
	}
	
	private String getCurrentDirectory()
	{
		File f = new File(".");
		return f.getAbsolutePath();
	}

	protected String getTemplateFile(String filename)
	{
		String stDir = this.getCurrentDir("src\\org\\mef\\dalgen\\resources\\dal");
		String path = pathCombine(stDir, filename);
		return path;
	}
	protected String getPresenterTemplateFile(String filename)
	{
		String stDir = this.getCurrentDir("src\\org\\mef\\dalgen\\resources\\presenter");
		String path = pathCombine(stDir, filename);
		return path;
	}
	
	
	protected String pathCombine(String path1, String path2)
	{
		if (! path1.endsWith("\\"))
		{
			path1 += "\\";
		}
		String path = path1 + path2;
		return path;
	}
}

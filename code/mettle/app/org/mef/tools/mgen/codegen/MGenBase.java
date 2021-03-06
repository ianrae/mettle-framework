package org.mef.tools.mgen.codegen;

import java.io.File;

import org.mef.framework.Version;
import org.mef.framework.sfx.SfxContext;
import org.mef.framework.sfx.SfxErrorTracker;
import org.mef.tools.mgen.codegen.old.AppScaffoldCodeGenerator;
import org.mef.tools.mgen.codegen.old.DalCodeGenerator;
import org.mef.tools.mgen.codegen.old.PresenterScaffoldCodeGenerator;
import org.mef.tools.mgen.codegen.phase.AppCodeGeneratorPhase;
import org.mef.tools.mgen.codegen.phase.DaoCodeGeneratorPhase;
import org.mef.tools.mgen.codegen.phase.MasterCodeGenerator;
import org.mef.tools.mgen.codegen.phase.PresenterCodeGeneratorPhase;

public class MGenBase extends GenBase
{
	protected boolean useNewImpl = true;

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
		log("Done.");
	}

	//------------- OLD ------------
	private void generateAppFiles() throws Exception
	{
		log("");
		log("APP files: (will not overwrite)...");
		AppScaffoldCodeGenerator gen1 = new AppScaffoldCodeGenerator();
		boolean b = gen1.generateAll();
	}

	private void generatePresenterFiles() throws Exception
	{
		log("");
		log("PRESENTER files: (will not overwrite)...");

		PresenterScaffoldCodeGenerator gen2 = new PresenterScaffoldCodeGenerator(_ctx);
		String appDir = this.getCurrentDir("");
		log(appDir);
		int n = gen2.init(appDir);
		boolean b = gen2.generateAll();
		//	    assertTrue(b);
	}

	private void generateDAOFiles() throws Exception
	{       
		log("");
		log("DAO files: (WILL overwrite)...");

		log("newDAO files: (WILL overwrite)...");
		DalCodeGenerator gen3 = new DalCodeGenerator(_ctx);
		gen3.genRealDAO = true;
		String appDir = this.getCurrentDir("");
		int n = gen3.init(appDir);
		boolean b = false;
		gen3.genDaoLoader = true;
		b = gen3.generateAll();
		//assertTrue(b);
	}

	//------------- New ------------
	private void newgenerateAppFiles() throws Exception
	{
		log("");
		log("newAPP files: (will not overwrite)...");
		MasterCodeGenerator master = new MasterCodeGenerator(_ctx);
		AppCodeGeneratorPhase phase = new AppCodeGeneratorPhase(_ctx);
		master.addPhase(phase);

		String appDir = this.getCurrentDirectory();
		master.initialize(appDir);
		boolean b = master.run();
	}

	private void newgeneratePresenterFiles() throws Exception
	{
		log("");
		log("newPRESENTER files: (will not overwrite)...");
		MasterCodeGenerator master = new MasterCodeGenerator(_ctx);
		PresenterCodeGeneratorPhase phase = new PresenterCodeGeneratorPhase(_ctx);
		master.addPhase(phase);

		String appDir = this.getCurrentDir(".");
		master.initialize(appDir);
		boolean b = master.run();
	}

	private void newgenerateDAOFiles() throws Exception
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


	//--helpers--
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
}

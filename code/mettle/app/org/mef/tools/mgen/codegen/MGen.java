package org.mef.tools.mgen.codegen;

import static org.junit.Assert.assertTrue;
import org.mef.framework.Version;

public class MGen extends MGenBase
{
	public void runCodeGeneration() throws Exception
	{
	    createContext();
	    log("MGEN v " + Version.version);

	    generateAppFiles();
	    generatePresenterFiles();
	    generateDAOFiles();
	}

	private void generateAppFiles() throws Exception
	{
	    log("");
	    log("APP files: (will not overwrite)...");
	    AppScaffoldCodeGenerator gen1 = new AppScaffoldCodeGenerator();
	    boolean b = gen1.generateAll();
	    assertTrue(b);
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
	    assertTrue(b);
	}

	private void generateDAOFiles() throws Exception
	{       
	    log("");
	    log("DAO files: (WILL overwrite)...");
	    DalCodeGenerator gen3 = new DalCodeGenerator(_ctx);
	    gen3.genRealDAO = true;
	    String appDir = this.getCurrentDir("");
	    int n = gen3.init(appDir);
	    boolean b = false;
	    gen3.genDaoLoader = true;
	    b = gen3.generateAll();
	    assertTrue(b);
	}
}

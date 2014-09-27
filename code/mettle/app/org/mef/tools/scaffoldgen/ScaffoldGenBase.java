package org.mef.tools.scaffoldgen;

import org.mef.framework.Version;
import org.mef.framework.sfx.SfxContext;
import org.mef.framework.sfx.SfxErrorTracker;
import org.mef.tools.mgen.codegen.GenBase;
import org.mef.tools.mgen.codegen.generators.CodeGenBase;
import org.mef.tools.mgen.parser.EntityDef;

public class ScaffoldGenBase extends GenBase
{
	private String controllerName;
	private String boundaryName;
	private String replyName;
	private String binderName;
	private String appDir;		

	public void init(String appDir) 
	{
		this.appDir = appDir;
	}		

	//will not overwrite an existing source file
	public void runCodeGeneration(String controllerName, String boundaryName, String replyName, String binderName) throws Exception
	{
		createContext();
		if (appDir == null)
		{
			appDir = this.getCurrentDirectory();
		}
		log("SCAFFOLDGEN v " + Version.version);
		this.controllerName = controllerName;
		this.boundaryName = boundaryName;
		this.replyName = replyName;
		this.binderName = binderName;

		if (! isNullOrEmpty(controllerName))
		{
			genController();
		}

		if (! isNullOrEmpty(boundaryName))
		{
			genBoundary();
		}

		if (! isNullOrEmpty(binderName))
		{
			genBinder();
		}
	}
	private void genBoundary() throws Exception 
	{
		String baseDir = "/mgen/resources/scaffoldgen/";
		String filename = "formbinder.stg";
		CodeGenBase inner = new FormBinderCodeGen(_ctx);
		EntityDef def = createDef();
		String packageName = "boundaries.binders";
		String relpath = "app\\boundaries\\binders";

		ScaffoldGenerator gen = new ScaffoldGenerator(_ctx, inner, baseDir, filename, def, packageName, relpath);
		gen.init(appDir);
		boolean b = gen.run();
	}



	private EntityDef createDef() 
	{
		EntityDef def = new EntityDef();
		def.enabled = true;
		def.name = "CollectInput";
		// TODO Auto-generated method stub
		return def;
	}
	private void genController()
	{
	}
	private void genBinder()
	{

	}

	//--helpers--
}
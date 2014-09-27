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
	public String appDir;
	private String presentNameToUse;
	private String inputToUse;		
	private String baseDir = "/mgen/resources/scaffoldgen/";

	public void presenterToUse(String name) 
	{
		this.presentNameToUse = name;
	}
	public void inputToUse(String name)
	{
		this.inputToUse = name;
	}
	public void replyToUse(String name) 
	{
		this.replyName = name;
	}

	//will not overwrite an existing source file
	public void runCodeGeneration(String controllerName, String boundaryName, String binderName) throws Exception
	{
		createContext();
		if (appDir == null)
		{
			appDir = this.getCurrentDirectory();
		}
		log("SCAFFOLDGEN v" + Version.version);
		this.controllerName = controllerName;
		this.boundaryName = boundaryName;
		
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

	private EntityDef createDef(String name) 
	{
		EntityDef def = new EntityDef();
		def.enabled = true;
		def.name = name;
		// TODO Auto-generated method stub
		return def;
	}
	private void genController() throws Exception
	{
		String filename = "controller.stg";
		CodeGenBase inner = new ControllerCodeGen(_ctx, this.presentNameToUse, this.replyName, this.boundaryName, this.inputToUse);
		EntityDef def = createDef(this.controllerName);
		String packageName = "controllers";
		String relpath = "app\\controllers";

		ScaffoldGenerator gen = new ScaffoldGenerator(_ctx, inner, baseDir, filename, def, packageName, relpath);
		gen.init(appDir);
		boolean b = gen.run();		
	}
	private void genBoundary() throws Exception
	{
		String filename = "boundary.stg";
		CodeGenBase inner = new BoundaryCodeGen(_ctx, this.presentNameToUse, this.replyName, this.binderName, this.inputToUse);
		EntityDef def = createDef(this.boundaryName);
		String packageName = "boundaries";
		String relpath = "app\\boundaries";

		ScaffoldGenerator gen = new ScaffoldGenerator(_ctx, inner, baseDir, filename, def, packageName, relpath);
		gen.init(appDir);
		boolean b = gen.run();
	}
	private void genBinder() throws Exception 
	{
		String filename = "formbinder.stg";
		CodeGenBase inner = new FormBinderCodeGen(_ctx);
		
		//binderName should be of form <type>Binder or <type>FormBinder
		String name = this.binderName.replace("FormBinder", "");
		if (name.endsWith("Binder"))
		{
			name = name.replace("Binder", "");
		}
		EntityDef def = createDef(name);
		String packageName = "boundaries.binders";
		String relpath = "app\\boundaries\\binders";

		ScaffoldGenerator gen = new ScaffoldGenerator(_ctx, inner, baseDir, filename, def, packageName, relpath);
		gen.init(appDir);
		boolean b = gen.run();
	}
	
	
	

	//--helpers--
}
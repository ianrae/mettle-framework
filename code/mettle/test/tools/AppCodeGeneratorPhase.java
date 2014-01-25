package tools;

import org.mef.framework.sfx.SfxContext;

public class AppCodeGeneratorPhase extends CodeGeneratorPhase
{
	public AppCodeGeneratorPhase(SfxContext ctx) 
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
		
		filename = "BaseMettleTest.txt";
		dest = pathCombine(appDir, "test/mef");
		addJavaCopyGenerator(baseDir, filename, dest);
		
		filename = "BasePresenterTest.txt";
		dest = pathCombine(appDir, "test/mef/presenter");
		baseDir = pathCombine(baseDir, "presenter");
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
		CopyFileGenerator gen = new CopyFileGenerator(_ctx, baseDir, filename, null, appDir);
		add(gen);
	}
	private void addJavaCopyGenerator(String baseDir, String filename, String destDir)
	{
		CopyFileGenerator gen = new CopyFileGenerator(_ctx, baseDir, filename, ".java", destDir);
		add(gen);
	}
	private void addCopyGenerator(String baseDir, String filename, String newExt, String destDir)
	{
		CopyFileGenerator gen = new CopyFileGenerator(_ctx, baseDir, filename, newExt, destDir);
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
		createDir("test\\mef\\presenter");
	}
}
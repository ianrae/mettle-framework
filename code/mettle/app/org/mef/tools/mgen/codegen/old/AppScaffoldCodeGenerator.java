package org.mef.tools.mgen.codegen.old;

//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;
import org.mef.framework.sfx.SfxFileUtils;
import org.mef.framework.sfx.SfxTextWriter;
import org.mef.tools.mgen.codegen.CodeGenerator;

//OLD !!!!!!!!!!!!
public class AppScaffoldCodeGenerator extends CodeGenerator
{
	
	public AppScaffoldCodeGenerator()
	{
		super(new SfxContext());
	}
	
	public AppScaffoldCodeGenerator(SfxContext ctx)
	{
		super(ctx);
	}
	
	@Override
	public boolean generateAll() throws Exception
	{
		if (this.appDir == null)
		{
			this.appDir = new File(".").getAbsolutePath(); //default to current directory
		}
		
		createDirStructure();
		String filename = "mef.xml";
		String baseDir = "/mgen/resources/app/copy/";
		InputStream stream = getSourceFile(baseDir, filename);
		
		boolean b = copyFile(stream, filename, appDir);
		if (! b)
		{
			return false;
		}
		
		filename = "Global.txt";
		String dest = pathCombine(appDir, "app");
		stream = getSourceFile(baseDir, filename);
		b = copyFile(stream, filename, ".java", dest);
		if (! b)
		{
			return false;
		}
		
		filename = "Boundary.txt";
		dest = pathCombine(appDir, "app\\boundaries");
		stream = getSourceFile(baseDir, filename);
		b = copyFile(stream, filename, ".java", dest);
		if (! b)
		{
			return false;
		}
		
		filename = "MettleInitializer.txt";
		dest = pathCombine(appDir, "app\\mef\\core");
		stream = getSourceFile(baseDir, filename);
		b = copyFile(stream, filename, ".java", dest);
		if (! b)
		{
			return false;
		}
		
		//---mgen---
		filename = "MGen.txt";
		dest = pathCombine(appDir, "mgen");
		stream = getSourceFile(baseDir + "mgen/", filename);
		b = copyFile(stream, filename, ".java", dest);
		if (! b)
		{
			return false;
		}
		
		
		//--- unit test---
		filename = "BaseTest.txt";
		dest = pathCombine(appDir, "test/mef");
		stream = getSourceFile(baseDir + "test/", filename);
		b = copyFile(stream, filename, ".java", dest);
		if (! b)
		{
			return false;
		}

		filename = "BaseMettleTest.txt";
		dest = pathCombine(appDir, "test/mef");
		stream = getSourceFile(baseDir + "test/", filename);
		b = copyFile(stream, filename, ".java", dest);
		if (! b)
		{
			return false;
		}
		
		filename = "BasePresenterTest.txt";
		dest = pathCombine(appDir, "test/mef/presenter");
		stream = getSourceFile(baseDir + "test/presenter/", filename);
		b = copyFile(stream, filename, ".java", dest);
		if (! b)
		{
			return false;
		}

		//controllers
		filename = "ErrorController.txt";
		dest = pathCombine(appDir, "app/controllers");
		stream = getSourceFile(baseDir, filename);
		b = copyFile(stream, filename, ".java", dest);
		if (! b)
		{
			return false;
		}
		
		//views
		filename = "error.scala.html";
		dest = pathCombine(appDir, "app/views");
		stream = getSourceFile(baseDir, filename);
		b = copyFile(stream, filename, null, dest);
		if (! b)
		{
			return false;
		}
		
		return b;
	}
	
	@Override
	public boolean generate(String name) throws Exception
	{
		return false;
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
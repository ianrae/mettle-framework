package org.mef.tools.mgen.codegen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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


public class AppScaffoldCodeGenerator extends CodeGenerator
{
	
	public AppScaffoldCodeGenerator(SfxContext ctx)
	{
		super(ctx);
	}
	
	@Override
	public boolean generate() throws Exception
	{
		createDirStructure();
		String filename = "mef.xml";
		String baseDir = "/mgen/resources/app/copy/";
		InputStream stream = getSourceFile(baseDir, filename);
		
//		String resDir = FilenameUtils.concat(stDir, "copy");
//		String resDir = pathCombine(stDir, "copy");
		boolean b = copyFile(stream, filename, appDir);
		if (! b)
		{
			return false;
		}
		
		filename = "Boundary.txt";
		String dest = pathCombine(appDir, "app\\boundaries");
		stream = getSourceFile(baseDir, filename);
		b = copyFile(stream, filename, ".java", dest);
		if (! b)
		{
			return false;
		}
		
		filename = "Initializer.txt";
		dest = pathCombine(appDir, "app\\mef\\core");
		stream = getSourceFile(baseDir, filename);
		b = copyFile(stream, filename, ".java", dest);
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
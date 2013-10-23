package org.mef.tools.mgen.codegen;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;
import org.mef.framework.sfx.SfxTextWriter;


public class AppScaffoldCodeGenerator extends SfxBaseObj
{
	private String appDir;
	private String stDir;
	public boolean disableFileIO;
	
	public AppScaffoldCodeGenerator(SfxContext ctx)
	{
		super(ctx);
	}
	
	public void init(String appDir, String stDir) throws Exception
	{
		this.appDir = appDir;
		this.stDir = stDir;
	}
	
	public boolean generate() throws Exception
	{
		createDirStructure();
		String filename = "mef.xml";
		
//		String resDir = FilenameUtils.concat(stDir, "copy");
		String resDir = pathCombine(stDir, "copy");
		boolean b = copyFile(filename, resDir, appDir);
		if (! b)
		{
			return false;
		}
		
		filename = "Boundary.txt";
		String dest = pathCombine(appDir, "app\\boundaries");
		b = copyFile(filename, ".java", resDir, dest);
		if (! b)
		{
			return false;
		}
		
		filename = "Initializer.txt";
		dest = pathCombine(appDir, "app\\mef\\core");
		b = copyFile(filename, ".java", resDir, dest);
		if (! b)
		{
			return false;
		}
		
		return b;
	}
	
	private boolean copyFile(String filename, String resDir, String appDir) throws Exception
	{
		return copyFile(filename, null, resDir, appDir);
	}
	private boolean copyFile(String filename, String newExt, String resDir, String appDir) throws Exception
	{
		String src = pathCombine(resDir, filename);
		String dest = pathCombine(appDir, filename);
		
		if (newExt != null)
		{
			dest = FilenameUtils.removeExtension(dest) + newExt;
		}
		
		File fdest = new File(dest);
		if (fdest.exists())
		{
			log(String.format("copy: (skipping, exists) => %s", dest));
			return true;
		}
		
		log(String.format("copy: %s => %s", src, dest));
		if (disableFileIO)
		{
			return true;
		}
		FileUtils.copyFile(new File(src), new File(dest));
		return true;
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

	private void createDir(String dirName) 
	{
		String dest = pathCombine(appDir, dirName);
		log("creating dir: " + dest);
		File f = new File(dest);
		f.mkdirs();
	}

	private boolean writeFile(String appDir, String subDir, String fileName, String code)
	{
		String outPath = this.pathCombine(appDir, subDir);
		outPath = this.pathCombine(outPath, String.format("%s.java", fileName));
		log(fileName + ": " + outPath);
		if (disableFileIO)
		{
			return true;
		}
		
		SfxTextWriter w = new SfxTextWriter(outPath, null);
		w.addLine(code);
		boolean b = w.writeFile();
		return b;
	}
	
}
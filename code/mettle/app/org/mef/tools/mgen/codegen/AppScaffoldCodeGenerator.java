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


public class AppScaffoldCodeGenerator extends SfxBaseObj
{
	private String appDir;
//	private String stDir;
	public boolean disableFileIO;
	
	public AppScaffoldCodeGenerator(SfxContext ctx)
	{
		super(ctx);
	}
	
	public void init(String appDir) throws Exception
	{
		this.appDir = appDir;
//		this.stDir = stDir;
	}
	
	public boolean generate() throws Exception
	{
		createDirStructure();
		String filename = "mef.xml";
		String baseDir = "/mgen/resources/app/copy/";
		InputStream stream = this.getClass().getResourceAsStream(baseDir + filename);
		if (stream == null)
		{
			SfxFileUtils utils = new SfxFileUtils();
			String tmp = utils.getCurrentDir();
			tmp = utils.PathCombine(tmp, "conf/" + baseDir + filename);
			stream = new FileInputStream(tmp);
			log("found at: " + tmp);
		}
		
//		String resDir = FilenameUtils.concat(stDir, "copy");
//		String resDir = pathCombine(stDir, "copy");
		boolean b = copyFile(stream, filename, appDir);
		if (! b)
		{
			return false;
		}
		
//		filename = "Boundary.txt";
//		String dest = pathCombine(appDir, "app\\boundaries");
//		b = copyFile(filename, ".java", resDir, dest);
//		if (! b)
//		{
//			return false;
//		}
//		
//		filename = "Initializer.txt";
//		dest = pathCombine(appDir, "app\\mef\\core");
//		b = copyFile(filename, ".java", resDir, dest);
//		if (! b)
//		{
//			return false;
//		}
		
		return b;
	}
	
	private boolean copyFile(InputStream stream, String filename, String appDir) throws Exception
	{
		return copyFile(stream, filename, null, appDir);
	}
	private boolean copyFile(InputStream stream, String filename, String newExt, String appDir) throws Exception
	{
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
		
		log(String.format("copy: %s => %s", filename, dest));
		if (disableFileIO)
		{
			return true;
		}
		
		List<String> linesL = this.readInputStream(stream);
		SfxTextWriter w = new SfxTextWriter(dest, linesL);
		return w.writeFile();
//		FileUtils.copyFile(new File(src), new File(dest));
	}
	
	List<String> readInputStream(InputStream stream) throws Exception
	{
		List<String> lineL = new ArrayList<String>();
        BufferedReader r = new BufferedReader(new InputStreamReader(stream));

        // reads each line
        String l;
        while((l = r.readLine()) != null) 
        {
        	lineL.add(l);
        } 
        stream.close();	
        
        return lineL;
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
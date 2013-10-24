package org.mef.tools.mgen.codegen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;
import org.mef.framework.sfx.SfxFileUtils;
import org.mef.framework.sfx.SfxTextWriter;

public abstract class CodeGenerator extends SfxBaseObj
{
	protected String appDir;
//	private String stDir;
	public boolean disableFileIO;
	
	public CodeGenerator(SfxContext ctx)
	{
		super(ctx);
	}
	
	public int init(String appDir) throws Exception
	{
		this.appDir = appDir;
//		this.stDir = stDir;
		return 0;
	}
	
	public abstract boolean generate() throws Exception;
	
	public abstract boolean generate(String name) throws Exception;
	
	
	protected InputStream getSourceFile(String baseDir, String filename) throws Exception
	{
		InputStream stream = this.getClass().getResourceAsStream(baseDir + filename);
		if (stream == null)
		{
			SfxFileUtils utils = new SfxFileUtils();
			String tmp = utils.getCurrentDir();
			tmp = utils.PathCombine(tmp, "conf/" + baseDir + filename);
			stream = new FileInputStream(tmp);
			log("found at: " + tmp);
		}
		return stream;
	}
	
	protected String getResourceOrFilePath(String baseDir, String filename)
	{
		InputStream stream = this.getClass().getResourceAsStream(baseDir + filename);
		if (stream != null)
		{
			return baseDir + filename;
		}
		else
		{
			SfxFileUtils utils = new SfxFileUtils();
			String tmp = utils.getCurrentDir();
			tmp = utils.PathCombine(tmp, "conf/" + baseDir + filename);
			log("found at: " + tmp);
			return tmp;
		}
		
	}

	protected boolean copyFile(InputStream stream, String filename, String appDir) throws Exception
	{
		return copyFile(stream, filename, null, appDir);
	}
	protected boolean copyFile(InputStream stream, String filename, String newExt, String appDir) throws Exception
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
	
	protected List<String> readInputStream(InputStream stream) throws Exception
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
	
	protected void createDir(String dirName) 
	{
		String dest = pathCombine(appDir, dirName);
		log("creating dir: " + dest);
		File f = new File(dest);
		f.mkdirs();
	}

	protected boolean writeFile(String appDir, String subDir, String fileName, String code)
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

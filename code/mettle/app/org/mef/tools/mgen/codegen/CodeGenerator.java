package org.mef.tools.mgen.codegen;

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

public abstract class CodeGenerator extends SfxBaseObj
{
	protected String appDir; //path to app root dir.  Generated files will be put under here
	public boolean disableFileIO; //for unit testing
	
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
	
	public abstract boolean generateAll() throws Exception;
	
	public abstract boolean generate(String name) throws Exception;
	
	
	protected InputStream getSourceFile(String baseDir, String filename) throws Exception
	{
		SfxFileUtils utils = new SfxFileUtils();
		String basePath = utils.PathCombine(baseDir, filename);

		InputStream stream = this.getClass().getResourceAsStream(basePath);
		if (stream == null)
		{
			String tmp = utils.getCurrentDir();
			tmp = utils.PathCombine(tmp, "conf/" + basePath);
			stream = new FileInputStream(tmp);
//			log("found at: " + tmp);
		}
		return stream;
	}
	
	protected String getResourceOrFilePath(String baseDir, String filename) throws Exception
	{
		//check for user-defined templates
		String userDefinedPath = getUserDefined(baseDir + filename);
		if (userDefinedPath != null)
		{
			log("using: " + userDefinedPath);
			return userDefinedPath;
		}
				
		
		InputStream stream = this.getClass().getResourceAsStream(baseDir + filename);
		if (stream != null)
		{
			List<String> linesL = readInputStream(stream);
			String path = FileUtils.getTempDirectoryPath();
			path = FilenameUtils.concat(path, filename);
//			log("TO: " + path);
			SfxTextWriter w = new SfxTextWriter(path, linesL);
			w.writeFile(); //track error later!!
			
			return path;
		}
		else
		{
			SfxFileUtils utils = new SfxFileUtils();
			String tmp = utils.getCurrentDir();
			tmp = utils.PathCombine(tmp, "conf/" + baseDir + filename);
//			log("found at: " + tmp);
			return tmp;
		}
		
	}

	private String getUserDefined(String filePath) 
	{
		filePath = filePath.replace("/", "\\");
		if (filePath.startsWith("\\"))
		{
			filePath = filePath.substring(1); //trim
		}
		
		String path = pathCombine(appDir, filePath);
		File f = new File(path);
		if (f.exists())
		{
			return path;
		}
		return null;
	}

	protected boolean copyFile(InputStream stream, String filename, String appDir) throws Exception
	{
		return copyFile(stream, filename, null, appDir);
	}
	protected String prettifyPath(String path)
	{
		String dest2 = path.replace(this.appDir, "");
		dest2 = dest2.replace('\\', '/');
		return dest2;
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
			log(String.format("copy: (skipping, exists) => %s", prettifyPath(dest)));
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
	
	public void createDir(String dirName) 
	{
		String dest = pathCombine(appDir, dirName);
		File f = new File(dest);
		if (! f.exists())
		{
			log("creating dir: " + dest);
			f.mkdirs();
		}
	}

	protected boolean writeFile(String appDir, String subDir, String fileName, String code)
	{
		String outPath = this.pathCombine(appDir, subDir);
//		outPath = this.pathCombine(outPath, String.format("%s.java", fileName));
		outPath = this.pathCombine(outPath, fileName);
		log(fileName + ": " + prettifyPath(outPath));
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

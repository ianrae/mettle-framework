package mef;

import java.io.File;

import mef.core.Initializer;

import org.apache.commons.io.FilenameUtils;
import org.mef.framework.sfx.SfxContext;


public class BaseTest 
{
	protected SfxContext _ctx;
	
	public void init()
	{
		_ctx = Initializer.createContext(true);
	}

	
	protected void log(String s)
	{
		System.out.println(s);
	}
	
	protected String getTestFile(String filepath)
	{
		String currentDir = this.getCurrentDirectory();
		String path = FilenameUtils.concat(currentDir, "test\\testfiles");
		path = FilenameUtils.concat(path, filepath);
		return path;
	}

	protected String getCurrentDir(String filepath)
	{
		String path = getCurrentDirectory();
		if (filepath != null)
		{
			path = pathCombine(path, filepath);
		}
		return path;
	}
	
	private String getCurrentDirectory()
	{
		File f = new File(".");
		return f.getAbsolutePath();
	}
	protected String pathCombine(String path1, String path2)
	{
		if (! path1.endsWith("\\"))
		{
			path1 += "\\";
		}
		String path = path1 + path2;
		return path;
	}	
}

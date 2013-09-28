package unittests;

import static org.junit.Assert.*;

import org.junit.Test;

import sfx.SfxContext;
import sfx.SfxFileUtils;

public class BaseTest 
{
	protected SfxContext _ctx;
	protected void createContext()
	{
		_ctx = new SfxContext();
	}
	
	protected void log(String s)
	{
		System.out.println(s);
	}

	protected String getTestFile(String filepath)
	{
		SfxFileUtils utils = new SfxFileUtils();
		String path = utils.getCurrentDir();
		path = utils.PathCombine(path, "test\\unittests\\testfiles");
		path = utils.PathCombine(path, filepath);
		return path;
	}
	protected String getUnitTestDir(String filepath)
	{
		SfxFileUtils utils = new SfxFileUtils();
		String path = utils.getCurrentDir();
		path = utils.PathCombine(path, "test\\unittests");
		if (filepath != null)
		{
			path = utils.PathCombine(path, filepath);
		}
		return path;
	}
	protected String getCurrentDir(String filepath)
	{
		SfxFileUtils utils = new SfxFileUtils();
		String path = utils.getCurrentDir();
		if (filepath != null)
		{
			path = utils.PathCombine(path, filepath);
		}
		return path;
	}

	protected String getTemplateFile(String filename)
	{
		String stDir = this.getCurrentDir("src\\org\\mef\\dalgen\\resources");
		SfxFileUtils utils = new SfxFileUtils();
		String path = utils.PathCombine(stDir, filename);
		return path;
	}
	
	protected String pathCombine(String path1, String path2)
	{
		SfxFileUtils utils = new SfxFileUtils();
		String path = utils.PathCombine(path1, path2);
		return path;
	}
}

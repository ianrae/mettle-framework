package org.mef.dalgen.unittests;

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
		path = utils.PathCombine(path, "src\\org\\mef\\dalgen\\unittests\\testfiles");
		path = utils.PathCombine(path, filepath);
		return path;
	}
	protected String getUnitTetDir(String filepath)
	{
		SfxFileUtils utils = new SfxFileUtils();
		String path = utils.getCurrentDir();
		path = utils.PathCombine(path, "src\\org\\mef\\dalgen\\unittests");
		path = utils.PathCombine(path, filepath);
		return path;
	}

}

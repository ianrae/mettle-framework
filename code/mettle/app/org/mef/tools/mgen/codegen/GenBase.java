package org.mef.tools.mgen.codegen;

import java.io.File;

import org.mef.framework.sfx.SfxContext;
import org.mef.framework.sfx.SfxErrorTracker;

//base for MGEN and SCAFFOLDGEN
public class GenBase 
{
	protected SfxContext _ctx;


	//--helpers--
	protected void createContext()
	{
		_ctx = new SfxContext();
		_ctx.getServiceLocator().registerSingleton(SfxErrorTracker.class, new SfxErrorTracker(_ctx));
	}

	protected void log(String s)
	{
		System.out.println(s);
	}

	protected String getTestFile(String filepath)
	{
		String path = getCurrentDirectory();
		path = pathCombine(path, "test\\unittests\\testfiles");
		path = pathCombine(path, filepath);
		return path;
	}
	protected String getUnitTestDir(String filepath)
	{
		String path = getCurrentDirectory();
		path = pathCombine(path, "test\\unittests");
		if (filepath != null)
		{
			path = pathCombine(path, filepath);
		}
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

	protected String getCurrentDirectory()
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
	
	protected boolean isNullOrEmpty(String msg) 
	{
		if (msg == null)
		{
			return true;
		}
		return msg.isEmpty();
	}
}

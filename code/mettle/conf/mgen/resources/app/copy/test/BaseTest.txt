package mef;

import static org.junit.Assert.assertEquals;

import java.io.File;

import mef.core.MettleInitializer;

import org.apache.commons.io.FilenameUtils;
import org.mef.framework.sfx.SfxContext;
import org.mef.framework.sfx.SfxErrorTracker;


public class BaseTest 
{
	protected SfxContext _ctx;
	protected boolean enabledConsoleLogging = true;
	
	public void init()
	{
		MettleInitializer initializer = new MettleInitializer();
		_ctx = initializer.createContext();
		_ctx.setVar("UNITTEST", "true");
		
		//now register any mocks...

		//finally, do onStart		
		initializer.onStart();
	}

	protected void chkErrors(int i) 
	{
		SfxErrorTracker tracker = (SfxErrorTracker) _ctx.getServiceLocator().getInstance(SfxErrorTracker.class);
		assertEquals(i, tracker.getErrorCount());
	}

	
	protected void log(String s)
	{
		if (enabledConsoleLogging)
		{
			System.out.println(s);
		}
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

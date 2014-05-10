package org.mef.framework.sfx;


import java.io.File;


/**
 * Most of our classes need an SfxContext object (for logging and other things).
 * 
 * This class is a base class for any object needing an SfxContext object. 
 *
 */
public class SfxBaseObj
{
	protected SfxContext _ctx;
	private ISfxLogger _logger; //gets from ctx in ctor but can change later to local logger
	
	public SfxBaseObj(SfxContext ctx)
	{
		_ctx = ctx;
		_logger = _ctx.getLogger();
	}
	
	public void setLogger(ISfxLogger logger)
	{
		_logger = logger;
	}
	
	protected void log(String msg)
	{
		_logger.log(msg);
	}
	protected void logDebug(String msg)
	{
		_logger.logDebug(msg);
	}
	
	public Object getInstance(Class interfaceClass)
	{
		Object obj = _ctx.getServiceLocator().getInstance(interfaceClass);
		return obj;
	}
//	protected Config getConfigInstance()
//	{
//		Config config = (Config) getInstance(Config.class);
//		return config;
//	}
	public SfxErrorTracker getErrorTrackerInstance()
	{
		SfxErrorTracker track = (SfxErrorTracker) getInstance(SfxErrorTracker.class);
		return track;
	}
	
	public void addError(String errMsg)
	{
		this.getErrorTrackerInstance().errorOccurred(errMsg);
	}
	public void addErrorException(Exception e, String title)
	{
		e.printStackTrace();
		this.addError(String.format("EXCEPTION in %s: %s", title, e.getMessage()));
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


package org.mef.framework;

import org.mef.framework.sfx.SfxLogger;

//mimic Play.logger
//wrap it too!!

public class Logger 
{
	//	  private static final play.Logger$ALogger logger;
	private static SfxLogger logger;

	public Logger()
	{
		
	}

//	public static play.Logger.ALogger of(String message);
//
//	public static play.Logger.ALogger of(java.lang.Class arg0);

	public static boolean isTraceEnabled()
	{
		init();
		return false;
	}

	public static boolean isDebugEnabled()
	{
		init();
		return false;
	}

	public static boolean isInfoEnabled()
	{
		init();
		return false;
	}

	public static boolean isWarnEnabled()
	{
		init();
		return false;
	}

	public static boolean isErrorEnabled()
	{
		init();
		return false;
	}

	public static void trace(String message)
	{
		init();
		logger.log(message);
	}
	

	public static void trace(String message, java.lang.Throwable arg1)
	{
		init();
		logger.log(message + ": " + arg1.getMessage());
	}

	public static void debug(String message)
	{
		init();
		logger.log(message);
	}

	public static void debug(String message, java.lang.Throwable arg1)
	{
		init();
		logger.log(message + ": " + arg1.getMessage());
	}

	public static void info(String message)
	{
		init();
		logger.log(message);
	}

	public static void info(String message, java.lang.Throwable arg1)
	{
		init();
		logger.log(message + ": " + arg1.getMessage());
	}

	public static void warn(String message)
	{
		init();
		logger.log(message);
	}

	public static void warn(String message, java.lang.Throwable arg1)
	{
		init();
		logger.log(message + ": " + arg1.getMessage());
	}

	public static void error(String message)
	{
		init();
		logger.log(message);
	}

	public static void error(String message, java.lang.Throwable arg1)
	{
		init();
		logger.log(message + ": " + arg1.getMessage());
	}
	
	//--helpers---
	private static void init()
	{
		if (logger == null) //not thread-safe!!
		{
			logger = new SfxLogger();
		}
	}

}

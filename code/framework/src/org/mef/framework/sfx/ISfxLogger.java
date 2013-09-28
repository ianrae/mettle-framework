package org.mef.framework.sfx;


/** 
 * Interface for loging object.
 *
 */
public interface ISfxLogger 
{
	void log(String msg);
	void logDebug(String msg);
//	void info(String msg);
//	void error(String string);
//	void warn(String string);
	
	int getLogLevel(); //0=off,1=error,2=warn,3=info,4=debug
	void setLogLevel(int level);
	void enableConsoleOutput(boolean enable);
}


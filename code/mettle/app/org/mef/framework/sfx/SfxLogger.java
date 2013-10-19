package org.mef.framework.sfx;

public class SfxLogger implements ISfxLogger {

	private int _level;//0=off,1=error,2=warn,3=info,4=debug 
//	private SfxTextWriter _fileWriter;
	private boolean _enableConsoleOutput = true;
	
	public SfxLogger()
	{
		_level = 3; //3 is normal
//		_fileWriter = null; //no file output by default
	}
	
//	//If you use this function, you must init,open, and close the fileWriter yourself.
//	//All SfxLogger will do is fileWriter.writeLine
//	public void setFileWriter(SfxTextWriter fileWriter)
//	{
//		_fileWriter = fileWriter;
//	}
	
	private void doLogOutput(String message)
	{
		if (_enableConsoleOutput)
		{
			System.out.println(message);
		}
		
//		if (_fileWriter != null)
//		{
//			_fileWriter.writeLine(message);
//		}
	}
	
	public void logDebug(String message)
	{
		if (_level >= 4)
		{
			doLogOutput(message);
		}
	}
	public void log(String message)
	{
		if (_level >= 0)
		{
			doLogOutput(message);
		}
	}
	public void logError(String message)
	{
		if (_level >= 0)
		{
			doLogOutput(message);
		}
	}
	
	public String limitString(String s, int len)
	{
		if (s.length() > len) {
			s = s.substring(0, len) + "...";
		}
		return s;
	}

	@Override
	public int getLogLevel()
	{
		return _level;
	}

	@Override
	public void setLogLevel(int level) 
	{
		_level = level;
	}
	
	@Override
	public void enableConsoleOutput(boolean enable)
	{
		_enableConsoleOutput  = enable;
	}
	

}

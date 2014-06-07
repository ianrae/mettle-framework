package org.mef.framework.sfx;


public class SfxErrorTracker extends SfxBaseObj
{
	private int _errorCount;
	private String _lastError;
	
	public SfxErrorTracker(SfxContext ctx)
	{
		super(ctx);
	}
	
	public synchronized void errorOccurred(String errMsg)
	{
		_lastError = errMsg;
		_errorCount++;
		this.log("ERROR: " + errMsg);
	}
	
	public int getErrorCount()
	{
		return _errorCount;
	}
}
package org.mef.framework.sfx;


public class SfxErrorTracker extends SfxBaseObj
{
	private int _errorCount;
	private String _lastError;
	private ISfxErrorListener _listener;
	
	public SfxErrorTracker(SfxContext ctx)
	{
		super(ctx);
	}
	
	public void setListener(ISfxErrorListener listener)
	{
		_listener = listener;
	}
	
	public synchronized void errorOccurred(String errMsg)
	{
		_lastError = errMsg;
		_errorCount++;
		this.log("ERROR: " + errMsg);
		if (_listener != null)
		{
			_listener.onError(errMsg);
		}
	}
	
	public int getErrorCount()
	{
		return _errorCount;
	}
}
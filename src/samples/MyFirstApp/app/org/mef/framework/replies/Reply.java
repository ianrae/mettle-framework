package org.mef.framework.replies;

public class Reply 
{
	private boolean _failed;
//	private boolean _badRequest;
	private String _forward;
	private String _view;

	private String _flash;
	
	public static final String VIEW_DEFAULT = ".";
	
	public Reply()
	{
		_view = VIEW_DEFAULT;
	}
	
	
	public boolean failed() 
	{
		return _failed;
	}
	public void setFailed(boolean b) 
	{
		_failed = b;
	}
	public String getForward() 
	{
		return _forward;
	}
	public void setForward(String s) 
	{
		_forward = s;
	}

	public void setFlash(String s) 
	{
		_flash = s;
	}
	public String getFlash()
	{
		return _flash;
	}
	
	public void setViewName(String s)
	{
		_view = s;
	}
	public String getViewName()
	{
		return _view;
	}

}

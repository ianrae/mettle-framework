package org.mef.framework.replies;

public class Reply 
{
	private boolean _failed;
//	private boolean _badRequest;
	//private int _forward;
	private int _destination;

	private String _flash;
	
	public static final int VIEW_NONE = 0;
	public static final int VIEW_INDEX = 1;
	public static final int VIEW_NEW = 2;
	public static final int VIEW_CREATED = 3;
	public static final int VIEW_EDIT = 4;
	public static final int VIEW_UPDATED = 5;
	public static final int VIEW_DELETE = 6;
	public static final int VIEW_DELETED = 7;
	
	
	public static final int FORWARD_INDEX = 101;
	public static final int FORWARD_NEW = 102;
	public static final int FORWARD_EDIT = 104;
	public static final int FORWARD_DELETE = 106;
	public static final int FORWARD_NOT_FOUND = 107;
	public static final int FOWARD_ERROR = 108;
	
	public Reply()
	{
		_destination = VIEW_NONE;
	}
	
	public boolean failed() 
	{
		return _failed;
	}
	public void setFailed(boolean b) 
	{
		_failed = b;
	}
	public boolean isForward()
	{
		return _destination >= 100;
	}

	public void setFlash(String s) 
	{
		_flash = s;
	}
	public String getFlash()
	{
		return _flash;
	}
	
	public void setDestination(int val)
	{
		_destination = val;
	}
	public int getDestination()
	{
		return _destination;
	}

}

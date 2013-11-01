package org.mef.framework.auth;


public class NotLoggedInException extends RuntimeException
{
	public NotLoggedInException()
	{
		super("Only authenticated users can perform this action");
	}
}

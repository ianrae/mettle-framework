package org.mef.framework.auth;

public class NotAuthorizedException extends RuntimeException
{
	public NotAuthorizedException()
	{
		super("User is not authorized to perform this action");
	}
}

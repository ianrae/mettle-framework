package org.mef.framework.auth;

public interface IAuthSubject 
{
	boolean isLoggedIn();
	String getName();
	Object getUserObject();
}

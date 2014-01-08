package org.mef.framework.auth2;

import org.mef.framework.auth.IAuthSubject;

public class AuthUser 
{
	private String name;
	private String userId;
	private String sessionId;
	private IAuthSubject subject;

	public AuthUser()
	{}

	public AuthUser( String name, String userId, String sessionId)
	{
		this.name = name;
		this.userId = userId;
		this.sessionId = sessionId;
	}

	public String getSessionId()
	{
		return sessionId;
	}
	public String getUsername()
	{
		return name;
	}
	public String getUserId()
	{
		return userId;
	}
	public Long getUserIdLong()
	{
		Long val = Long.parseLong(userId);
		return val;
	}
	
	public IAuthSubject getSubject()
	{
		return this.subject;
	}
	public void setSubject(IAuthSubject subj)
	{
		this.subject = subj;
	}
}
package org.mef.framework.auth2;


public class AuthUser 
{
	private String name;
	private String userId;
	private String sessionId;
//	private IAuthSubject subject;
	private Object subject;

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
	
	public Object getSubject()
	{
		return this.subject;
	}
	public void setSubject(Object subj)
	{
		this.subject = subj;
	}
}
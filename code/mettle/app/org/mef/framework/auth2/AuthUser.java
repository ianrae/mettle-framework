package org.mef.framework.auth2;


public class AuthUser 
{
	private String name;
	private String userId;
	private String sessionId;
	private Object subject; //for authorization SRT
	private Object userData; //app-dependent and optional
	
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
	public Object getUserData()
	{
		return this.userData;
	}
	public void setUserData(Object subj)
	{
		this.userData = subj;
	}
}
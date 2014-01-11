package org.mef.framework.presenters;

import org.mef.framework.Logger;
import org.mef.framework.auth.IAuthorizer;
import org.mef.framework.auth.NotAuthorizedException;
import org.mef.framework.auth.NotLoggedInException;
import org.mef.framework.commands.Command;
import org.mef.framework.replies.Reply;
import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;
import org.mef.framework.utils.MethodInvoker;


public class Presenter extends SfxBaseObj 
{
	private IAuthorizer auth;
	
	public Presenter(SfxContext ctx)
	{
		super(ctx);
	}
	
	protected Reply createReply()
	{
		return null;
	}
	
	public void setAuthorizer(IAuthorizer auth)
	{
		this.auth = auth;
	}
	public IAuthorizer getAuthorizer()
	{
		return this.auth;
	}
	
	public Reply process(Command cmd)
	{
		String methodName = cmd.getClass().getName();
		int pos = methodName.lastIndexOf('.');
		if (pos > 0)
		{
			methodName = methodName.substring(pos + 1);
			pos = methodName.indexOf('$');
			if (pos > 0)
			{
				methodName = methodName.substring(pos + 1);
			}
		}
		methodName = "on" + methodName;
		log("looking for: " + methodName);
		
		Reply reply = createReply();
		if (reply == null)
		{
			Logger.warn("you forgot to implement createReply!!");
			reply = new Reply();
			reply.setFailed(true);
			return reply;
		}
		
		Reply tmpReply = doBeforeAction(cmd);
		if (tmpReply != null)
		{
			return tmpReply;
		}
		
		MethodInvoker invoker = new MethodInvoker(_ctx, this, methodName, Command.class);
		
		Object res = invoker.call(cmd, reply);
		doAfterAction(cmd, (Reply)res);
		if (res == null)
		{
			Logger.warn("null from invoker.call!!");
			reply.setFailed(true);
			return reply;
		}

		reply = (Reply)res;
		return reply;
	}
	
	protected Reply doBeforeAction(Command cmd)
	{
		Reply reply = null;
		
		try 
		{
			reply = beforeAction(cmd);
		}
		catch(NotLoggedInException ex)
		{
			reply = createReply();
			reply.setDestination(Reply.FOWARD_NOT_AUTHENTICATED);
		}
		catch(NotAuthorizedException ex)
		{
			reply = createReply();
			reply.setDestination(Reply.FOWARD_NOT_AUTHORIZED);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return reply;
	}
	
	protected void doAfterAction(Command cmd, Reply reply) 
	{
		try 
		{
			afterAction(cmd, reply);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	protected Reply beforeAction(Command cmd) throws Exception
	{
		return null;
	}
	protected void afterAction(Command cmd, Reply reply) throws Exception
	{}

	//---auth---
	protected boolean hasRole(Command cmd, String roleName)
	{
		if (auth == null)
		{
			return false;
		}
		boolean b = auth.isAuth(cmd.authUser, roleName, null);
		return b;
	}
	protected boolean isAuthorized(Command cmd, String roleName, String targetName)
	{
		if (auth == null)
		{
			return false;
		}
		boolean b = auth.isAuth(cmd.authUser, roleName, targetName);
		return b;
	}
	protected boolean isLoggedIn(Command cmd)
	{
		if (cmd.authUser == null)
		{
			return false;
		}
		return true;
	}
	
	protected void ensureHasRole(Command cmd, String roleName) throws Exception
	{
		ensureIsLoggedIn(cmd);
		if (! hasRole(cmd, roleName))
		{
			throw new NotAuthorizedException();
		}
	}
	protected void ensureIsAuth(Command cmd, String roleName, String ticketName) throws Exception
	{
		ensureIsLoggedIn(cmd);
		if (! isAuthorized(cmd, roleName, ticketName))
		{
			throw new NotAuthorizedException();
		}
	}
	protected void ensureIsLoggedIn(Command cmd) throws Exception
	{
		if (! isLoggedIn(cmd))
		{
			throw new NotLoggedInException();
		}
	}

}

package org.mef.framework.presenters;



import org.mef.framework.Logger;
import org.mef.framework.commands.Command;
import org.mef.framework.replies.Reply;
import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;
import org.mef.framework.utils.MethodInvoker;


public class Presenter extends SfxBaseObj 
{
	
	public Presenter(SfxContext ctx)
	{
		super(ctx);
	}
	
	protected Reply createReply()
	{
		return null;
	}
	
	public Reply process(Command cmd)
	{
		String methodName = cmd.getClass().getName();
		int pos = methodName.lastIndexOf('.');
		if (pos > 0)
		{
			methodName = methodName.substring(pos + 1);
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
		
		
		MethodInvoker invoker = new MethodInvoker(_ctx, this, methodName, Command.class);
		
		Object res = invoker.call(cmd);
		if (res == null)
		{
			Logger.warn("null from invoker.call!!");
			reply.setFailed(true);
			return reply;
		}

		reply = (Reply)res;
		return reply;
	}


}

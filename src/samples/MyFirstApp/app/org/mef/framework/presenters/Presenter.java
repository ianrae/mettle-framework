package org.mef.framework.presenters;


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
		
		MethodInvoker invoker = new MethodInvoker(_ctx, this, methodName, Command.class);

		Object res = invoker.call(cmd);
		Reply reply = (Reply)res;
		return reply;
	}

}
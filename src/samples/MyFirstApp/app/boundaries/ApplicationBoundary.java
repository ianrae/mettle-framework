package boundaries;

import mef.presenters.HomePagePresenter;

import org.mef.framework.commands.Command;
import org.mef.framework.replies.Reply;
import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;

public class ApplicationBoundary extends SfxBaseObj
{
	public ApplicationBoundary(SfxContext ctx)
	{
		super(ctx);
	}
	public Reply process(Command cmd, Object route)
	{
		HomePagePresenter presenter = new HomePagePresenter(_ctx);
		
		Reply reply = presenter.process(cmd);
		if (reply.failed())
		{
			return null; //some hard-code error page
		}
		else if (reply.getForward() != null) //change to forward
		{
			return null; //return route
		}
		return reply;
	}
}
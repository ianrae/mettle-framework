package boundaries;

import mef.presenters.HomePagePresenter;

import org.mef.framework.commands.Command;
import org.mef.framework.replies.Reply;
import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;

import play.data.DynamicForm;
import play.data.Form;

public class ApplicationBoundary extends SfxBaseObj
{
	public ApplicationBoundary(SfxContext ctx)
	{
		super(ctx);
	}
	public Reply process(Command cmd, Object route)
	{
		//!!add http cgi params as cmd.setParameters
		DynamicForm dynamicForm = Form.form().bindFromRequest();
		cmd.setParameters(dynamicForm.data());
		
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
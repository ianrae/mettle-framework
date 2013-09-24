package boundaries;

import mef.presenters.HomePagePresenter;
import mef.presenters.HomePageReply;
import models.TaskModel;

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
	
	public Reply process(Command cmd, Form<TaskModel> taskForm)
	{
		FormBinder binder = new FormBinder(taskForm);
		cmd.setFormBinder(binder);
		return process(cmd);
	}
	public Reply process(Command cmd)
	{
		//!!add http cgi params as cmd.setParameters
		DynamicForm dynamicForm = Form.form().bindFromRequest();
		cmd.setParameters(dynamicForm.data());
		
		HomePagePresenter presenter = new HomePagePresenter(_ctx);
		
		Reply reply = presenter.process(cmd);
		if (reply.failed())
		{
			return new HomePageReply(); //null; //some hard-code error page
		}
		else if (reply.getForward() != null) //change to forward
		{
			return new HomePageReply(); //null; //return route
		}
		return reply;
	}
}
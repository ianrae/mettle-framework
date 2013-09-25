package boundaries;

import mef.entities.Task;
import mef.presenters.HomePagePresenter;
import models.TaskModel;

import org.mef.framework.boundaries.BoundaryBase;
import org.mef.framework.commands.Command;
import org.mef.framework.entities.Entity;
import org.mef.framework.replies.Reply;
import org.mef.framework.sfx.SfxContext;

import controllers.routes;
import play.api.mvc.Call;
import play.data.Form;

public class ApplicationBoundary extends BoundaryBase
{
	public ApplicationBoundary(SfxContext ctx)
	{
		super(ctx);
	}
	
	@Override
	protected FormBinder createFormBinder(Command cmd, Entity entity)
	{
		//TaskModel model = Boundary.convertToTaskModel(task);
		Form<TaskModel> taskForm =  Form.form(TaskModel.class);
		FormBinder binder = new FormBinder(taskForm);
		return binder;
	}
	
	@Override
	public Reply process(Command cmd)
	{
		begin(cmd);
		HomePagePresenter presenter = new HomePagePresenter(_ctx);
		
		Reply reply = presenter.process(cmd);
		if (reply.failed())
		{
			result = play.mvc.Results.redirect(routes.Owner.logout());
			return reply;
		}
		return reply;
	}
	
}
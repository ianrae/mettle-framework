package boundaries;

import mef.entities.Task;
import mef.presenters.HomePagePresenter;
import mef.presenters.replies.HomePageReply;
import models.TaskModel;

import org.mef.framework.boundaries.BoundaryBase;
import org.mef.framework.commands.Command;
import org.mef.framework.sfx.SfxContext;

import play.data.Form;
import boundaries.binders.HomeFormBinder;
import controllers.routes;

public class ApplicationBoundary extends BoundaryBase
{
	public ApplicationBoundary(SfxContext ctx)
	{
		super(ctx);
	}
	
	public HomePageReply addFormAndProcess(Command cmd)
	{
		Form<TaskModel> validationForm =  Form.form(TaskModel.class);
		HomeFormBinder binder = new HomeFormBinder(validationForm);
		cmd.setFormBinder(binder);
		return process(cmd);
	}
	
	public Form<Task> makeForm(HomePageReply reply)
	{
		Form<Task> frm =  Form.form(Task.class);
		frm = frm.fill(reply._entity);
		return frm;
	}
	
	@Override
	public HomePageReply process(Command cmd)
	{
		begin(cmd);
		HomePagePresenter presenter = new HomePagePresenter(_ctx);
		
		HomePageReply reply = (HomePageReply) presenter.process(cmd);
		return reply;
	}
	
}
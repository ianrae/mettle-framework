package boundaries;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mef.entities.Task;
import mef.presenters.HomePagePresenter;
import mef.presenters.HomePageReply;
import models.TaskModel;

import org.mef.framework.binder.IFormBinder;
import org.mef.framework.commands.Command;
import org.mef.framework.replies.Reply;
import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;

import controllers.routes;

import play.api.Play;
import play.api.mvc.Call;
import play.data.DynamicForm;
import play.data.Form;
import play.data.validation.ValidationError;
import play.mvc.*;

public class ApplicationBoundary extends SfxBaseObj
{
	public Result result;
	private Command _cmd;
	
	public ApplicationBoundary(SfxContext ctx)
	{
		super(ctx);
	}
	
	public Reply process(Command cmd, Task task)
	{
		//TaskModel model = Boundary.convertToTaskModel(task);
		Form<TaskModel> taskForm =  Form.form(TaskModel.class);
		FormBinder binder = new FormBinder(taskForm);
		cmd.setFormBinder(binder);
		return process(cmd);
	}
	public Reply process(Command cmd)
	{
		_cmd = cmd;
		//!!add http cgi params as cmd.setParameters
		DynamicForm dynamicForm = Form.form().bindFromRequest();
		cmd.setParameters(dynamicForm.data());
		
		HomePagePresenter presenter = new HomePagePresenter(_ctx);
		
		Reply reply = presenter.process(cmd);
		if (reply.failed())
		{
			Call xx = routes.Owner.logout();
			result = play.mvc.Results.redirect(xx);
			return reply;
		}
		return reply;
	}
	
	  public Map<String, List<ValidationError>> getValidationErrors()
	  {
		  IFormBinder binder = _cmd.getFormBinder();
		  if (binder == null)
		  {
			  return new HashMap<String, List<ValidationError>>(); //empty
		  }
		  
		  Map<String, List<ValidationError>> map = (Map<String, List<ValidationError>>) binder.getValidationErrors();
		  return map;
	  }
	
}
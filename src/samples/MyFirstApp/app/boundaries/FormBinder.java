package boundaries;

import mef.entities.Task;
import models.TaskModel;

import org.mef.framework.binder.IFormBinder;

import play.data.Form;

public class FormBinder implements IFormBinder
{
	private Form<TaskModel> taskForm;
	private Form<TaskModel> boundForm;

	public FormBinder(Form<TaskModel> taskForm)
	{
		this.taskForm = taskForm;
	}
	@Override
	public boolean bind() 
	{
		Form<TaskModel> filledForm = taskForm.bindFromRequest();
		this.boundForm = filledForm;
		return ! filledForm.hasErrors();
	}

	@Override
	public Task getObject() 
	{
		TaskModel model = boundForm.get();
		if (model == null)
		{
			return null;
		}
		model.entity = Boundary.convertFromTaskModel(model);
		return model.entity;
	}

}

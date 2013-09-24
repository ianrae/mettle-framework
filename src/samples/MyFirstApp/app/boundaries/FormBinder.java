package boundaries;

import java.util.Map;

import mef.entities.Task;
import models.TaskModel;

import org.mef.framework.binder.IFormBinder;

import play.data.Form;

public class FormBinder implements IFormBinder
{
	private Form<TaskModel> taskForm;
	private Form<TaskModel> filledForm;

	public FormBinder(Form<TaskModel> taskForm)
	{
		this.taskForm = taskForm;
	}
	@Override
	public boolean bind() 
	{
		this.filledForm = taskForm.bindFromRequest();
		return ! filledForm.hasErrors();
	}

	@Override
	public Task getObject() 
	{
		TaskModel model = filledForm.get();
		if (model == null)
		{
			return null;
		}
		model.entity = Boundary.convertFromTaskModel(model);
		return model.entity;
	}
	
	@Override
	public Object getValidationErrors() 
	{
		return filledForm.errors();
	}

}

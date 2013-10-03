package boundaries.binders;

import java.util.Map;

import mef.entities.Task;
import models.TaskModel;

import org.mef.framework.binder.IFormBinder;

import boundaries.daos.TaskDAO;

import play.Logger;
import play.data.Form;

public class HomeFormBinder implements IFormBinder
{
	private Form<TaskModel> taskForm;
	private Form<TaskModel> filledForm;

	public HomeFormBinder(Form<TaskModel> taskForm)
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
		
		Logger.info("555");
		if (model.getLabel() != null)
		{
			Logger.info("55: " + model.getLabel());
		}
		Task entity = TaskDAO.createEntityFromModel(model);
		model.entity = entity;
		return entity;
	}
	
	@Override
	public Object getValidationErrors() 
	{
		return filledForm.errors();
	}
	@Override
	public Object getRawObject() 
	{
		return filledForm.get();
	}

}

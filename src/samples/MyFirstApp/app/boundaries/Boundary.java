package boundaries;

import java.util.ArrayList;
import java.util.List;

import org.mef.framework.sfx.SfxContext;

import boundaries.dals.TaskDAL;


import models.TaskModel;
import mef.core.Initializer;
import mef.entities.Task;
import mef.presenters.HomePagePresenter;

public class Boundary 
{
	public static SfxContext theCtx;
	
	private static void init()
	{
		if (theCtx == null)
		{
			theCtx = Initializer.createContext(new TaskDAL());
		}
	}
	
	public static ApplicationBoundary createApplicationBoundary()
	{
		init();
		return new ApplicationBoundary(theCtx);
	}
	
	public static TaskModel convertToTaskModel(Task entity)
	{
		TaskModel t = new TaskModel();
		t.setId(entity.id);
		t.setLabel(entity.label);
		return t;
	}
	public static Task convertFromTaskModel(TaskModel t)
	{
		Task entity = new Task();
		entity.carrier = t;
		entity.id = t.getId();
		entity.label = t.getLabel();
		return entity;
	}

	public static List<TaskModel> convertToTaskModel(List<Task> entityL)
	{
		ArrayList<TaskModel> L = new ArrayList<TaskModel>();
		for(Task task : entityL)
		{
			TaskModel t = convertToTaskModel(task);
			L.add(t);
		}
		return L;
	}
	public static List<Task> convertFromTask(List<TaskModel> L)
	{
		ArrayList<Task> entityL = new ArrayList<Task>();
		for(TaskModel t : L)
		{
			Task entity = convertFromTaskModel(t);
			entityL.add(entity);
		}
		return entityL;
	}
}

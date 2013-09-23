package boundaries;

import java.util.ArrayList;
import java.util.List;

import org.mef.framework.sfx.SfxContext;


import models.Task;
import mef.core.Initializer;
import mef.entities.TaskEO;
import mef.presenters.HomePagePresenter;

public class Boundary 
{
	public static SfxContext theContext;
//	private static DALTask theDAL;
	private static HomePagePresenter thePresenter;
	
	private static void init()
	{
		if (theContext == null)
		{
//			theDAL = new DALTask();
			theContext = Initializer.createContext(new TaskDAL());
			thePresenter = new HomePagePresenter(theContext);
		}
	}
	
	public static HomePagePresenter getHomePresenter()
	{
		init();
		return thePresenter;
	}
	
	public static Task convertToTask(TaskEO entity)
	{
		Task t = new Task();
		t.setId(entity.id);
		t.setLabel(entity.label);
		return t;
	}
	public static TaskEO convertFromTask(Task t)
	{
		TaskEO entity = new TaskEO();
		entity.id = t.getId();
		entity.label = t.getLabel();
		return entity;
	}

	public static List<Task> convertToTask(List<TaskEO> entityL)
	{
		ArrayList<Task> L = new ArrayList<Task>();
		for(TaskEO task : entityL)
		{
			Task t = convertToTask(task);
			L.add(t);
		}
		return L;
	}
	public static List<TaskEO> convertFromTask(List<Task> L)
	{
		ArrayList<TaskEO> entityL = new ArrayList<TaskEO>();
		for(Task t : L)
		{
			TaskEO entity = convertFromTask(t);
			entityL.add(entity);
		}
		return entityL;
	}
}

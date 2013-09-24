package boundaries;

import java.util.ArrayList;
import java.util.List;

import org.mef.framework.sfx.SfxContext;

import boundaries.dals.TaskDAL;
import boundaries.dals.UserDAL;


import models.TaskModel;
import models.UserModel;
import mef.core.Initializer;
import mef.entities.Task;
import mef.entities.User;
import mef.presenters.HomePagePresenter;

public class Boundary 
{
	public static SfxContext theCtx;
	
	private static void init()
	{
		if (theCtx == null)
		{
			theCtx = Initializer.createContext(new TaskDAL(), new UserDAL()); //fix later!!
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
		t.setEntity(entity);
		t.forceId(entity.id);
		t.setLabel(entity.label);
		return t;
	}
	public static Task convertFromTaskModel(TaskModel t)
	{
		Task entity = new Task();
		entity.carrier = t;
		entity.id = (t.getId() == null) ? 0 : t.getId();
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
	
	//User
	public static UserModel convertToUserModel(User entity)
	{
		UserModel t = new UserModel();
		t.entity = entity;
		t.setId(entity.id);
		t.setName(entity.name);
		//email later!!
		return t;
	}
	public static User convertFromUserModel(UserModel t)
	{
		User entity = new User();
		entity.carrier = t;
		entity.id = (t.getId() == null) ? 0 : t.getId();
		entity.name	= t.getName();
		//!email!!
		return entity;
	}
	public static List<User> convertFromUser(List<UserModel> L)
	{
		ArrayList<User> entityL = new ArrayList<User>();
		for(UserModel t : L)
		{
			User entity = convertFromUserModel(t);
			entityL.add(entity);
		}
		return entityL;
	}
	
}

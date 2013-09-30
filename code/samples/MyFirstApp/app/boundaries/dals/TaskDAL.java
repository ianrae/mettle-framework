package boundaries.dals;

import java.util.ArrayList;
import java.util.List;

import org.mef.framework.binder.IFormBinder;

import boundaries.Boundary;

import models.TaskModel;

import mef.dals.ITaskDAL;
import mef.entities.Task;

public class TaskDAL implements ITaskDAL 
{

	@Override
	public void save(Task entity) 
	{
		TaskModel t = (TaskModel)entity.cc; 
		if (t == null) //not yet known by db? (newly created)
		{
			t = createModelFromEntity(entity);
		}
		t.save();
		entity.id = t.getId(); //in case created on
	}

	@Override
	public Task findById(long id) 
	{
		TaskModel t = TaskModel.find.byId(id);
		Task entity = createEntityFromModel(t);
		t.entity = entity;
		return entity;
	}

	@Override
	public List<Task> all() 
	{
		List<TaskModel> L = TaskModel.find.all();
		List<Task> entityL = convertFromTask(L);
		return entityL;
	}

	@Override
	public int size() 
	{
		return TaskModel.find.all().size();
	}

	@Override
	public void delete(long id) 
	{
		TaskModel t = TaskModel.find.byId(id);
		t.delete();
	}

	@Override
	public Task find_by_label(String val) 
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public static TaskModel createModelFromEntity(Task entity)
	{
		TaskModel t = new TaskModel();
		t.entity = entity;
		t.setId(entity.id);
		t.setLabel(entity.label);
		return t;
	}
	public static Task createEntityFromModel(TaskModel t)
	{
		Task entity = new Task();
		entity.cc = t;
		entity.id = (t.getId() == null) ? 0 : t.getId();
		entity.label = t.getLabel();
		return entity;
	}

	public static List<TaskModel> createModelFromEntity(List<Task> entityL)
	{
		ArrayList<TaskModel> L = new ArrayList<TaskModel>();
		for(Task task : entityL)
		{
			TaskModel t = createModelFromEntity(task);
			L.add(t);
		}
		return L;
	}
	public static List<Task> convertFromTask(List<TaskModel> L)
	{
		ArrayList<Task> entityL = new ArrayList<Task>();
		for(TaskModel t : L)
		{
			Task entity = createEntityFromModel(t);
			entityL.add(entity);
		}
		return entityL;
	}

	@Override
	public void updateFrom(IFormBinder binder) {
		// TODO Auto-generated method stub
		
	}

}

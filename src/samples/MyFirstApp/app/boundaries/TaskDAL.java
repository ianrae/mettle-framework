package boundaries;

import java.util.List;

import models.Task;

import mef.dals.ITaskDAL;
import mef.entities.TaskEO;

public class TaskDAL implements ITaskDAL 
{

	@Override
	public void save(TaskEO entity) 
	{
		Task t = Boundary.convertToTask(entity);
		t.save();
	}

	@Override
	public TaskEO findById(long id) 
	{
		Task t = Task.find.byId(id);
		TaskEO entity = Boundary.convertFromTask(t);
		return entity;
	}

	@Override
	public List<TaskEO> findAll() 
	{
		List<Task> L = Task.all();
		List<TaskEO> entityL = Boundary.convertFromTask(L);
		return entityL;
	}

	@Override
	public int size() 
	{
		return Task.all().size();
	}

	@Override
	public void delete(long id) 
	{
		Task t = Task.find.byId(id);
		t.delete();
	}

}

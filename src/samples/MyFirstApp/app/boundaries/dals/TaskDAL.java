package boundaries.dals;

import java.util.List;

import boundaries.Boundary;

import models.TaskModel;

import mef.dals.ITaskDAL;
import mef.entities.Task;

public class TaskDAL implements ITaskDAL 
{

	@Override
	public void save(Task entity) 
	{
		TaskModel t = Boundary.convertToTaskModel(entity);
		t.save();
	}

	@Override
	public Task findById(long id) 
	{
		TaskModel t = TaskModel.find.byId(id);
		Task entity = Boundary.convertFromTaskModel(t);
		return entity;
	}

	@Override
	public List<Task> all() 
	{
		List<TaskModel> L = TaskModel.all();
		List<Task> entityL = Boundary.convertFromTask(L);
		return entityL;
	}

	@Override
	public int size() 
	{
		return TaskModel.all().size();
	}

	@Override
	public void delete(long id) 
	{
		TaskModel t = TaskModel.find.byId(id);
		t.delete();
	}

}

package mef.dals;

import java.util.List;

import mef.entities.Task;

public interface ITaskDAL
{
	void save(Task t);
	Task findById(long id);
	List<Task> findAll();
	int size();
	void delete(long id); 
}

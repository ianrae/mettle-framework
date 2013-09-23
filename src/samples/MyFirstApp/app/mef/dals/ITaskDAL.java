package mef.dals;

import java.util.List;

import mef.entities.TaskEO;

public interface ITaskDAL
{
	void save(TaskEO t);
	TaskEO findById(long id);
	List<TaskEO> findAll();
	int size();
	void delete(long id); 
}

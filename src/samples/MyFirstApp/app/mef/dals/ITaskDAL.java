package mef.dals;

import java.util.List;

import mef.entities.Task;
public interface ITaskDAL
{
	int size();
	Task findById(long id);
	List<Task> all();
	void delete(long id);
	void save(Task entity);        
        public Task find_by_label(String val);

}

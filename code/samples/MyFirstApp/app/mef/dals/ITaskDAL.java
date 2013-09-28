package mef.dals;

import mef.entities.*;
import java.util.List;
public interface ITaskDAL
{
	int size();
	Task findById(long id);
	List<Task> all();
	void delete(long id);
	void save(Task entity);        
        public Task find_by_label(String val);

}

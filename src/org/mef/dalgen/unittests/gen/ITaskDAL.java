package org.mef.dalgen.unittests.gen;

import org.mef.dal.*;
import java.util.List;
public interface ITaskDAL
{
	int count();
	Task findById(long id);
	List<Task> all();
	void delete(long id);
	void save(Task entity);        
        public Task find_by_label(String val);

}

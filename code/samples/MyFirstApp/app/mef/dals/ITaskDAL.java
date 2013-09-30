package mef.dals;

import mef.entities.*;
import java.util.List;

import org.mef.framework.dal.IDAL;
public interface ITaskDAL extends IDAL
{
	Task findById(long id);
	List<Task> all();
	void save(Task entity);        
        public Task find_by_label(String val);

}

package mef.daos;

import mef.entities.*;
import java.util.List;

import org.mef.framework.dao.IDAO;

public interface ITaskDAO extends IDAO
{
	Task findById(long id);
	List<Task> all();
	void save(Task entity);        
        public Task find_by_label(String val);

}

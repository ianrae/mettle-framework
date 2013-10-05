//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.daos;

import mef.entities.*;
import java.util.List;
import org.mef.framework.binder.IFormBinder;
import org.mef.framework.dao.IDAO;
import mef.gen.*;
public interface ITaskDAO  extends IDAO
{
	Task findById(long id);
	List<Task> all();
	void save(Task entity);        
	void update(Task entity);

    public Task find_by_label(String val);

}

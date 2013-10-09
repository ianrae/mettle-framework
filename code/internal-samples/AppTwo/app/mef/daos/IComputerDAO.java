//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.daos;

import mef.entities.*;
import java.util.List;
import org.mef.framework.binder.IFormBinder;
import org.mef.framework.dao.IDAO;
import mef.gen.*;
import java.util.Date;
import com.avaje.ebean.Page;
public interface IComputerDAO  extends IDAO
{
	Computer findById(long id);
	List<Computer> all();
	void save(Computer entity);        
	void update(Computer entity);

    public Computer find_by_name(String val);

Page<Computer> page(int page, int pageSize,String orderBy, String filter);

}

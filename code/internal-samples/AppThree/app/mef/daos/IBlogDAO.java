//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.daos;

import mef.entities.*;
import java.util.List;
import org.mef.framework.binder.IFormBinder;
import org.mef.framework.dao.IDAO;
import mef.gen.*;
import java.util.Date;
import com.avaje.ebean.Page;
public interface IBlogDAO  extends IDAO
{
	Blog findById(long id);
	List<Blog> all();
	void save(Blog entity);        
	void update(Blog entity);

    public Blog find_by_name(String val);

}

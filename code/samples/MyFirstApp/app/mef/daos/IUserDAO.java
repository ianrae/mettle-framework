//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.daos;

import mef.entities.*;
import java.util.List;
import org.mef.framework.binder.IFormBinder;
import org.mef.framework.dal.IDAL;
import mef.gen.*;
public interface IUserDAO  extends IDAL
{
	User findById(long id);
	List<User> all();
	void save(User entity);        
	void update(User entity);

    public User find_by_name(String val);

List<User> search_by_name(String name);

}

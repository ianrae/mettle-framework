//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.daos;

import mef.entities.*;
import java.util.List;
import org.mef.framework.binder.IFormBinder;
import org.mef.framework.dao.IDAO;
import mef.gen.*;
import java.util.Date;
import com.avaje.ebean.Page;
import org.mef.framework.auth.AuthSubject;
import org.mef.framework.auth.AuthRole;
import org.mef.framework.auth.AuthTicket;
import org.mef.framework.auth.AuthRule;
public interface IUserDAO  extends IDAO
{
	User findById(long id);
	List<User> all();
	void save(User entity);        
	void update(User entity);

    public User find_by_name(String val);

}

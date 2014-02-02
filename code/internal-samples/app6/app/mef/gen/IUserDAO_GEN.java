//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.gen;

import mef.entities.*;
import java.util.List;
import org.mef.framework.binder.IFormBinder;
import org.mef.framework.dao.IDAO;
import mef.gen.*;
import java.util.Date;
import com.avaje.ebean.Page;
import org.mef.framework.fluent.QStep;
import org.mef.framework.fluent.Query1;



public interface IUserDAO_GEN  extends IDAO
{
	User findById(long id);
	List<User> all();
	void save(User entity);        
	void update(User entity);
	public Query1<User> query();


    public User find_by_name(String val);

}

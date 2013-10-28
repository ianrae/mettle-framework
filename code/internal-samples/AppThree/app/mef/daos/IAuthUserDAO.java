//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.daos;

import mef.entities.*;
import java.util.List;
import org.mef.framework.binder.IFormBinder;
import org.mef.framework.dao.IDAO;
import mef.gen.*;
import java.util.Date;
import com.avaje.ebean.Page;
public interface IAuthUserDAO  extends IDAO
{
	AuthUser findById(long id);
	List<AuthUser> all();
	void save(AuthUser entity);        
	void update(AuthUser entity);

    public AuthUser find_by_name(String val);

}

//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.daos;

import mef.entities.*;
import java.util.List;
import org.mef.framework.binder.IFormBinder;
import org.mef.framework.dao.IDAO;
import mef.gen.*;
import java.util.Date;
import com.avaje.ebean.Page;
public interface IAuthSubjectDAO  extends IDAO
{
	AuthSubject findById(long id);
	List<AuthSubject> all();
	void save(AuthSubject entity);        
	void update(AuthSubject entity);

    public AuthSubject find_by_name(String val);

}

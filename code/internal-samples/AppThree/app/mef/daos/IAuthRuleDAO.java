//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.daos;

import mef.entities.*;
import java.util.List;
import org.mef.framework.binder.IFormBinder;
import org.mef.framework.dao.IDAO;
import mef.gen.*;
import java.util.Date;
import com.avaje.ebean.Page;
import org.mef.framework.auth.AuthRole;
import org.mef.framework.auth.AuthTicket;
public interface IAuthRuleDAO  extends IDAO
{
	AuthRule findById(long id);
	List<AuthRule> all();
	void save(AuthRule entity);        
	void update(AuthRule entity);

    AuthRule find_by_subject_and_role_and_ticket(AuthSubject s, AuthRole r, AuthTicket t);

}

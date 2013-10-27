//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.daos.mocks;

import java.util.List;
import java.util.ArrayList;
import mef.entities.*;
import mef.daos.*;
import org.mef.framework.binder.IFormBinder;
import org.codehaus.jackson.map.ObjectMapper;
import mef.gen.*;
import org.mef.framework.entitydb.EntityDB;
import java.util.Date;
import com.avaje.ebean.Page;
public class MockAuthRuleDAO extends MockAuthRuleDAO_GEN
{


	//method
public AuthRule find_by_user_and_role_and_ticket(User u, Role r, Ticket t)
{
	List<AuthRule> L = this.all();
	AuthRule rule = this._entityDB.findFirstMatchEntity(L, "user", u);
	
	if (rule == null)
	{
		return null;
	}
	else
	{
		return L.get(0);
	}
}

}

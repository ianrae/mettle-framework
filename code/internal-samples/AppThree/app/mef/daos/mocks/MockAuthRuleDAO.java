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
import org.mef.framework.entitydb.IValueMatcher;

import java.util.Date;
import com.avaje.ebean.Page;
public class MockAuthRuleDAO extends MockAuthRuleDAO_GEN
{


	//method
public AuthRule find_by_user_and_role_and_ticket(AuthSubject u, AuthRole r, AuthTicket t)
{
	List<AuthRule> L = this.all();
	L = this._entityDB.findMatchesEntity(L, "user", u);
	L = this._entityDB.findMatchesEntity(L, "role", r);
	L = this._entityDB.findMatchesEntity(L, "ticket", t);
	
	
	if (L.size() == 0)
	{
		return null;
	}
	else
	{
		return L.get(0);
	}
}

}

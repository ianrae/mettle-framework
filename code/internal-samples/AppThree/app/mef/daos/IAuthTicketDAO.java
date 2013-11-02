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
public interface IAuthTicketDAO  extends IDAO
{
	AuthTicket findById(long id);
	List<AuthTicket> all();
	void save(AuthTicket entity);        
	void update(AuthTicket entity);

    }

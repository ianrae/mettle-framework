//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.daos;

import mef.entities.*;
import java.util.List;
import org.mef.framework.binder.IFormBinder;
import org.mef.framework.dao.IDAO;
import mef.gen.*;
import java.util.Date;
import com.avaje.ebean.Page;
public interface ITicketDAO  extends IDAO
{
	Ticket findById(long id);
	List<Ticket> all();
	void save(Ticket entity);        
	void update(Ticket entity);

    }

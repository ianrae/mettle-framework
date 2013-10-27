//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.daos;

import mef.entities.*;
import java.util.List;
import org.mef.framework.binder.IFormBinder;
import org.mef.framework.dao.IDAO;
import mef.gen.*;
import java.util.Date;
import com.avaje.ebean.Page;
public interface IAuthRulesDAO  extends IDAO
{
	AuthRules findById(long id);
	List<AuthRules> all();
	void save(AuthRules entity);        
	void update(AuthRules entity);

    }

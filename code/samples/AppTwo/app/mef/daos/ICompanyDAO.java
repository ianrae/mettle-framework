//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.daos;

import mef.entities.*;
import java.util.List;
import org.mef.framework.binder.IFormBinder;
import org.mef.framework.dao.IDAO;
import mef.gen.*;
public interface ICompanyDAO  extends IDAO
{
	Company findById(long id);
	List<Company> all();
	void save(Company entity);        
	void update(Company entity);

    }

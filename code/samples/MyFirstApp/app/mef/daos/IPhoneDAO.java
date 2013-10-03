//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.daos;

import mef.entities.*;
import java.util.List;
import org.mef.framework.binder.IFormBinder;
import org.mef.framework.dao.IDAO;
import mef.gen.*;
public interface IPhoneDAO  extends IDAO
{
	Phone findById(long id);
	List<Phone> all();
	void save(Phone entity);        
	void update(Phone entity);

    public Phone find_by_name(String val);

}

//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.dals;

import mef.entities.*;
import java.util.List;
import org.mef.framework.binder.IFormBinder;
public interface IPhoneDAL
{
	int size();
	Phone findById(long id);
	List<Phone> all();
	void delete(long id);
	void save(Phone entity);        
    void updateFrom(IFormBinder binder);        
    void initFromJson(String json) throws Exception;
    public Phone find_by_name(String val);

}

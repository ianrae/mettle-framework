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

public class MockPhoneDAO implements IPhoneDAO
{
    protected ArrayList<Phone> _L = new ArrayList<Phone>();

    @Override
    public int size() 
    {
        return _L.size();
    }

    @Override
    public Phone findById(long id) 
    {
        for(Phone entity : _L)
        {
            if (entity.id == id)
            {
                return entity;
            }
        }
        return null; //not found
    }

    @Override
    public List<Phone> all() 
    {
        return _L; //ret copy??!!
    }

    @Override
    public void delete(long id) 
    {
        Phone entity = this.findById(id);
        if (entity != null)
        {
            _L.remove(entity);
        }
    }

    @Override
    public void save(Phone entity) 
    {
    	if (entity.id == null)
		{
    		entity.id = new Long(0L);
    	}

        delete(entity.id); //remove existing
        if (entity.id == 0)
        {
        	entity.id = nextAvailIdNumber();
        }

         _L.add(entity);
     }

    private Long nextAvailIdNumber() 
    {
    	long used = 0;
        for(Phone entity : _L)
        {
            if (entity.id > used)
            {
                used = entity.id;
            }
        }
        return used + 1;
	}

	@Override
	public void update(Phone entity) 
	{
		this.save(entity);
	}

    @Override
    public void updateFrom(IFormBinder binder) 
    {
    	Phone entity = (Phone) binder.getObject();
    	save(entity);

    }


	//query
    @Override
    public Phone find_by_name(String val) 
    {
		EntityDB<Phone> db = new EntityDB<Phone>();
		Phone user = db.findFirstMatch(_L, "name", val);
		return user;
    }

}

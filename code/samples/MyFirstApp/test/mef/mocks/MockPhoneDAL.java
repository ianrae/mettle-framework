//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.mocks;

import java.util.List;
import java.util.ArrayList;
import mef.entities.*;
import mef.dals.*;

import org.codehaus.jackson.map.ObjectMapper;
import org.mef.framework.binder.IFormBinder;
public class MockPhoneDAL implements IPhoneDAL
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
    public void updateFrom(IFormBinder binder) 
    {
    	Phone entity = (Phone) binder.getObject();
    	save(entity);

    }

	    @Override
    public Phone find_by_name(String val) 
    {
        for(Phone entity : _L)
        {
            if (entity.name == val)
            {
                return entity;
            }
        }
        return null; //not found
    }
	    
	    
    public void initFromJson(String json) throws Exception
    {
    	ObjectMapper mapper = new ObjectMapper();
    	Phone[] arPhone = mapper.readValue(json, Phone[].class);
    	for(int i = 0; i < arPhone.length; i++)
    	{
    		Phone phone = arPhone[i];
    		Phone existing = this.find_by_name(phone.name);
    		if (existing != null)
    		{
    			phone.id = existing.id;
    		}
    		save(phone); //inserts or updates 
    	}
    }

}

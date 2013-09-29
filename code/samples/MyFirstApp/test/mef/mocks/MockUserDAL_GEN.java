//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.mocks;

import java.util.List;
import java.util.ArrayList;
import mef.entities.*;
import mef.dals.*;

import org.mef.framework.binder.IFormBinder;
import org.mef.framework.sfx.SfxContext;
import org.codehaus.jackson.map.ObjectMapper;
public class MockUserDAL_GEN implements IUserDAL
{
    protected ArrayList<User> _L = new ArrayList<User>();

    @Override
    public int size() 
    {
        return _L.size();
    }

    @Override
    public User findById(long id) 
    {
        for(User entity : _L)
        {
            if (entity.id == id)
            {
                return entity;
            }
        }
        return null; //not found
    }

    @Override
    public List<User> all() 
    {
        return _L; //ret copy??!!
    }

    @Override
    public void delete(long id) 
    {
        User entity = this.findById(id);
        if (entity != null)
        {
            _L.remove(entity);
        }
    }

    @Override
    public void save(User entity) 
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
        for(User entity : _L)
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
    	User entity = (User) binder.getObject();
    	save(entity);

    }
    
    public SfxContext _ctx;

    @Override
    public void initFromJson(String json) throws Exception
    {
		IPhoneDAL dal = (IPhoneDAL) _ctx.getServiceLocator().getInstance(IPhoneDAL.class); 
    	
    	ObjectMapper mapper = new ObjectMapper();
    	User[] arUser = mapper.readValue(json, User[].class);
    	for(int i = 0; i < arUser.length; i++)
    	{
    		User entity = arUser[i];

    		doPhone(entity, dal);
    		
    		
    		User existing = this.find_by_name(entity.name);
    		if (existing != null)
    		{
    			entity.id = existing.id;
    		}
    		save(entity); //inserts or updates 
    	}
    }

	private void doPhone(User entity, IPhoneDAL dal) 
	{
		Phone ph = dal.find_by_name(entity.phone.name);
		
		if (ph != null)
		{
			entity.phone.id = ph.id;
		}
		dal.save(entity.phone); //inserts or updates 
		
	}

	//query
    @Override
    public User find_by_name(String val) 
    {
        for(User entity : _L)
        {
            if (entity.name.equals(val))
            {
                return entity;
            }
        }
        return null; //not found
    }

//method
public List<User> search_by_name(String name)
{
	return null;
}

}

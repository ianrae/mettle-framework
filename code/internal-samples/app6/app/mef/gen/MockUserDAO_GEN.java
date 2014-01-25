//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.gen;

import java.util.List;
import java.util.ArrayList;
import mef.entities.*;
import mef.daos.*;
import org.mef.framework.binder.IFormBinder;
import com.fasterxml.jackson.databind.ObjectMapper;
import mef.gen.*;
import org.mef.framework.entitydb.EntityDB;
import java.util.Date;
import com.avaje.ebean.Page;
public class MockUserDAO_GEN implements IUserDAO
{
    protected List<User> _L = new ArrayList<User>();
    protected EntityDB<User> _entityDB = new EntityDB<User>();

    @Override
    public int size() 
    {
        return _L.size();
    }

    @Override
    public User findById(long id) 
    {
    	User entity = this.findActualById(id);
    	if (entity != null)
    	{
    		return new User(entity); //return copy
        }
        return null; //not found
    }

    protected User findActualById(long id) 
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
        User entity = this.findActualById(id);
        if (entity != null)
        {
            _L.remove(entity);
        }
    }

    @Override
    public void save(User entity) 
    {
    	if (entity.id == null)
		{
    		entity.id = new Long(0L);
    	}

    	if (findActualById(entity.id) != null)
    	{
    		throw new RuntimeException(String.format("save: id %d already exists", entity.id));
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
	public void update(User entity) 
	{
		this.delete(entity.id);
		this.save(entity);
	}

    @Override
    public void updateFrom(IFormBinder binder) 
    {
    	User entity = (User) binder.getObject();
		this.delete(entity.id);
    	save(entity);

    }


	//query
    @Override
    public User find_by_name(String val) 
    {
		User user = _entityDB.findFirstMatch(_L, "name", val);
		return user;
    }

}

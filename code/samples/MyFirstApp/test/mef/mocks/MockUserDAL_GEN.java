//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.mocks;

import java.util.List;
import java.util.ArrayList;
import mef.entities.*;
import mef.dals.*;
import org.mef.framework.binder.IFormBinder;
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
	public void update(User entity) 
	{
		this.save(entity);
	}

    @Override
    public void updateFrom(IFormBinder binder) 
    {
    	User entity = (User) binder.getObject();
    	save(entity);

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

//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.mocks;

import java.util.List;
import java.util.ArrayList;

import org.mef.framework.binder.IFormBinder;

import mef.entities.*;
import mef.dals.*;

public class MockUserDAL implements IUserDAL
{
    private ArrayList<User> _L = new ArrayList<User>();

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
        _L.add(entity);
    }

    @Override
    public void updateFrom(IFormBinder binder) 
    {
    	User entity = (User) binder.getObject();
    	save(entity);

    }
}
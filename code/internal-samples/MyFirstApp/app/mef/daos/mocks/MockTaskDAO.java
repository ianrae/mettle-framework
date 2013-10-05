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

public class MockTaskDAO implements ITaskDAO
{
    protected ArrayList<Task> _L = new ArrayList<Task>();

    @Override
    public int size() 
    {
        return _L.size();
    }

    @Override
    public Task findById(long id) 
    {
        for(Task entity : _L)
        {
            if (entity.id == id)
            {
                return entity;
            }
        }
        return null; //not found
    }

    @Override
    public List<Task> all() 
    {
        return _L; //ret copy??!!
    }

    @Override
    public void delete(long id) 
    {
        Task entity = this.findById(id);
        if (entity != null)
        {
            _L.remove(entity);
        }
    }

    @Override
    public void save(Task entity) 
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
        for(Task entity : _L)
        {
            if (entity.id > used)
            {
                used = entity.id;
            }
        }
        return used + 1;
	}

	@Override
	public void update(Task entity) 
	{
		this.save(entity);
	}

    @Override
    public void updateFrom(IFormBinder binder) 
    {
    	Task entity = (Task) binder.getObject();
    	save(entity);

    }


	//query
    @Override
    public Task find_by_label(String val) 
    {
		EntityDB<Task> db = new EntityDB<Task>();
		Task user = db.findFirstMatch(_L, "label", val);
		return user;
    }

}

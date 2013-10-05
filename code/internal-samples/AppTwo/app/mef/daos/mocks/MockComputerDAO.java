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

public class MockComputerDAO implements IComputerDAO
{
    protected ArrayList<Computer> _L = new ArrayList<Computer>();

    @Override
    public int size() 
    {
        return _L.size();
    }

    @Override
    public Computer findById(long id) 
    {
        for(Computer entity : _L)
        {
            if (entity.id == id)
            {
                return entity;
            }
        }
        return null; //not found
    }

    @Override
    public List<Computer> all() 
    {
        return _L; //ret copy??!!
    }

    @Override
    public void delete(long id) 
    {
        Computer entity = this.findById(id);
        if (entity != null)
        {
            _L.remove(entity);
        }
    }

    @Override
    public void save(Computer entity) 
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
        for(Computer entity : _L)
        {
            if (entity.id > used)
            {
                used = entity.id;
            }
        }
        return used + 1;
	}

	@Override
	public void update(Computer entity) 
	{
		this.save(entity);
	}

    @Override
    public void updateFrom(IFormBinder binder) 
    {
    	Computer entity = (Computer) binder.getObject();
    	save(entity);

    }


	//query
    @Override
    public Computer find_by_name(String val) 
    {
		EntityDB<Computer> db = new EntityDB<Computer>();
		Computer user = db.findFirstMatch(_L, "name", val);
		return user;
    }

}

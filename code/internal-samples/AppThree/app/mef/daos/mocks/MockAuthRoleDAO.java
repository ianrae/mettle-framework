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
import java.util.Date;
import com.avaje.ebean.Page;
public class MockAuthRoleDAO implements IAuthRoleDAO
{
    protected List<AuthRole> _L = new ArrayList<AuthRole>();
    protected EntityDB<AuthRole> _entityDB = new EntityDB<AuthRole>();

    @Override
    public int size() 
    {
        return _L.size();
    }

    @Override
    public AuthRole findById(long id) 
    {
    	AuthRole entity = this.findActualById(id);
    	if (entity != null)
    	{
    		return new AuthRole(entity); //return copy
        }
        return null; //not found
    }

    protected AuthRole findActualById(long id) 
    {
        for(AuthRole entity : _L)
        {
            if (entity.id == id)
            {
                return entity;
            }
        }
        return null; //not found
    }

    @Override
    public List<AuthRole> all() 
    {
        return _L; //ret copy??!!
    }

    @Override
    public void delete(long id) 
    {
        AuthRole entity = this.findActualById(id);
        if (entity != null)
        {
            _L.remove(entity);
        }
    }

    @Override
    public void save(AuthRole entity) 
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
        for(AuthRole entity : _L)
        {
            if (entity.id > used)
            {
                used = entity.id;
            }
        }
        return used + 1;
	}

	@Override
	public void update(AuthRole entity) 
	{
		this.delete(entity.id);
		this.save(entity);
	}

    @Override
    public void updateFrom(IFormBinder binder) 
    {
    	AuthRole entity = (AuthRole) binder.getObject();
		this.delete(entity.id);
    	save(entity);

    }


	//query
    @Override
    public AuthRole find_by_name(String val) 
    {
		AuthRole user = _entityDB.findFirstMatch(_L, "name", val);
		return user;
    }

}

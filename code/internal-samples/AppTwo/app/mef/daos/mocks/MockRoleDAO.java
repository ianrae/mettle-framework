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
public class MockRoleDAO implements IRoleDAO
{
    protected List<Role> _L = new ArrayList<Role>();
    protected EntityDB<Role> _entityDB = new EntityDB<Role>();

    @Override
    public int size() 
    {
        return _L.size();
    }

    @Override
    public Role findById(long id) 
    {
    	Role entity = this.findActualById(id);
    	if (entity != null)
    	{
    		return new Role(entity); //return copy
        }
        return null; //not found
    }

    protected Role findActualById(long id) 
    {
        for(Role entity : _L)
        {
            if (entity.id == id)
            {
                return entity;
            }
        }
        return null; //not found
    }

    @Override
    public List<Role> all() 
    {
        return _L; //ret copy??!!
    }

    @Override
    public void delete(long id) 
    {
        Role entity = this.findActualById(id);
        if (entity != null)
        {
            _L.remove(entity);
        }
    }

    @Override
    public void save(Role entity) 
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
        for(Role entity : _L)
        {
            if (entity.id > used)
            {
                used = entity.id;
            }
        }
        return used + 1;
	}

	@Override
	public void update(Role entity) 
	{
		this.save(entity);
	}

    @Override
    public void updateFrom(IFormBinder binder) 
    {
    	Role entity = (Role) binder.getObject();
    	save(entity);

    }


	//query
    @Override
    public Role find_by_name(String val) 
    {
		Role user = _entityDB.findFirstMatch(_L, "name", val);
		return user;
    }

}

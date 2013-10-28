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
public class MockAuthUserDAO implements IAuthUserDAO
{
    protected List<AuthUser> _L = new ArrayList<AuthUser>();
    protected EntityDB<AuthUser> _entityDB = new EntityDB<AuthUser>();

    @Override
    public int size() 
    {
        return _L.size();
    }

    @Override
    public AuthUser findById(long id) 
    {
    	AuthUser entity = this.findActualById(id);
    	if (entity != null)
    	{
    		return new AuthUser(entity); //return copy
        }
        return null; //not found
    }

    protected AuthUser findActualById(long id) 
    {
        for(AuthUser entity : _L)
        {
            if (entity.id == id)
            {
                return entity;
            }
        }
        return null; //not found
    }

    @Override
    public List<AuthUser> all() 
    {
        return _L; //ret copy??!!
    }

    @Override
    public void delete(long id) 
    {
        AuthUser entity = this.findActualById(id);
        if (entity != null)
        {
            _L.remove(entity);
        }
    }

    @Override
    public void save(AuthUser entity) 
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
        for(AuthUser entity : _L)
        {
            if (entity.id > used)
            {
                used = entity.id;
            }
        }
        return used + 1;
	}

	@Override
	public void update(AuthUser entity) 
	{
		this.delete(entity.id);
		this.save(entity);
	}

    @Override
    public void updateFrom(IFormBinder binder) 
    {
    	AuthUser entity = (AuthUser) binder.getObject();
		this.delete(entity.id);
    	save(entity);

    }


	//query
    @Override
    public AuthUser find_by_name(String val) 
    {
		AuthUser user = _entityDB.findFirstMatch(_L, "name", val);
		return user;
    }

}

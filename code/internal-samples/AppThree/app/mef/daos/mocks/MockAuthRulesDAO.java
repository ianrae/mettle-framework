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
public class MockAuthRulesDAO implements IAuthRulesDAO
{
    protected List<AuthRules> _L = new ArrayList<AuthRules>();
    protected EntityDB<AuthRules> _entityDB = new EntityDB<AuthRules>();

    @Override
    public int size() 
    {
        return _L.size();
    }

    @Override
    public AuthRules findById(long id) 
    {
    	AuthRules entity = this.findActualById(id);
    	if (entity != null)
    	{
    		return new AuthRules(entity); //return copy
        }
        return null; //not found
    }

    protected AuthRules findActualById(long id) 
    {
        for(AuthRules entity : _L)
        {
            if (entity.id == id)
            {
                return entity;
            }
        }
        return null; //not found
    }

    @Override
    public List<AuthRules> all() 
    {
        return _L; //ret copy??!!
    }

    @Override
    public void delete(long id) 
    {
        AuthRules entity = this.findActualById(id);
        if (entity != null)
        {
            _L.remove(entity);
        }
    }

    @Override
    public void save(AuthRules entity) 
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
        for(AuthRules entity : _L)
        {
            if (entity.id > used)
            {
                used = entity.id;
            }
        }
        return used + 1;
	}

	@Override
	public void update(AuthRules entity) 
	{
		this.delete(entity.id);
		this.save(entity);
	}

    @Override
    public void updateFrom(IFormBinder binder) 
    {
    	AuthRules entity = (AuthRules) binder.getObject();
		this.delete(entity.id);
    	save(entity);

    }


	}

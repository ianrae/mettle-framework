//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.gen;

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
import org.mef.framework.auth.AuthSubject;
import org.mef.framework.auth.AuthRole;
import org.mef.framework.auth.AuthTicket;
import org.mef.framework.auth.AuthRule;
public class MockAuthRuleDAO_GEN implements IAuthRuleDAO
{
    protected List<AuthRule> _L = new ArrayList<AuthRule>();
    protected EntityDB<AuthRule> _entityDB = new EntityDB<AuthRule>();

    @Override
    public int size() 
    {
        return _L.size();
    }

    @Override
    public AuthRule findById(long id) 
    {
    	AuthRule entity = this.findActualById(id);
    	if (entity != null)
    	{
    		return new AuthRule(entity); //return copy
        }
        return null; //not found
    }

    protected AuthRule findActualById(long id) 
    {
        for(AuthRule entity : _L)
        {
            if (entity.id == id)
            {
                return entity;
            }
        }
        return null; //not found
    }

    @Override
    public List<AuthRule> all() 
    {
        return _L; //ret copy??!!
    }

    @Override
    public void delete(long id) 
    {
        AuthRule entity = this.findActualById(id);
        if (entity != null)
        {
            _L.remove(entity);
        }
    }

    @Override
    public void save(AuthRule entity) 
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
        for(AuthRule entity : _L)
        {
            if (entity.id > used)
            {
                used = entity.id;
            }
        }
        return used + 1;
	}

	@Override
	public void update(AuthRule entity) 
	{
		this.delete(entity.id);
		this.save(entity);
	}

    @Override
    public void updateFrom(IFormBinder binder) 
    {
    	AuthRule entity = (AuthRule) binder.getObject();
		this.delete(entity.id);
    	save(entity);

    }


	//method
public AuthRule find_by_subject_and_role_and_ticket(AuthSubject s, AuthRole r, AuthTicket t)
{
	return null;
}

}

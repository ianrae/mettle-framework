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
import org.mef.framework.fluent.EntityDBQueryProcessor;
import org.mef.framework.fluent.ProcRegistry;
import org.mef.framework.fluent.QStep;
import org.mef.framework.fluent.Query1;
import org.mef.framework.fluent.QueryContext;
import org.mef.framework.sfx.SfxContext;


public class MockUserDAO_GEN implements IUserDAO
{
    protected List<User> _L = new ArrayList<User>();
    protected EntityDB<User> _entityDB = new EntityDB<User>();
	public QueryContext<User> queryctx; 

	@Override
	public void init(SfxContext ctx)
	{
		this.queryctx = new QueryContext<User>(ctx, User.class);

		ProcRegistry registry = (ProcRegistry) ctx.getServiceLocator().getInstance(ProcRegistry.class);
		EntityDBQueryProcessor<User> proc = new EntityDBQueryProcessor<User>(ctx, _L);
		registry.registerDao(User.class, proc);
	}

	@Override
	public Query1<User> query() 
	{
		queryctx.queryL = new ArrayList<QStep>();
		return new Query1<User>(queryctx);
	}


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
            if (entity.getId() == id)
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
    	if (entity.getId() == null)
		{
    		entity.setId(new Long(0L));
    	}

    	if (findActualById(entity.getId()) != null)
    	{
    		throw new RuntimeException(String.format("save: id %d already exists", entity.getId()));
    	}


        delete(entity.getId()); //remove existing
        if (entity.getId() == 0)
        {
        	entity.setId(nextAvailIdNumber());
        }

         _L.add(entity);
     }

    private Long nextAvailIdNumber() 
    {
    	long used = 0;
        for(User entity : _L)
        {
            if (entity.getId() > used)
            {
                used = entity.getId();
            }
        }
        return used + 1;
	}

	@Override
	public void update(User entity) 
	{
		this.delete(entity.getId());
		this.save(entity);
	}

    @Override
    public void updateFrom(IFormBinder binder) 
    {
    	User entity = (User) binder.getObject();
		this.delete(entity.getId());
    	save(entity);

    }
    @Override
    public void updateFrom(IFormBinder binder, User entity) 
    {
    	updateFrom(binder);
    }

	
	//query
    @Override
    public User find_by_name(String val) 
    {
		User user = _entityDB.findFirstMatch(_L, "name", val);
		return user;
    }

}

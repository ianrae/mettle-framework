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
public class MockComputerDAO_GEN implements IComputerDAO
{
    protected List<Computer> _L = new ArrayList<Computer>();
    protected EntityDB<Computer> _entityDB = new EntityDB<Computer>();

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
		Computer user = _entityDB.findFirstMatch(_L, "name", val);
		return user;
    }

//method
public Page<Computer> page(int page, int pageSize,String orderBy, String filter)
{
	return null;
}

}

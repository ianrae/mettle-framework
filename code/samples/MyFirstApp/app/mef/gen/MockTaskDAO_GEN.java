package mef.gen;

import java.util.List;
import java.util.ArrayList;

import org.mef.framework.binder.IFormBinder;

import mef.entities.*;
import mef.daos.*;

public class MockTaskDAO_GEN implements ITaskDAO
{
    private ArrayList<Task> _L = new ArrayList<Task>();

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
        delete(entity.id); //remove existing
        _L.add(entity);
    }
           @Override
    public Task find_by_label(String val) 
    {
        for(Task entity : _L)
        {
            if (entity.label == val)
            {
                return entity;
            }
        }
        return null; //not found
    }

		@Override
		public void updateFrom(IFormBinder binder) {
			// TODO Auto-generated method stub
			
		}

}

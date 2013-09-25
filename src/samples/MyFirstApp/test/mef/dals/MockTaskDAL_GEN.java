package mef.dals;

import java.util.List;
import java.util.ArrayList;
import mef.entities.*;

public class MockTaskDAL_GEN implements ITaskDAL
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

}

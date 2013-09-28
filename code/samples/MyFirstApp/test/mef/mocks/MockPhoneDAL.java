//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.mocks;

import java.util.List;
import java.util.ArrayList;
import mef.entities.*;
import mef.dals.*;

public class MockPhoneDAL implements IPhoneDAL
{
    private ArrayList<Phone> _L = new ArrayList<Phone>();

    @Override
    public int size() 
    {
        return _L.size();
    }

    @Override
    public Phone findById(long id) 
    {
        for(Phone entity : _L)
        {
            if (entity.id == id)
            {
                return entity;
            }
        }
        return null; //not found
    }

    @Override
    public List<Phone> all() 
    {
        return _L; //ret copy??!!
    }

    @Override
    public void delete(long id) 
    {
        Phone entity = this.findById(id);
        if (entity != null)
        {
            _L.remove(entity);
        }
    }

    @Override
    public void save(Phone entity) 
    {
        delete(entity.id); //remove existing
        _L.add(entity);
    }
       }

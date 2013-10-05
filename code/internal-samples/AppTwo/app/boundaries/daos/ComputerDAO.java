//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.
package boundaries.daos;


import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.mef.framework.binder.IFormBinder;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;

import play.Logger;

import boundaries.Boundary;

import models.ComputerModel;
import play.db.ebean.Model.Finder;

import mef.daos.IComputerDAO;
import mef.entities.Computer;

public class ComputerDAO implements IComputerDAO 
{
	@Override
	public void save(Computer entity) 
	{
		ComputerModel t = (ComputerModel)entity.cc; 
		if (t == null) //not yet known by db? (newly created)
		{
			System.out.println("save-auto-create");
			t = createModelFromEntity(entity); //create model, set entity, and call all setters
		}
		else //touch all (for ebean), except id
		{
			touchAll(t, entity);
		}
		t.save();
		entity.id = t.getId(); //in case created on
	}

	@Override
	public Computer findById(long id) 
	{
		ComputerModel t = ComputerModel.find.byId(id);
		if (t == null)
		{
			return null;
		}

		t.entity = createEntityFromModel(t); //create entity, set m.cc and t.entity, copy all fields from model to entity
		return t.entity;
	}

	@Override
	public List<Computer> all() 
	{
		List<ComputerModel> L = ComputerModel.all();
		List<Computer> entityL = createEntityFromModel(L);
		return entityL;
	}

	@Override
	public int size() 
	{
		return ComputerModel.all().size();
	}

	@Override
	public void delete(long id) 
	{
		ComputerModel t = ComputerModel.find.byId(id);
		t.delete();
	}

	//Computer
	//create model, set entity, and call all setters
	public static ComputerModel createModelFromEntity(Computer entity)
	{
		if (entity == null)
		{
			return null;
		}
		ComputerModel t = new ComputerModel();
		entity.cc = t;
		t.entity = entity;
		touchAll(t, entity);
		return t;
	}
	//create entity, set m.cc and t.entity, copy all fields from model to entity
	public static Computer createEntityFromModel(ComputerModel t)
	{
		if (t == null)
		{
			return null;		
		}

		if (t.entity != null && t.entity.cc != null)
		{
			return t.entity; //already exists
		}
		Computer entity = new Computer();
		entity.cc = t;
		entity.id = (t.getId() == null) ? 0 : t.getId();		
		t.entity = entity;
		touchAll(entity, t);
		return entity;
	}
	public static List<Computer> createEntityFromModel(List<ComputerModel> L)
	{
		ArrayList<Computer> entityL = new ArrayList<Computer>();
		for(ComputerModel t : L)
		{
			Computer entity = createEntityFromModel(t);
			if (entity != null) //why??!!
			{
				entityL.add(entity);
			}
		}
		return entityL;
	}


	@Override
	public void updateFrom(IFormBinder binder) 
	{
		ComputerModel model = (ComputerModel) binder.getRawObject();
		model.update();
	}


	@Override
	public void update(Computer entity) 
	{
		ComputerModel t = (ComputerModel)entity.cc; 
		if (t == null) //not yet known by db? (newly created)
		{
			t.entity = null; //throw exception
		}
		else //touch all (for ebean), except id
		{
			touchAll(t, entity);
		}
		t.update();
	}

       protected static void touchAll(ComputerModel t, Computer entity)
{
	t.setName(entity.name);
}

protected static void touchAll(Computer entity, ComputerModel t)
{
	entity.name = t.getName();
}

    @Override
    public Computer find_by_name(String val) 
    {
      ComputerModel model = ComputerModel.find.where().eq("name", val).findUnique();
	  if (model == null)
	  {
		return null;
	  }
	  Computer entity = createEntityFromModel(model);
	  return entity;
    }

}

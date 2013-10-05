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

import models.PhoneModel;
import play.db.ebean.Model.Finder;

import mef.daos.IPhoneDAO;
import mef.entities.Phone;

public class PhoneDAO implements IPhoneDAO 
{
	@Override
	public void save(Phone entity) 
	{
		PhoneModel t = (PhoneModel)entity.cc; 
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
	public Phone findById(long id) 
	{
		PhoneModel t = PhoneModel.find.byId(id);
		if (t == null)
		{
			return null;
		}

		t.entity = createEntityFromModel(t); //create entity, set m.cc and t.entity, copy all fields from model to entity
		return t.entity;
	}

	@Override
	public List<Phone> all() 
	{
		List<PhoneModel> L = PhoneModel.all();
		List<Phone> entityL = createEntityFromModel(L);
		return entityL;
	}

	@Override
	public int size() 
	{
		return PhoneModel.all().size();
	}

	@Override
	public void delete(long id) 
	{
		PhoneModel t = PhoneModel.find.byId(id);
		t.delete();
	}

	//Phone
	//create model, set entity, and call all setters
	public static PhoneModel createModelFromEntity(Phone entity)
	{
		if (entity == null)
		{
			return null;
		}
		PhoneModel t = new PhoneModel();
		entity.cc = t;
		t.entity = entity;
		touchAll(t, entity);
		return t;
	}
	//create entity, set m.cc and t.entity, copy all fields from model to entity
	public static Phone createEntityFromModel(PhoneModel t)
	{
		if (t == null)
		{
			return null;		
		}

		if (t.entity != null && t.entity.cc != null)
		{
			return t.entity; //already exists
		}
		Phone entity = new Phone();
		entity.cc = t;
		entity.id = (t.getId() == null) ? 0 : t.getId();		
		t.entity = entity;
		touchAll(entity, t);
		return entity;
	}
	public static List<Phone> createEntityFromModel(List<PhoneModel> L)
	{
		ArrayList<Phone> entityL = new ArrayList<Phone>();
		for(PhoneModel t : L)
		{
			Phone entity = createEntityFromModel(t);
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
		PhoneModel model = (PhoneModel) binder.getRawObject();
		model.update();
	}


	@Override
	public void update(Phone entity) 
	{
		PhoneModel t = (PhoneModel)entity.cc; 
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

       protected static void touchAll(PhoneModel t, Phone entity)
{
	t.setName(entity.name);
}

protected static void touchAll(Phone entity, PhoneModel t)
{
	entity.name = t.getName();
}

    @Override
    public Phone find_by_name(String val) 
    {
      PhoneModel model = PhoneModel.find.where().eq("name", val).findUnique();
	  if (model == null)
	  {
		return null;
	  }
	  Phone entity = createEntityFromModel(model);
	  return entity;
    }

}

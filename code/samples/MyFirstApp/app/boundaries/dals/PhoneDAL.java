package boundaries.dals;

import java.util.ArrayList;
import java.util.List;

import mef.dals.IPhoneDAL;
import mef.entities.Phone;
import models.PhoneModel;

import org.codehaus.jackson.map.ObjectMapper;
import org.mef.framework.binder.IFormBinder;

import com.avaje.ebean.Ebean;

public class PhoneDAL implements IPhoneDAL 
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
			t.setName(entity.name);
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
		t.setId(entity.id);
		t.setName(entity.name);
		//email later!!
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
		t.entity = entity;
		entity.id = (t.getId() == null) ? 0 : t.getId();
		entity.name	= t.getName();
		//!email!!
		return entity;
	}
	public static List<Phone> createEntityFromModel(List<PhoneModel> L)
	{
		ArrayList<Phone> entityL = new ArrayList<Phone>();
		for(PhoneModel t : L)
		{
			Phone entity = createEntityFromModel(t);
			entityL.add(entity);
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
	public Phone find_by_name(String val) 
	{
		PhoneModel t = Ebean.find(PhoneModel.class).where().eq("name", val).findUnique();		
		if (t == null)
		{
			return null;
		}
		Phone entity = createEntityFromModel(t);
		return entity;
	}


	@Override
	public void update(Phone entity) 
	{
		PhoneModel t = (PhoneModel)entity.cc; 
		if (t == null) //not yet known by db? (newly created)
		{
			t.entity = null; //throw exception;
		}
		else //touch all (for ebean), except id
		{
//			t.setName(entity.name);
		}
		t.update();
	}

}

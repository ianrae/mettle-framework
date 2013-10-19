//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.
package boundaries.daos;


import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.mef.framework.binder.IFormBinder;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;

import play.Logger;
import java.util.Date;

import boundaries.Boundary;
import boundaries.daos.*;
import mef.core.Initializer;

import models.CompanyModel;
import play.db.ebean.Model.Finder;

import mef.daos.*;
import mef.entities.Company;
import com.avaje.ebean.Page;
public class CompanyDAO implements ICompanyDAO 
{
	@Override
	public void save(Company entity) 
	{
		CompanyModel t = (CompanyModel)entity.cc; 
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
	public Company findById(long id) 
	{
		CompanyModel t = CompanyModel.find.byId(id);
		if (t == null)
		{
			return null;
		}

		t.entity = createEntityFromModel(t); //create entity, set m.cc and t.entity, copy all fields from model to entity
		return t.entity;
	}

	@Override
	public List<Company> all() 
	{
		List<CompanyModel> L = CompanyModel.all();
		List<Company> entityL = createEntityFromModel(L);
		return entityL;
	}

	@Override
	public int size() 
	{
		return CompanyModel.all().size();
	}

	@Override
	public void delete(long id) 
	{
		CompanyModel t = CompanyModel.find.byId(id);
		t.delete();
	}

	//Company
	//create model, set entity, and call all setters
	public static CompanyModel createModelFromEntity(Company entity)
	{
		if (entity == null)
		{
			return null;
		}
		CompanyModel t = new CompanyModel();
		entity.cc = t;
		t.entity = entity;
		touchAll(t, entity);
		return t;
	}
	//create entity, set m.cc and t.entity, copy all fields from model to entity
	public static Company createEntityFromModel(CompanyModel t)
	{
		if (t == null)
		{
			return null;		
		}

		if (t.entity != null && t.entity.cc != null)
		{
			return t.entity; //already exists
		}
		Company entity = new Company();
		entity.cc = t;
		entity.id = (t.getId() == null) ? 0 : t.getId();		
		t.entity = entity;
		touchAll(entity, t);
		return entity;
	}
	public static List<Company> createEntityFromModel(List<CompanyModel> L)
	{
		ArrayList<Company> entityL = new ArrayList<Company>();
		for(CompanyModel t : L)
		{
			Company entity = createEntityFromModel(t);
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
		CompanyModel model = (CompanyModel) binder.getRawObject();
		model.update();
	}


	@Override
	public void update(Company entity) 
	{
		CompanyModel t = (CompanyModel)entity.cc; 
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

       protected static void touchAll(CompanyModel t, Company entity)
{
	t.setId(entity.id);
	t.setName(entity.name);
}

protected static void touchAll(Company entity, CompanyModel t)
{
	entity.name = t.getName();
}

    @Override
    public Company find_by_name(String val) 
    {
      CompanyModel model = CompanyModel.find.where().eq("name", val).findUnique();
	  if (model == null)
	  {
		return null;
	  }
	  Company entity = createEntityFromModel(model);
	  return entity;
    }

}

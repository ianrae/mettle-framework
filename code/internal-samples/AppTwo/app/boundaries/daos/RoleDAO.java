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

import models.RoleModel;
import play.db.ebean.Model.Finder;

import mef.daos.*;
import mef.entities.Role;
import com.avaje.ebean.Page;
public class RoleDAO implements IRoleDAO 
{
	@Override
	public void save(Role entity) 
	{
		RoleModel t = (RoleModel)entity.cc; 
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
	public Role findById(long id) 
	{
		RoleModel t = RoleModel.find.byId(id);
		if (t == null)
		{
			return null;
		}

		t.entity = createEntityFromModel(t); //create entity, set m.cc and t.entity, copy all fields from model to entity
		return t.entity;
	}

	@Override
	public List<Role> all() 
	{
		List<RoleModel> L = RoleModel.all();
		List<Role> entityL = createEntityFromModel(L);
		return entityL;
	}

	@Override
	public int size() 
	{
		return RoleModel.all().size();
	}

	@Override
	public void delete(long id) 
	{
		RoleModel t = RoleModel.find.byId(id);
		t.delete();
	}

	//Role
	//create model, set entity, and call all setters
	public static RoleModel createModelFromEntity(Role entity)
	{
		if (entity == null)
		{
			return null;
		}
		RoleModel t = new RoleModel();
		entity.cc = t;
		t.entity = entity;
		touchAll(t, entity);
		return t;
	}
	//create entity, set m.cc and t.entity, copy all fields from model to entity
	public static Role createEntityFromModel(RoleModel t)
	{
		if (t == null)
		{
			return null;		
		}

		if (t.entity != null && t.entity.cc != null)
		{
			return t.entity; //already exists
		}
		Role entity = new Role();
		entity.cc = t;
		entity.id = (t.getId() == null) ? 0 : t.getId();		
		t.entity = entity;
		touchAll(entity, t);
		return entity;
	}
	public static List<Role> createEntityFromModel(List<RoleModel> L)
	{
		ArrayList<Role> entityL = new ArrayList<Role>();
		for(RoleModel t : L)
		{
			Role entity = createEntityFromModel(t);
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
		RoleModel model = (RoleModel) binder.getRawObject();
		model.update();
	}


	@Override
	public void update(Role entity) 
	{
		RoleModel t = (RoleModel)entity.cc; 
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

       protected static void touchAll(RoleModel t, Role entity)
{
	t.setId(entity.id);
	t.setName(entity.name);
}

protected static void touchAll(Role entity, RoleModel t)
{
	entity.name = t.getName();
}

}

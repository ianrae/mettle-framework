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

import models.AuthRoleModel;
import play.db.ebean.Model.Finder;

import mef.daos.*;
import mef.entities.*;
import com.avaje.ebean.Page;
import org.mef.framework.auth.AuthSubject;
import org.mef.framework.auth.AuthRole;
import org.mef.framework.auth.AuthTicket;
import org.mef.framework.auth.AuthRule;
public class AuthRoleDAO implements IAuthRoleDAO 
{
	@Override
	public void save(AuthRole entity) 
	{
		AuthRoleModel t = (AuthRoleModel)entity.cc; 
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
	public AuthRole findById(long id) 
	{
		AuthRoleModel t = AuthRoleModel.find.byId(id);
		if (t == null)
		{
			return null;
		}

		t.entity = createEntityFromModel(t); //create entity, set m.cc and t.entity, copy all fields from model to entity
		return t.entity;
	}

	@Override
	public List<AuthRole> all() 
	{
		List<AuthRoleModel> L = AuthRoleModel.all();
		List<AuthRole> entityL = createEntityFromModel(L);
		return entityL;
	}

	@Override
	public int size() 
	{
		return AuthRoleModel.all().size();
	}

	@Override
	public void delete(long id) 
	{
		AuthRoleModel t = AuthRoleModel.find.byId(id);
		t.delete();
	}

	//AuthRole
	//create model, set entity, and call all setters
	public static AuthRoleModel createModelFromEntity(AuthRole entity)
	{
		if (entity == null)
		{
			return null;
		}
		AuthRoleModel t = new AuthRoleModel();
		entity.cc = t;
		t.entity = entity;
		touchAll(t, entity);
		return t;
	}
	//create entity, set m.cc and t.entity, copy all fields from model to entity
	public static AuthRole createEntityFromModel(AuthRoleModel t)
	{
		if (t == null)
		{
			return null;		
		}

		if (t.entity != null && t.entity.cc != null)
		{
			return t.entity; //already exists
		}
		AuthRole entity = new AuthRole();
		entity.cc = t;
		entity.id = (t.getId() == null) ? 0 : t.getId();		
		t.entity = entity;
		touchAll(entity, t);
		return entity;
	}
	public static List<AuthRole> createEntityFromModel(List<AuthRoleModel> L)
	{
		ArrayList<AuthRole> entityL = new ArrayList<AuthRole>();
		for(AuthRoleModel t : L)
		{
			AuthRole entity = createEntityFromModel(t);
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
		AuthRoleModel model = (AuthRoleModel) binder.getRawObject();
		model.update();
	}


	@Override
	public void update(AuthRole entity) 
	{
		AuthRoleModel t = (AuthRoleModel)entity.cc; 
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

       protected static void touchAll(AuthRoleModel t, AuthRole entity)
{
	t.setId(entity.id);
	t.setName(entity.name);
}

protected static void touchAll(AuthRole entity, AuthRoleModel t)
{
	entity.name = t.getName();
}

    @Override
    public AuthRole find_by_name(String val) 
    {
      AuthRoleModel model = AuthRoleModel.find.where().eq("name", val).findUnique();
	  if (model == null)
	  {
		return null;
	  }
	  AuthRole entity = createEntityFromModel(model);
	  return entity;
    }

}

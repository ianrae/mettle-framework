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

import models.AuthUserModel;
import play.db.ebean.Model.Finder;

import mef.daos.*;
import mef.entities.*;
import com.avaje.ebean.Page;
public class AuthUserDAO implements IAuthUserDAO 
{
	@Override
	public void save(AuthUser entity) 
	{
		AuthUserModel t = (AuthUserModel)entity.cc; 
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
	public AuthUser findById(long id) 
	{
		AuthUserModel t = AuthUserModel.find.byId(id);
		if (t == null)
		{
			return null;
		}

		t.entity = createEntityFromModel(t); //create entity, set m.cc and t.entity, copy all fields from model to entity
		return t.entity;
	}

	@Override
	public List<AuthUser> all() 
	{
		List<AuthUserModel> L = AuthUserModel.all();
		List<AuthUser> entityL = createEntityFromModel(L);
		return entityL;
	}

	@Override
	public int size() 
	{
		return AuthUserModel.all().size();
	}

	@Override
	public void delete(long id) 
	{
		AuthUserModel t = AuthUserModel.find.byId(id);
		t.delete();
	}

	//User
	//create model, set entity, and call all setters
	public static AuthUserModel createModelFromEntity(AuthUser entity)
	{
		if (entity == null)
		{
			return null;
		}
		AuthUserModel t = new AuthUserModel();
		entity.cc = t;
		t.entity = entity;
		touchAll(t, entity);
		return t;
	}
	//create entity, set m.cc and t.entity, copy all fields from model to entity
	public static AuthUser createEntityFromModel(AuthUserModel t)
	{
		if (t == null)
		{
			return null;		
		}

		if (t.entity != null && t.entity.cc != null)
		{
			return t.entity; //already exists
		}
		AuthUser entity = new AuthUser();
		entity.cc = t;
		entity.id = (t.getId() == null) ? 0 : t.getId();		
		t.entity = entity;
		touchAll(entity, t);
		return entity;
	}
	public static List<AuthUser> createEntityFromModel(List<AuthUserModel> L)
	{
		ArrayList<AuthUser> entityL = new ArrayList<AuthUser>();
		for(AuthUserModel t : L)
		{
			AuthUser entity = createEntityFromModel(t);
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
		AuthUserModel model = (AuthUserModel) binder.getRawObject();
		model.update();
	}


	@Override
	public void update(AuthUser entity) 
	{
		AuthUserModel t = (AuthUserModel)entity.cc; 
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

       protected static void touchAll(AuthUserModel t, AuthUser entity)
{
	t.setId(entity.id);
	t.setName(entity.name);
}

protected static void touchAll(AuthUser entity, AuthUserModel t)
{
	entity.name = t.getName();
}

    @Override
    public AuthUser find_by_name(String val) 
    {
      AuthUserModel model = AuthUserModel.find.where().eq("name", val).findUnique();
	  if (model == null)
	  {
		return null;
	  }
	  AuthUser entity = createEntityFromModel(model);
	  return entity;
    }

}

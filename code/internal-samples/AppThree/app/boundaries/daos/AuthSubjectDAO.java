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

import models.AuthSubjectModel;
import play.db.ebean.Model.Finder;

import mef.daos.*;
import mef.entities.*;
import com.avaje.ebean.Page;
import org.mef.framework.auth.AuthSubject;
import org.mef.framework.auth.AuthRole;
import org.mef.framework.auth.AuthTicket;
import org.mef.framework.auth.AuthRule;
public class AuthSubjectDAO implements IAuthSubjectDAO 
{
	@Override
	public void save(AuthSubject entity) 
	{
		AuthSubjectModel t = (AuthSubjectModel)entity.cc; 
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
	public AuthSubject findById(long id) 
	{
		AuthSubjectModel t = AuthSubjectModel.find.byId(id);
		if (t == null)
		{
			return null;
		}

		t.entity = createEntityFromModel(t); //create entity, set m.cc and t.entity, copy all fields from model to entity
		return t.entity;
	}

	@Override
	public List<AuthSubject> all() 
	{
		List<AuthSubjectModel> L = AuthSubjectModel.all();
		List<AuthSubject> entityL = createEntityFromModel(L);
		return entityL;
	}

	@Override
	public int size() 
	{
		return AuthSubjectModel.all().size();
	}

	@Override
	public void delete(long id) 
	{
		AuthSubjectModel t = AuthSubjectModel.find.byId(id);
		t.delete();
	}

	//AuthSubject
	//create model, set entity, and call all setters
	public static AuthSubjectModel createModelFromEntity(AuthSubject entity)
	{
		if (entity == null)
		{
			return null;
		}
		AuthSubjectModel t = new AuthSubjectModel();
		entity.cc = t;
		t.entity = entity;
		touchAll(t, entity);
		return t;
	}
	//create entity, set m.cc and t.entity, copy all fields from model to entity
	public static AuthSubject createEntityFromModel(AuthSubjectModel t)
	{
		if (t == null)
		{
			return null;		
		}

		if (t.entity != null && t.entity.cc != null)
		{
			return t.entity; //already exists
		}
		AuthSubject entity = new AuthSubject();
		entity.cc = t;
		entity.id = (t.getId() == null) ? 0 : t.getId();		
		t.entity = entity;
		touchAll(entity, t);
		return entity;
	}
	public static List<AuthSubject> createEntityFromModel(List<AuthSubjectModel> L)
	{
		ArrayList<AuthSubject> entityL = new ArrayList<AuthSubject>();
		for(AuthSubjectModel t : L)
		{
			AuthSubject entity = createEntityFromModel(t);
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
		AuthSubjectModel model = (AuthSubjectModel) binder.getRawObject();
		model.update();
	}


	@Override
	public void update(AuthSubject entity) 
	{
		AuthSubjectModel t = (AuthSubjectModel)entity.cc; 
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

       protected static void touchAll(AuthSubjectModel t, AuthSubject entity)
{
	t.setId(entity.id);
	t.setName(entity.name);
}

protected static void touchAll(AuthSubject entity, AuthSubjectModel t)
{
	entity.name = t.getName();
}

    @Override
    public AuthSubject find_by_name(String val) 
    {
      AuthSubjectModel model = AuthSubjectModel.find.where().eq("name", val).findUnique();
	  if (model == null)
	  {
		return null;
	  }
	  AuthSubject entity = createEntityFromModel(model);
	  return entity;
    }

}

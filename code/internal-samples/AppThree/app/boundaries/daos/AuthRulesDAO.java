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

import models.AuthRulesModel;
import play.db.ebean.Model.Finder;

import mef.daos.*;
import mef.entities.AuthRules;
import com.avaje.ebean.Page;
public class AuthRulesDAO implements IAuthRulesDAO 
{
	@Override
	public void save(AuthRules entity) 
	{
		AuthRulesModel t = (AuthRulesModel)entity.cc; 
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
	public AuthRules findById(long id) 
	{
		AuthRulesModel t = AuthRulesModel.find.byId(id);
		if (t == null)
		{
			return null;
		}

		t.entity = createEntityFromModel(t); //create entity, set m.cc and t.entity, copy all fields from model to entity
		return t.entity;
	}

	@Override
	public List<AuthRules> all() 
	{
		List<AuthRulesModel> L = AuthRulesModel.all();
		List<AuthRules> entityL = createEntityFromModel(L);
		return entityL;
	}

	@Override
	public int size() 
	{
		return AuthRulesModel.all().size();
	}

	@Override
	public void delete(long id) 
	{
		AuthRulesModel t = AuthRulesModel.find.byId(id);
		t.delete();
	}

	//AuthRules
	//create model, set entity, and call all setters
	public static AuthRulesModel createModelFromEntity(AuthRules entity)
	{
		if (entity == null)
		{
			return null;
		}
		AuthRulesModel t = new AuthRulesModel();
		entity.cc = t;
		t.entity = entity;
		touchAll(t, entity);
		return t;
	}
	//create entity, set m.cc and t.entity, copy all fields from model to entity
	public static AuthRules createEntityFromModel(AuthRulesModel t)
	{
		if (t == null)
		{
			return null;		
		}

		if (t.entity != null && t.entity.cc != null)
		{
			return t.entity; //already exists
		}
		AuthRules entity = new AuthRules();
		entity.cc = t;
		entity.id = (t.getId() == null) ? 0 : t.getId();		
		t.entity = entity;
		touchAll(entity, t);
		return entity;
	}
	public static List<AuthRules> createEntityFromModel(List<AuthRulesModel> L)
	{
		ArrayList<AuthRules> entityL = new ArrayList<AuthRules>();
		for(AuthRulesModel t : L)
		{
			AuthRules entity = createEntityFromModel(t);
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
		AuthRulesModel model = (AuthRulesModel) binder.getRawObject();
		model.update();
	}


	@Override
	public void update(AuthRules entity) 
	{
		AuthRulesModel t = (AuthRulesModel)entity.cc; 
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

       protected static void touchAll(AuthRulesModel t, AuthRules entity)
{
	t.setId(entity.id);
	t.setUserId(entity.userId);
	t.setRoleId(entity.roleId);
	t.setTicketId(entity.ticketId);
}

protected static void touchAll(AuthRules entity, AuthRulesModel t)
{
	entity.userId = t.getUserId();
	entity.roleId = t.getRoleId();
	entity.ticketId = t.getTicketId();
}

}

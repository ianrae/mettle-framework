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

import models.AuthRuleModel;
import play.db.ebean.Model.Finder;

import mef.daos.*;
import mef.entities.AuthRule;
import mef.entities.Role;
import mef.entities.Ticket;
import mef.entities.User;

import com.avaje.ebean.Page;
public class AuthRuleDAO implements IAuthRuleDAO 
{
	@Override
	public void save(AuthRule entity) 
	{
		AuthRuleModel t = (AuthRuleModel)entity.cc; 
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
	public AuthRule findById(long id) 
	{
		AuthRuleModel t = AuthRuleModel.find.byId(id);
		if (t == null)
		{
			return null;
		}

		t.entity = createEntityFromModel(t); //create entity, set m.cc and t.entity, copy all fields from model to entity
		return t.entity;
	}

	@Override
	public List<AuthRule> all() 
	{
		List<AuthRuleModel> L = AuthRuleModel.all();
		List<AuthRule> entityL = createEntityFromModel(L);
		return entityL;
	}

	@Override
	public int size() 
	{
		return AuthRuleModel.all().size();
	}

	@Override
	public void delete(long id) 
	{
		AuthRuleModel t = AuthRuleModel.find.byId(id);
		t.delete();
	}

	//AuthRule
	//create model, set entity, and call all setters
	public static AuthRuleModel createModelFromEntity(AuthRule entity)
	{
		if (entity == null)
		{
			return null;
		}
		AuthRuleModel t = new AuthRuleModel();
		entity.cc = t;
		t.entity = entity;
		touchAll(t, entity);
		return t;
	}
	//create entity, set m.cc and t.entity, copy all fields from model to entity
	public static AuthRule createEntityFromModel(AuthRuleModel t)
	{
		if (t == null)
		{
			return null;		
		}

		if (t.entity != null && t.entity.cc != null)
		{
			return t.entity; //already exists
		}
		AuthRule entity = new AuthRule();
		entity.cc = t;
		entity.id = (t.getId() == null) ? 0 : t.getId();		
		t.entity = entity;
		touchAll(entity, t);
		return entity;
	}
	public static List<AuthRule> createEntityFromModel(List<AuthRuleModel> L)
	{
		ArrayList<AuthRule> entityL = new ArrayList<AuthRule>();
		for(AuthRuleModel t : L)
		{
			AuthRule entity = createEntityFromModel(t);
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
		AuthRuleModel model = (AuthRuleModel) binder.getRawObject();
		model.update();
	}


	@Override
	public void update(AuthRule entity) 
	{
		AuthRuleModel t = (AuthRuleModel)entity.cc; 
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

       protected static void touchAll(AuthRuleModel t, AuthRule entity)
{
	t.setId(entity.id);
	UserDAO userDAO = (UserDAO)Initializer.theCtx.getServiceLocator().getInstance(IUserDAO.class);
	t.setUser(userDAO.createModelFromEntity(entity.user));
	RoleDAO roleDAO = (RoleDAO)Initializer.theCtx.getServiceLocator().getInstance(IRoleDAO.class);
	t.setRole(roleDAO.createModelFromEntity(entity.role));
	t.setTicketId(entity.ticketId);
}

protected static void touchAll(AuthRule entity, AuthRuleModel t)
{
	UserDAO userDAO = (UserDAO)Initializer.theCtx.getServiceLocator().getInstance(IUserDAO.class);
	entity.user = userDAO.createEntityFromModel(t.getUser());
	RoleDAO roleDAO = (RoleDAO)Initializer.theCtx.getServiceLocator().getInstance(IRoleDAO.class);
	entity.role = roleDAO.createEntityFromModel(t.getRole());
	entity.ticketId = t.getTicketId();
}


@Override
public AuthRule find_by_user_and_role_and_ticket(User u, Role r, Ticket t) {
	// TODO Auto-generated method stub
	return null;
}

}

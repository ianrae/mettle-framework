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

import models.AuthTicketModel;
import play.db.ebean.Model.Finder;

import mef.daos.*;
import mef.entities.*;
import com.avaje.ebean.Page;
public class AuthTicketDAO implements IAuthTicketDAO 
{
	@Override
	public void save(AuthTicket entity) 
	{
		AuthTicketModel t = (AuthTicketModel)entity.cc; 
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
	public AuthTicket findById(long id) 
	{
		AuthTicketModel t = AuthTicketModel.find.byId(id);
		if (t == null)
		{
			return null;
		}

		t.entity = createEntityFromModel(t); //create entity, set m.cc and t.entity, copy all fields from model to entity
		return t.entity;
	}

	@Override
	public List<AuthTicket> all() 
	{
		List<AuthTicketModel> L = AuthTicketModel.all();
		List<AuthTicket> entityL = createEntityFromModel(L);
		return entityL;
	}

	@Override
	public int size() 
	{
		return AuthTicketModel.all().size();
	}

	@Override
	public void delete(long id) 
	{
		AuthTicketModel t = AuthTicketModel.find.byId(id);
		t.delete();
	}

	//AuthTicket
	//create model, set entity, and call all setters
	public static AuthTicketModel createModelFromEntity(AuthTicket entity)
	{
		if (entity == null)
		{
			return null;
		}
		AuthTicketModel t = new AuthTicketModel();
		entity.cc = t;
		t.entity = entity;
		touchAll(t, entity);
		return t;
	}
	//create entity, set m.cc and t.entity, copy all fields from model to entity
	public static AuthTicket createEntityFromModel(AuthTicketModel t)
	{
		if (t == null)
		{
			return null;		
		}

		if (t.entity != null && t.entity.cc != null)
		{
			return t.entity; //already exists
		}
		AuthTicket entity = new AuthTicket();
		entity.cc = t;
		entity.id = (t.getId() == null) ? 0 : t.getId();		
		t.entity = entity;
		touchAll(entity, t);
		return entity;
	}
	public static List<AuthTicket> createEntityFromModel(List<AuthTicketModel> L)
	{
		ArrayList<AuthTicket> entityL = new ArrayList<AuthTicket>();
		for(AuthTicketModel t : L)
		{
			AuthTicket entity = createEntityFromModel(t);
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
		AuthTicketModel model = (AuthTicketModel) binder.getRawObject();
		model.update();
	}


	@Override
	public void update(AuthTicket entity) 
	{
		AuthTicketModel t = (AuthTicketModel)entity.cc; 
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

       protected static void touchAll(AuthTicketModel t, AuthTicket entity)
{
	t.setId(entity.id);
}

protected static void touchAll(AuthTicket entity, AuthTicketModel t)
{
}

}

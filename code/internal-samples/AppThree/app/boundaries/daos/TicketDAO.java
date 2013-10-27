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

import models.TicketModel;
import play.db.ebean.Model.Finder;

import mef.daos.*;
import mef.entities.Ticket;
import com.avaje.ebean.Page;
public class TicketDAO implements ITicketDAO 
{
	@Override
	public void save(Ticket entity) 
	{
		TicketModel t = (TicketModel)entity.cc; 
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
	public Ticket findById(long id) 
	{
		TicketModel t = TicketModel.find.byId(id);
		if (t == null)
		{
			return null;
		}

		t.entity = createEntityFromModel(t); //create entity, set m.cc and t.entity, copy all fields from model to entity
		return t.entity;
	}

	@Override
	public List<Ticket> all() 
	{
		List<TicketModel> L = TicketModel.all();
		List<Ticket> entityL = createEntityFromModel(L);
		return entityL;
	}

	@Override
	public int size() 
	{
		return TicketModel.all().size();
	}

	@Override
	public void delete(long id) 
	{
		TicketModel t = TicketModel.find.byId(id);
		t.delete();
	}

	//Ticket
	//create model, set entity, and call all setters
	public static TicketModel createModelFromEntity(Ticket entity)
	{
		if (entity == null)
		{
			return null;
		}
		TicketModel t = new TicketModel();
		entity.cc = t;
		t.entity = entity;
		touchAll(t, entity);
		return t;
	}
	//create entity, set m.cc and t.entity, copy all fields from model to entity
	public static Ticket createEntityFromModel(TicketModel t)
	{
		if (t == null)
		{
			return null;		
		}

		if (t.entity != null && t.entity.cc != null)
		{
			return t.entity; //already exists
		}
		Ticket entity = new Ticket();
		entity.cc = t;
		entity.id = (t.getId() == null) ? 0 : t.getId();		
		t.entity = entity;
		touchAll(entity, t);
		return entity;
	}
	public static List<Ticket> createEntityFromModel(List<TicketModel> L)
	{
		ArrayList<Ticket> entityL = new ArrayList<Ticket>();
		for(TicketModel t : L)
		{
			Ticket entity = createEntityFromModel(t);
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
		TicketModel model = (TicketModel) binder.getRawObject();
		model.update();
	}


	@Override
	public void update(Ticket entity) 
	{
		TicketModel t = (TicketModel)entity.cc; 
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

       protected static void touchAll(TicketModel t, Ticket entity)
{
	t.setId(entity.id);
}

protected static void touchAll(Ticket entity, TicketModel t)
{
}

}
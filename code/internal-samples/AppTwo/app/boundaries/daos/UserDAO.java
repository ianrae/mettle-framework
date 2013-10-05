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

import models.UserModel;
import play.db.ebean.Model.Finder;

import mef.daos.*;
import mef.entities.User;

public class UserDAO implements IUserDAO 
{
	@Override
	public void save(User entity) 
	{
		UserModel t = (UserModel)entity.cc; 
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
	public User findById(long id) 
	{
		UserModel t = UserModel.find.byId(id);
		if (t == null)
		{
			return null;
		}

		t.entity = createEntityFromModel(t); //create entity, set m.cc and t.entity, copy all fields from model to entity
		return t.entity;
	}

	@Override
	public List<User> all() 
	{
		List<UserModel> L = UserModel.all();
		List<User> entityL = createEntityFromModel(L);
		return entityL;
	}

	@Override
	public int size() 
	{
		return UserModel.all().size();
	}

	@Override
	public void delete(long id) 
	{
		UserModel t = UserModel.find.byId(id);
		t.delete();
	}

	//User
	//create model, set entity, and call all setters
	public static UserModel createModelFromEntity(User entity)
	{
		if (entity == null)
		{
			return null;
		}
		UserModel t = new UserModel();
		entity.cc = t;
		t.entity = entity;
		touchAll(t, entity);
		return t;
	}
	//create entity, set m.cc and t.entity, copy all fields from model to entity
	public static User createEntityFromModel(UserModel t)
	{
		if (t == null)
		{
			return null;		
		}

		if (t.entity != null && t.entity.cc != null)
		{
			return t.entity; //already exists
		}
		User entity = new User();
		entity.cc = t;
		entity.id = (t.getId() == null) ? 0 : t.getId();		
		t.entity = entity;
		touchAll(entity, t);
		return entity;
	}
	public static List<User> createEntityFromModel(List<UserModel> L)
	{
		ArrayList<User> entityL = new ArrayList<User>();
		for(UserModel t : L)
		{
			User entity = createEntityFromModel(t);
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
		UserModel model = (UserModel) binder.getRawObject();
		model.update();
	}


	@Override
	public void update(User entity) 
	{
		UserModel t = (UserModel)entity.cc; 
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

       protected static void touchAll(UserModel t, User entity)
{
	t.setName(entity.name);
}

protected static void touchAll(User entity, UserModel t)
{
	entity.name = t.getName();
}

}

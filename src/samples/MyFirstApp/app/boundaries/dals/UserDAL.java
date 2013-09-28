package boundaries.dals;

import java.util.ArrayList;
import java.util.List;

import play.Logger;

import boundaries.Boundary;

import models.UserModel;

import mef.dals.IUserDAL;
import mef.entities.User;

public class UserDAL implements IUserDAL 
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
			t.setName(entity.name);
		}
		t.save();
	}
	

	@Override
	public User findById(long id) 
	{
		UserModel t = UserModel.find.byId(id);
		Logger.info("HERE GOES:");
//		UserModel t = UserModel.find.fetch("phone").where(String.format("id=%d",id)).findUnique();
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
		UserModel t = new UserModel();
		entity.cc = t;
		t.entity = entity;
		t.setId(entity.id);
		t.setName(entity.name);
		//email later!!
		return t;
	}
	//create entity, set m.cc and t.entity, copy all fields from model to entity
	public static User createEntityFromModel(UserModel t)
	{
		if (t.entity != null && t.entity.cc != null)
		{
			return t.entity; //already exists
		}
		User entity = new User();
		entity.cc = t;
		t.entity = entity;
		entity.id = (t.getId() == null) ? 0 : t.getId();
		entity.name	= t.getName();
		//!email!!
		return entity;
	}
	public static List<User> createEntityFromModel(List<UserModel> L)
	{
		ArrayList<User> entityL = new ArrayList<User>();
		for(UserModel t : L)
		{
			User entity = createEntityFromModel(t);
			entityL.add(entity);
		}
		return entityL;
	}

}

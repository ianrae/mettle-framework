package boundaries.dals;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.mef.framework.binder.IFormBinder;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;

import play.Logger;

import boundaries.Boundary;

import models.UserModel;

import mef.dals.IUserDAL;
import mef.entities.Task;
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
		Logger.info("HERE GOES:");
//		UserModel t = UserModel.find.byId(id);
		
//		String oql = 
//		        "  find  user_model "
////		        +" fetch phone "
//		        +" where order.id = :id";
//		   
//		 Query<UserModel> query = Ebean.createQuery(UserModel.class, oql);
//		 query.setParameter("id", id);
//		UserModel t = query.findUnique();
		
		//http://www.avaje.org/static/javadoc/pub/com/avaje/ebean/Query.html
		UserModel t = Ebean.find(UserModel.class).fetch("phone").where().eq("id", id).findUnique();		
		
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
		t.setPhone(PhoneDAL.createModelFromEntity(entity.phone));
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
		entity.phone = PhoneDAL.createEntityFromModel(t.getPhone());
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


	@Override
	public void updateFrom(IFormBinder binder) 
	{
		UserModel model = (UserModel) binder.getRawObject();
		model.update();
	}


	@Override
	public List<User> search_by_name(String name) 
	{
//		UserModel t = Ebean.find(UserModel.class).fetch("phone").where().eq("id", id).findUnique();		
		List<User> resultsL = new ArrayList<User>(); //1!
		return resultsL;
	}


	@Override
	public User find_by_name(String val) {
		// TODO Auto-generated method stub
		return null;
	}
	
}

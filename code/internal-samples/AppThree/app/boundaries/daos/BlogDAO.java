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

import models.BlogModel;
import play.db.ebean.Model.Finder;

import mef.daos.*;
import mef.entities.*;
import com.avaje.ebean.Page;
public class BlogDAO implements IBlogDAO 
{
	@Override
	public void save(Blog entity) 
	{
		BlogModel t = (BlogModel)entity.cc; 
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
	public Blog findById(long id) 
	{
		BlogModel t = BlogModel.find.byId(id);
		if (t == null)
		{
			return null;
		}

		t.entity = createEntityFromModel(t); //create entity, set m.cc and t.entity, copy all fields from model to entity
		return t.entity;
	}

	@Override
	public List<Blog> all() 
	{
		List<BlogModel> L = BlogModel.all();
		List<Blog> entityL = createEntityFromModel(L);
		return entityL;
	}

	@Override
	public int size() 
	{
		return BlogModel.all().size();
	}

	@Override
	public void delete(long id) 
	{
		BlogModel t = BlogModel.find.byId(id);
		t.delete();
	}

	//Blog
	//create model, set entity, and call all setters
	public static BlogModel createModelFromEntity(Blog entity)
	{
		if (entity == null)
		{
			return null;
		}
		BlogModel t = new BlogModel();
		entity.cc = t;
		t.entity = entity;
		touchAll(t, entity);
		return t;
	}
	//create entity, set m.cc and t.entity, copy all fields from model to entity
	public static Blog createEntityFromModel(BlogModel t)
	{
		if (t == null)
		{
			return null;		
		}

		if (t.entity != null && t.entity.cc != null)
		{
			return t.entity; //already exists
		}
		Blog entity = new Blog();
		entity.cc = t;
		entity.id = (t.getId() == null) ? 0 : t.getId();		
		t.entity = entity;
		touchAll(entity, t);
		return entity;
	}
	public static List<Blog> createEntityFromModel(List<BlogModel> L)
	{
		ArrayList<Blog> entityL = new ArrayList<Blog>();
		for(BlogModel t : L)
		{
			Blog entity = createEntityFromModel(t);
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
		BlogModel model = (BlogModel) binder.getRawObject();
		model.update();
	}


	@Override
	public void update(Blog entity) 
	{
		BlogModel t = (BlogModel)entity.cc; 
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

       protected static void touchAll(BlogModel t, Blog entity)
{
	t.setId(entity.id);
	t.setName(entity.name);
}

protected static void touchAll(Blog entity, BlogModel t)
{
	entity.name = t.getName();
}

    @Override
    public Blog find_by_name(String val) 
    {
      BlogModel model = BlogModel.find.where().eq("name", val).findUnique();
	  if (model == null)
	  {
		return null;
	  }
	  Blog entity = createEntityFromModel(model);
	  return entity;
    }

}

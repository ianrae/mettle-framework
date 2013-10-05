//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.
package boundaries.daos;


import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.mef.framework.binder.IFormBinder;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;

import play.Logger;

import boundaries.Boundary;

import models.TaskModel;
import play.db.ebean.Model.Finder;

import mef.daos.ITaskDAO;
import mef.entities.Task;

public class TaskDAO implements ITaskDAO 
{
	@Override
	public void save(Task entity) 
	{
		TaskModel t = (TaskModel)entity.cc; 
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
	public Task findById(long id) 
	{
		TaskModel t = TaskModel.find.byId(id);
		if (t == null)
		{
			return null;
		}

		t.entity = createEntityFromModel(t); //create entity, set m.cc and t.entity, copy all fields from model to entity
		return t.entity;
	}

	@Override
	public List<Task> all() 
	{
		List<TaskModel> L = TaskModel.all();
		List<Task> entityL = createEntityFromModel(L);
		return entityL;
	}

	@Override
	public int size() 
	{
		return TaskModel.all().size();
	}

	@Override
	public void delete(long id) 
	{
		TaskModel t = TaskModel.find.byId(id);
		t.delete();
	}

	//Task
	//create model, set entity, and call all setters
	public static TaskModel createModelFromEntity(Task entity)
	{
		if (entity == null)
		{
			return null;
		}
		TaskModel t = new TaskModel();
		entity.cc = t;
		t.entity = entity;
		touchAll(t, entity);
		return t;
	}
	//create entity, set m.cc and t.entity, copy all fields from model to entity
	public static Task createEntityFromModel(TaskModel t)
	{
		if (t == null)
		{
			return null;		
		}

		if (t.entity != null && t.entity.cc != null)
		{
			return t.entity; //already exists
		}
		Task entity = new Task();
		entity.cc = t;
		entity.id = (t.getId() == null) ? 0 : t.getId();		
		t.entity = entity;
		touchAll(entity, t);
		return entity;
	}
	public static List<Task> createEntityFromModel(List<TaskModel> L)
	{
		ArrayList<Task> entityL = new ArrayList<Task>();
		for(TaskModel t : L)
		{
			Task entity = createEntityFromModel(t);
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
		TaskModel model = (TaskModel) binder.getRawObject();
		model.update();
	}


	@Override
	public void update(Task entity) 
	{
		TaskModel t = (TaskModel)entity.cc; 
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

       protected static void touchAll(TaskModel t, Task entity)
{
	t.setLabel(entity.label);
	t.setEnabled(entity.enabled);
}

protected static void touchAll(Task entity, TaskModel t)
{
	entity.label = t.getLabel();
	entity.enabled = t.getEnabled();
}

    @Override
    public Task find_by_label(String val) 
    {
      TaskModel model = TaskModel.find.where().eq("label", val).findUnique();
	  if (model == null)
	  {
		return null;
	  }
	  Task entity = createEntityFromModel(model);
	  return entity;
    }

}

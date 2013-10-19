package tools;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class DALTests extends BaseTest
{
	public static class Entity
	{
		public Object carrier;
	}
	public static class Task extends Entity
	{
		public long id;
		public String label;
		public boolean enabled;
	}
	
	public static class TaskModel //extends Model
	{
		private Task entity;
		
		public void initEntity(Task entity)
		{
			this.entity = entity;
		}
		
		//getters and setters
		public String getLabel() {
			return this.entity.label;
		}
		public void setLabel(String label) {
			this.entity.label = label;
		}
		public boolean isEnabled() {
			return this.entity.enabled;
		}
		public void setEnabled(boolean enabled) {
			this.entity.enabled = enabled;
		}
		public long getId() 
		{
			return entity.id;
		}
		public void forceId(long id) 
		{
			this.entity.id = id;
		}
	}
	

	public static interface ITaskDAL
	{
		int count();
		Task findById(long id);
		List<Task> all();
		void delete(long id);
		void save(Task entity);
		
		Task find_by_label(String label);
	}
	
	public static class MockTaskDAL implements ITaskDAL
	{
		private ArrayList<Task> _L = new ArrayList<DALTests.Task>();
		
		@Override
		public int count() 
		{
			return _L.size();
		}

		@Override
		public Task findById(long id) 
		{
			for(Task entity : _L)
			{
				if (entity.id == id)
				{
					return entity;
				}
			}
			return null; //not found
		}

		@Override
		public List<Task> all() 
		{
			return _L; //ret copy??!!
		}

		@Override
		public void delete(long id) 
		{
			Task entity = this.findById(id);
			if (entity != null)
			{
				_L.remove(entity);
			}
		}

		@Override
		public void save(Task entity) 
		{
			delete(entity.id); //remove existing
			_L.add(entity);
		}

		@Override
		public Task find_by_label(String label) 
		{
			for(Task entity : _L)
			{
				if (entity.label == label)
				{
					return entity;
				}
			}
			return null; //not found
		}
	}
	
	public static class TaskDALUtils
	{
		public static void copyTo(Task entity, TaskModel model)
		{
			model.initEntity(entity);
//			model.forceId(entity.id);
//			model.setLabel(entity.label);
//			model.setEnabled(entity.enabled);
		}
		public static void copyTo(TaskModel model, Task entity)
		{
			if (model.entity == entity)
			{
				return;
			}
			entity.id = model.getId();
			entity.label = model.getLabel();
			entity.enabled = model.isEnabled();
		}
		public static void touch(TaskModel model)
		{
			//does nothing but eBean likes to see setters called
			Task entity = model.entity;
//			model.forceId(entity.id); don't touch id
			model.setLabel(entity.label);
			model.setEnabled(entity.enabled);
		}
	}
	
	
	@Test
	public void test() 
	{
		Task entity = new Task();
		entity.label = "abc";
		
		TaskModel model = new TaskModel();
		model.initEntity(entity);
		assertEquals("abc", model.getLabel());
	}

	@Test
	public void testDAL() 
	{
		Task entity = new Task();
		entity.id = 44;
		entity.label = "abc";
		
		ITaskDAL dal = new MockTaskDAL();
		assertEquals(0, dal.count());
		assertEquals(0, dal.all().size());
		assertEquals(null, dal.findById(44));

		dal.save(entity);
		assertEquals(1, dal.count());
		assertEquals(1, dal.all().size());
		assertEquals(44, dal.all().get(0).id);
		
		assertEquals(entity, dal.findById(44));
		assertEquals(entity, dal.find_by_label("abc"));
		assertEquals(null, dal.find_by_label("def"));
	}

	@Test
	public void testUtils() 
	{
		Task entity = new Task();
		entity.id = 44;
		entity.label = "abc";
		entity.enabled = true;
		
		TaskModel model = new TaskModel();
		TaskDALUtils.copyTo(entity, model);
		
		assertEquals(44, model.getId());
		assertEquals("abc", model.getLabel());
		assertEquals(true, model.isEnabled());
		
		Task entity2 = new Task();
		TaskDALUtils.copyTo(model, entity2);
		assertEquals(44, entity2.id);
		assertEquals("abc", entity2.label);
		assertEquals(true, entity2.enabled);
	}		
}

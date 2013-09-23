package mef.dals;

import java.util.ArrayList;
import java.util.List;

import mef.dals.ITaskDAL;
import mef.entities.TaskEO;

public class MockTaskDAL implements ITaskDAL
{
	ArrayList<TaskEO> _L = new ArrayList<TaskEO>();
	public boolean _dbDown;
	
	public void save(TaskEO t)
	{
		if (_dbDown) return;
		
		TaskEO existing = findById(t.id);
		if (existing != null)
		{
			_L.remove(existing);
		}
		_L.add(t);
	}
	
	public TaskEO findById(long id)
	{
		if (_dbDown) return null;
		
		for(int i = 0; i < _L.size(); i++)
		{
			TaskEO t = _L.get(i);
			if (t.id == id)
			{
				return t;
			}
		}
		return null;
	}
	
	public List<TaskEO> findAll()
	{
		if (_dbDown) return null;
		return _L;
	}
	public int size()
	{
		return _L.size();
	}

	public void delete(long id) 
	{
		TaskEO existing = findById(id);
		if (existing != null)
		{
			_L.remove(existing);
		}
	}
}
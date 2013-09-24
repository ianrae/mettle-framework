package mef.dals;

import java.util.ArrayList;
import java.util.List;

import mef.dals.ITaskDAL;
import mef.entities.Task;

public class MockTaskDAL implements ITaskDAL
{
	ArrayList<Task> _L = new ArrayList<Task>();
	public boolean _dbDown;
	
	public void save(Task t)
	{
		if (_dbDown) return;
		
		Task existing = findById(t.id);
		if (existing != null)
		{
			_L.remove(existing);
		}
		_L.add(t);
	}
	
	public Task findById(long id)
	{
		if (_dbDown) return null;
		
		for(int i = 0; i < _L.size(); i++)
		{
			Task t = _L.get(i);
			if (t.id == id)
			{
				return t;
			}
		}
		return null;
	}
	
	public List<Task> all()
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
		Task existing = findById(id);
		if (existing != null)
		{
			_L.remove(existing);
		}
	}
}
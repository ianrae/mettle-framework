package mef;

import mef.entities.Task;

import org.mef.framework.binder.IFormBinder;

public class MockFormBinder implements IFormBinder
{
	private Task entity;
	
	public MockFormBinder(Task entity)
	{
		this.entity = entity;
	}
	public boolean bind()
	{
		return true;
	}

	@Override
	public Task getObject() 
	{
		return entity;
	}
}
package mef;

import org.mef.framework.entities.*;

import org.mef.framework.binder.IFormBinder;

public class MockFormBinder implements IFormBinder
{
	private Entity entity;
	
	public MockFormBinder(Entity entity)
	{
		this.entity = entity;
	}
	public boolean bind()
	{
		return true;
	}

	@Override
	public Entity getObject() 
	{
		return entity;
	}
	
	@Override
	public Object getValidationErrors() 
	{
		return null;
	}
}
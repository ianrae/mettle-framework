package mef.mocks;

import mef.entities.User;

import org.mef.framework.entities.*;

import org.mef.framework.binder.IFormBinder;

public class MockFormBinder implements IFormBinder
{
	private Entity entity;
	public boolean isValid = true;
	
	public MockFormBinder(Entity entity)
	{
		this.entity = entity;
	}
	public boolean bind()
	{
		String s = entity.validate();
		if (s != null)
		{
			return false; //failed validation!
		}
		
		return isValid;
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
	@Override
	public Object getRawObject() 
	{
		return entity;
	}
}
package org.mef.framework.test.helpers;

import org.mef.framework.entities.*;
import org.mef.twixt.binder.IFormBinder;

import play.data.Form;


public class MockFormBinder implements IFormBinder<Entity>
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
	public Entity get()
	{
		return entity;
	}
	
	@Override
	public Object getValidationErrors() 
	{
		return null;
	}
	@Override
	public Form<Entity> getForm() 
	{
		return null;
	}
	@Override
	public Form<Entity> fillForm(Entity input) 
	{
		return null;
	}
}
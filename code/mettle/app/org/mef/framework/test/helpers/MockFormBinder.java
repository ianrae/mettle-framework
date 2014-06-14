package org.mef.framework.test.helpers;

import org.mef.framework.entities.*;
import org.mef.framework.binder.IFormBinder;

import play.db.ebean.Model;

public class MockFormBinder implements IFormBinder<Model,Entity>
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
	public Model getInputModel()
	{
		return null;
	}
	
	@Override
	public Object getValidationErrors() 
	{
		return null;
	}
	@Override
	public Entity getEntity() 
	{
		return entity;
	}

	@Override
	public Entity convert(Model model) 
	{
		return null;
	}
}
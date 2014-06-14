package org.mef.framework.binder;

import org.apache.commons.lang.NotImplementedException;
import org.mef.framework.binder.IFormBinder;

import play.db.ebean.Model;

public class GenericMockFormBinder<M,E> implements IFormBinder<M,E>
{
	private M obj;
	public boolean isValid = true;
	
	public GenericMockFormBinder(M obj)
	{
		this.obj = obj;
	}
	public boolean bind()
	{
		return isValid;
	}

	@Override
	public M getInputModel()
	{
		return obj;
	}
	
	@Override
	public Object getValidationErrors() 
	{
		return null;
	}
	@Override
	public E getEntity()
	{
		M model = getInputModel();
		if (model == null)
		{
			return null;
		}
		E entity = convert(model);
		return entity;
	}
	@Override
	public E convert(M model) 
	{
		throw new NotImplementedException("convert !!");
//		return null; //!!must implement
	}
}
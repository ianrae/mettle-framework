package org.mef.framework.binder;

import org.mef.framework.binder.IFormBinder;

public class GenericMockFormBinder<T> implements IFormBinder
{
	private T obj;
	public boolean isValid = true;
	
	public GenericMockFormBinder(T obj)
	{
		this.obj = obj;
	}
	public boolean bind()
	{
		return isValid;
	}

	@Override
	public T getObject() 
	{
		return obj;
	}
	
	@Override
	public Object getValidationErrors() 
	{
		return null;
	}
	@Override
	public Object getRawObject() 
	{
		return obj;
	}
}
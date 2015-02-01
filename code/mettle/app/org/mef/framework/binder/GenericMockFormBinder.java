package org.mef.framework.binder;

import play.data.Form;

public class GenericMockFormBinder<T> implements IFormBinder<T>
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
	public T get()
	{
		return obj;
	}
	
	@Override
	public Object getValidationErrors() 
	{
		return null;
	}
	@Override
	public Form<T> getForm() 
	{
		return null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public Form<T> fillForm(T input) 
	{
		Form<T> form = (Form<T>) Form.form(input.getClass());
		form = form.fill(input);
		return form;
	}
}
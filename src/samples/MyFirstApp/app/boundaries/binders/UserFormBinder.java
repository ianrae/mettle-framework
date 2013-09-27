package boundaries.binders;

import java.util.Map;

import mef.entities.User;
import models.UserModel;

import org.mef.framework.binder.IFormBinder;

import boundaries.dals.UserDAL;

import play.Logger;
import play.data.Form;

public class UserFormBinder implements IFormBinder
{
	private Form<UserModel> validationForm;
	private Form<UserModel> filledForm;

	public UserFormBinder(Form<UserModel> validationForm)
	{
		this.validationForm = validationForm;
	}
	@Override
	public boolean bind() 
	{
		this.filledForm = validationForm.bindFromRequest();
		
		User entity = getObject();
		if (entity != null)
		{
			String s = entity.validate();
			if (s != null)
			{
				filledForm.reject("entity", s); //add the error
			}
		}
		
		return ! filledForm.hasErrors();
	}

	@Override
	public User getObject() 
	{
		if (filledForm.hasErrors())
		{
			return null;
		}
		
		UserModel model = filledForm.get();
		if (model == null)
		{
			return null;
		}
		
		User entity = UserDAL.createEntityFromModel(model);
		return entity;
	}
	
	@Override
	public Object getValidationErrors() 
	{
		return filledForm.errors();
	}
	
	public UserModel getRawObj()
	{
		return filledForm.get();
	}
	public Form<UserModel> getRawForm()
	{
		return filledForm;
	}

}

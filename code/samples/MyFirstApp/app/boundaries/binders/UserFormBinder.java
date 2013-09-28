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
		Logger.info("b0");
		User entity = getObject();
		if (entity != null)
		{
			Logger.info("b1");
			String s = entity.validate();
			if (s != null)
			{
				Logger.info("b2");
				filledForm.reject("entity", s); //add the error
			}
		}
		
		return ! filledForm.hasErrors();
	}

	@Override
	public User getObject() 
	{
		Logger.info("g0");
		if (filledForm.hasErrors())
		{
			Logger.info("g1");
			return null;
		}
		
		UserModel model = filledForm.get();
		if (model == null)
		{
			Logger.info("g2");
			return null;
		}
		
		Logger.info("g3");
		User entity = UserDAL.createEntityFromModel(model);
		return entity;
	}
	
	@Override
	public Object getValidationErrors() 
	{
		return filledForm.errors();
	}
	
	public Form<UserModel> getRawForm()
	{
		return filledForm;
	}
	@Override
	public UserModel getRawObject() 
	{
		return filledForm.get();
	}

}

package boundaries;

import java.util.Map;

import mef.entities.User;
import models.UserModel;

import org.mef.framework.binder.IFormBinder;

import play.Logger;
import play.data.Form;

public class UserFormBinder implements IFormBinder
{
	private Form<UserModel> UserForm;
	private Form<UserModel> filledForm;

	public UserFormBinder(Form<UserModel> UserForm)
	{
		this.UserForm = UserForm;
	}
	@Override
	public boolean bind() 
	{
		this.filledForm = UserForm.bindFromRequest();
		return ! filledForm.hasErrors();
	}

	@Override
	public User getObject() 
	{
		UserModel model = filledForm.get();
		if (model == null)
		{
			return null;
		}
		
		User entity = Boundary.convertFromUserModel(model);
		model.entity = entity;
		return entity;
	}
	
	@Override
	public Object getValidationErrors() 
	{
		return filledForm.errors();
	}

}

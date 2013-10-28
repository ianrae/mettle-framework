//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package boundaries.binders;

import java.util.Map;

import mef.entities.AuthUser;
import models.AuthUserModel;

import org.mef.framework.binder.IFormBinder;

import boundaries.daos.AuthUserDAO;

import play.Logger;
import play.data.Form;

public class UserFormBinder implements IFormBinder
{
	private Form<AuthUserModel> validationForm;
	private Form<AuthUserModel> filledForm;

	public UserFormBinder(Form<AuthUserModel> validationForm)
	{
		this.validationForm = validationForm;
	}
	@Override
	public boolean bind() 
	{
		this.filledForm = validationForm.bindFromRequest();
		Logger.info("b0");
		AuthUser entity = getObject();
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
	public AuthUser getObject() 
	{
		Logger.info("g0");
		if (filledForm.hasErrors())
		{
			Logger.info("g1");
			return null;
		}

		AuthUserModel model = filledForm.get();
		if (model == null)
		{
			Logger.info("g2");
			return null;
		}

		Logger.info("g3");
		AuthUser entity = AuthUserDAO.createEntityFromModel(model);
		return entity;
	}

	@Override
	public Object getValidationErrors() 
	{
		return filledForm.errors();
	}

	public Form<AuthUserModel> getRawForm()
	{
		return filledForm;
	}
	@Override
	public AuthUserModel getRawObject() 
	{
		return filledForm.get();
	}





}

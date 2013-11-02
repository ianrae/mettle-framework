//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package boundaries.binders;

import java.util.Map;

import mef.entities.AuthSubject;
import models.AuthSubjectModel;

import org.mef.framework.binder.IFormBinder;

import boundaries.daos.AuthSubjectDAO;

import play.Logger;
import play.data.Form;

public class UserFormBinder implements IFormBinder
{
	private Form<AuthSubjectModel> validationForm;
	private Form<AuthSubjectModel> filledForm;

	public UserFormBinder(Form<AuthSubjectModel> validationForm)
	{
		this.validationForm = validationForm;
	}
	@Override
	public boolean bind() 
	{
		this.filledForm = validationForm.bindFromRequest();
		Logger.info("b0");
		AuthSubject entity = getObject();
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
	public AuthSubject getObject() 
	{
		Logger.info("g0");
		if (filledForm.hasErrors())
		{
			Logger.info("g1");
			return null;
		}

		AuthSubjectModel model = filledForm.get();
		if (model == null)
		{
			Logger.info("g2");
			return null;
		}

		Logger.info("g3");
		AuthSubject entity = AuthSubjectDAO.createEntityFromModel(model);
		return entity;
	}

	@Override
	public Object getValidationErrors() 
	{
		return filledForm.errors();
	}

	public Form<AuthSubjectModel> getRawForm()
	{
		return filledForm;
	}
	@Override
	public AuthSubjectModel getRawObject() 
	{
		return filledForm.get();
	}





}

//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package boundaries.binders;

import java.util.Map;

import mef.entities.Company;
import models.CompanyModel;

import org.mef.framework.binder.IFormBinder;

import boundaries.daos.CompanyDAO;

import play.Logger;
import play.data.Form;

public class CompanyFormBinder implements IFormBinder
{
	private Form<CompanyModel> validationForm;
	private Form<CompanyModel> filledForm;

	public CompanyFormBinder(Form<CompanyModel> validationForm)
	{
		this.validationForm = validationForm;
	}
	@Override
	public boolean bind() 
	{
		this.filledForm = validationForm.bindFromRequest();
		Logger.info("b0");
		Company entity = getObject();
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
	public Company getObject() 
	{
		Logger.info("g0");
		if (filledForm.hasErrors())
		{
			Logger.info("g1");
			return null;
		}

		CompanyModel model = filledForm.get();
		if (model == null)
		{
			Logger.info("g2");
			return null;
		}

		Logger.info("g3");
		Company entity = CompanyDAO.createEntityFromModel(model);
		return entity;
	}

	@Override
	public Object getValidationErrors() 
	{
		return filledForm.errors();
	}

	public Form<CompanyModel> getRawForm()
	{
		return filledForm;
	}
	@Override
	public CompanyModel getRawObject() 
	{
		return filledForm.get();
	}





}

//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package boundaries.binders;

import java.util.Map;

import mef.entities.Computer;
import models.ComputerModel;

import org.mef.framework.binder.IFormBinder;

import boundaries.daos.ComputerDAO;

import play.Logger;
import play.data.Form;

public class ComputerFormBinder implements IFormBinder
{
	private Form<ComputerModel> validationForm;
	private Form<ComputerModel> filledForm;

	public ComputerFormBinder(Form<ComputerModel> validationForm)
	{
		this.validationForm = validationForm;
	}
	@Override
	public boolean bind() 
	{
		this.filledForm = validationForm.bindFromRequest();
		Logger.info("b0");
		Computer entity = getObject();
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
	public Computer getObject() 
	{
		Logger.info("g0");
		if (filledForm.hasErrors())
		{
			Logger.info("g1");
			return null;
		}

		ComputerModel model = filledForm.get();
		if (model == null)
		{
			Logger.info("g2");
			return null;
		}

		Logger.info("g3");
		Computer entity = ComputerDAO.createEntityFromModel(model);
		return entity;
	}

	@Override
	public Object getValidationErrors() 
	{
		return filledForm.errors();
	}

	public Form<ComputerModel> getRawForm()
	{
		return filledForm;
	}
	@Override
	public ComputerModel getRawObject() 
	{
		return filledForm.get();
	}











}

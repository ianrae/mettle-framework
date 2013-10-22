//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package boundaries.binders;

import java.util.Map;

import mef.entities.Role;
import models.RoleModel;

import org.mef.framework.binder.IFormBinder;

import boundaries.daos.RoleDAO;

import play.Logger;
import play.data.Form;

public class RoleFormBinder implements IFormBinder
{
	private Form<RoleModel> validationForm;
	private Form<RoleModel> filledForm;

	public RoleFormBinder(Form<RoleModel> validationForm)
	{
		this.validationForm = validationForm;
	}
	@Override
	public boolean bind() 
	{
		this.filledForm = validationForm.bindFromRequest();
		Logger.info("b0");
		Role entity = getObject();
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
	public Role getObject() 
	{
		Logger.info("g0");
		if (filledForm.hasErrors())
		{
			Logger.info("g1");
			return null;
		}

		RoleModel model = filledForm.get();
		if (model == null)
		{
			Logger.info("g2");
			return null;
		}

		Logger.info("g3");
		Role entity = RoleDAO.createEntityFromModel(model);
		return entity;
	}

	@Override
	public Object getValidationErrors() 
	{
		return filledForm.errors();
	}

	public Form<RoleModel> getRawForm()
	{
		return filledForm;
	}
	@Override
	public RoleModel getRawObject() 
	{
		return filledForm.get();
	}





}

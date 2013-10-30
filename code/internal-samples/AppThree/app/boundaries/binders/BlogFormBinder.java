//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package boundaries.binders;

import java.util.Map;

import mef.entities.Blog;
import models.BlogModel;

import org.mef.framework.binder.IFormBinder;

import boundaries.daos.BlogDAO;

import play.Logger;
import play.data.Form;

public class BlogFormBinder implements IFormBinder
{
	private Form<BlogModel> validationForm;
	private Form<BlogModel> filledForm;

	public BlogFormBinder(Form<BlogModel> validationForm)
	{
		this.validationForm = validationForm;
	}
	@Override
	public boolean bind() 
	{
		this.filledForm = validationForm.bindFromRequest();
		Logger.info("b0");
		Blog entity = getObject();
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
	public Blog getObject() 
	{
		Logger.info("g0");
		if (filledForm.hasErrors())
		{
			Logger.info("g1");
			return null;
		}

		BlogModel model = filledForm.get();
		if (model == null)
		{
			Logger.info("g2");
			return null;
		}

		Logger.info("g3");
		Blog entity = BlogDAO.createEntityFromModel(model);
		return entity;
	}

	@Override
	public Object getValidationErrors() 
	{
		return filledForm.errors();
	}

	public Form<BlogModel> getRawForm()
	{
		return filledForm;
	}
	@Override
	public BlogModel getRawObject() 
	{
		return filledForm.get();
	}





}

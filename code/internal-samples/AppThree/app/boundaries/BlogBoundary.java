//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package boundaries;

import mef.presenters.BlogPresenter;
import mef.presenters.replies.BlogReply;
import models.BlogModel;
import mef.core.Initializer;
import org.mef.framework.boundaries.BoundaryBase;
import org.mef.framework.commands.Command;
import org.mef.framework.sfx.SfxContext;

import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import boundaries.binders.BlogFormBinder;
import boundaries.daos.BlogDAO;
public class BlogBoundary extends BoundaryBase
{
	public static BlogBoundary create() 
	{
		Boundary.init();
		return new BlogBoundary(Initializer.theCtx);
	}

	public BlogFormBinder binder;

	public BlogBoundary(SfxContext ctx)
	{
		super(ctx);
	}

	public BlogReply addFormAndProcess(Command cmd)
	{
		Form<BlogModel> validationForm =  Form.form(BlogModel.class);
		binder = new BlogFormBinder(validationForm);
		cmd.setFormBinder(binder);
		return process(cmd);
	}

	public Form<BlogModel> makeForm(BlogReply reply)
	{
		if (binder != null)
		{
			Logger.info("mf-binder");
			return binder.getRawForm();
		}
		Logger.info("mf-make");
		Form<BlogModel> frm = Form.form(BlogModel.class);
		BlogModel model = BlogDAO.createModelFromEntity(reply._entity);
		frm = frm.fill(model);
		return frm;
	}

	@Override
	public BlogReply process(Command cmd)
	{
		begin(cmd);
		BlogPresenter presenter = new BlogPresenter(_ctx);

		BlogReply reply = (BlogReply) presenter.process(cmd);

		String flashKey = reply.getFlashKey();
		String flashMsg = reply.getFlash();
		if (flashKey != null)
		{
			Controller.flash(flashKey, flashMsg);
		}
		return reply;
	}

	public String getAllValidationErrors()
	{
		return dogetAllValidationErrors(binder);
	}





}

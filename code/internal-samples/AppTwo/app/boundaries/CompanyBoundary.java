//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package boundaries;

import mef.core.Initializer;
import mef.presenters.CompanyPresenter;
import mef.presenters.replies.CompanyReply;
import models.CompanyModel;

import org.mef.framework.boundaries.BoundaryBase;
import org.mef.framework.commands.Command;
import org.mef.framework.sfx.SfxContext;

import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import boundaries.binders.CompanyFormBinder;
import boundaries.daos.CompanyDAO;
public class CompanyBoundary extends BoundaryBase
{
	public static CompanyBoundary create() 
	{
		Boundary.init();
		return new CompanyBoundary(Initializer.theCtx);
	}

	public CompanyFormBinder binder;

	public CompanyBoundary(SfxContext ctx)
	{
		super(ctx);
	}

	public CompanyReply addFormAndProcess(Command cmd)
	{
		Form<CompanyModel> validationForm =  Form.form(CompanyModel.class);
		binder = new CompanyFormBinder(validationForm);
		cmd.setFormBinder(binder);
		return process(cmd);
	}

	public Form<CompanyModel> makeForm(CompanyReply reply)
	{
		if (binder != null)
		{
			Logger.info("mf-binder");
			return binder.getRawForm();
		}
		Logger.info("mf-make");
		Form<CompanyModel> frm = Form.form(CompanyModel.class);
		CompanyModel model = CompanyDAO.createModelFromEntity(reply._entity);
		frm = frm.fill(model);
		return frm;
	}

	@Override
	public CompanyReply process(Command cmd)
	{
		begin(cmd);
		CompanyPresenter presenter = new CompanyPresenter(_ctx);

		CompanyReply reply = (CompanyReply) presenter.process(cmd);

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

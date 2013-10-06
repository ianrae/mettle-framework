//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package boundaries;

import mef.core.Initializer;
import mef.presenters.ComputerPresenter;
import mef.presenters.replies.ComputerReply;
import models.ComputerModel;

import org.mef.framework.boundaries.BoundaryBase;
import org.mef.framework.commands.Command;
import org.mef.framework.sfx.SfxContext;

import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import boundaries.binders.ComputerFormBinder;
import boundaries.daos.ComputerDAO;
public class ComputerBoundary extends BoundaryBase
{
	public static ComputerBoundary create() 
	{
		Boundary.init();
		return new ComputerBoundary(Initializer.theCtx);
	}

	public ComputerFormBinder binder;

	public ComputerBoundary(SfxContext ctx)
	{
		super(ctx);
	}

	public ComputerReply addFormAndProcess(Command cmd)
	{
		Form<ComputerModel> validationForm =  Form.form(ComputerModel.class);
		binder = new ComputerFormBinder(validationForm);
		cmd.setFormBinder(binder);
		return process(cmd);
	}

	public Form<ComputerModel> makeForm(ComputerReply reply)
	{
		if (binder != null)
		{
			Logger.info("mf-binder");
			return binder.getRawForm();
		}
		Logger.info("mf-make");
		Form<ComputerModel> frm = Form.form(ComputerModel.class);
		ComputerModel model = ComputerDAO.createModelFromEntity(reply._entity);
		frm = frm.fill(model);
		return frm;
	}

	@Override
	public ComputerReply process(Command cmd)
	{
		begin(cmd);
		ComputerPresenter presenter = new ComputerPresenter(_ctx);

		ComputerReply reply = (ComputerReply) presenter.process(cmd);

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

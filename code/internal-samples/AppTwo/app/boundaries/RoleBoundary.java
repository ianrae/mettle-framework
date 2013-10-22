//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package boundaries;

import mef.presenters.RolePresenter;
import mef.presenters.replies.RoleReply;
import models.RoleModel;
import mef.core.Initializer;
import org.mef.framework.boundaries.BoundaryBase;
import org.mef.framework.commands.Command;
import org.mef.framework.sfx.SfxContext;

import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import boundaries.binders.RoleFormBinder;
import boundaries.daos.RoleDAO;
public class RoleBoundary extends BoundaryBase
{
	public static RoleBoundary create() 
	{
		Boundary.init();
		return new RoleBoundary(Initializer.theCtx);
	}

	public RoleFormBinder binder;

	public RoleBoundary(SfxContext ctx)
	{
		super(ctx);
	}

	public RoleReply addFormAndProcess(Command cmd)
	{
		Form<RoleModel> validationForm =  Form.form(RoleModel.class);
		binder = new RoleFormBinder(validationForm);
		cmd.setFormBinder(binder);
		return process(cmd);
	}

	public Form<RoleModel> makeForm(RoleReply reply)
	{
		if (binder != null)
		{
			Logger.info("mf-binder");
			return binder.getRawForm();
		}
		Logger.info("mf-make");
		Form<RoleModel> frm = Form.form(RoleModel.class);
		RoleModel model = RoleDAO.createModelFromEntity(reply._entity);
		frm = frm.fill(model);
		return frm;
	}

	@Override
	public RoleReply process(Command cmd)
	{
		begin(cmd);
		RolePresenter presenter = new RolePresenter(_ctx);

		RoleReply reply = (RoleReply) presenter.process(cmd);

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

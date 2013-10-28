//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package boundaries;

import mef.presenters.UserPresenter;
import mef.presenters.replies.UserReply;
import models.AuthUserModel;
import mef.core.Initializer;
import org.mef.framework.boundaries.BoundaryBase;
import org.mef.framework.commands.Command;
import org.mef.framework.sfx.SfxContext;

import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import boundaries.binders.UserFormBinder;
import boundaries.daos.AuthUserDAO;
public class UserBoundary extends BoundaryBase
{
	public static UserBoundary create() 
	{
		Boundary.init();
		return new UserBoundary(Initializer.theCtx);
	}

	public UserFormBinder binder;

	public UserBoundary(SfxContext ctx)
	{
		super(ctx);
	}

	public UserReply addFormAndProcess(Command cmd)
	{
		Form<AuthUserModel> validationForm =  Form.form(AuthUserModel.class);
		binder = new UserFormBinder(validationForm);
		cmd.setFormBinder(binder);
		return process(cmd);
	}

	public Form<AuthUserModel> makeForm(UserReply reply)
	{
		if (binder != null)
		{
			Logger.info("mf-binder");
			return binder.getRawForm();
		}
		Logger.info("mf-make");
		Form<AuthUserModel> frm = Form.form(AuthUserModel.class);
		AuthUserModel model = AuthUserDAO.createModelFromEntity(reply._entity);
		frm = frm.fill(model);
		return frm;
	}

	@Override
	public UserReply process(Command cmd)
	{
		begin(cmd);
		UserPresenter presenter = new UserPresenter(_ctx);

		UserReply reply = (UserReply) presenter.process(cmd);

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

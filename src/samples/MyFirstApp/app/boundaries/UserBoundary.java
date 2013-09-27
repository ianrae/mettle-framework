package boundaries;

import java.util.List;
import java.util.Map;

import mef.entities.User;
import mef.presenters.UserPresenter;
import mef.presenters.replies.UserReply;
import models.UserModel;

import org.mef.framework.boundaries.BoundaryBase;
import org.mef.framework.commands.Command;
import org.mef.framework.sfx.SfxContext;

import play.Logger;
import play.data.Form;
import play.data.validation.ValidationError;
import boundaries.binders.UserFormBinder;
import boundaries.dals.UserDAL;
import controllers.routes;

public class UserBoundary extends BoundaryBase
{
	public UserFormBinder binder;
	
	public UserBoundary(SfxContext ctx)
	{
		super(ctx);
	}
	
	public UserReply addFormAndProcess(Command cmd)
	{
		Form<UserModel> validationForm =  Form.form(UserModel.class);
		binder = new UserFormBinder(validationForm);
		cmd.setFormBinder(binder);
		return process(cmd);
	}
	
	public Form<UserModel> makeForm(UserReply reply)
	{
		if (binder != null)
		{
			Logger.info("mf-binder");
			return binder.getRawForm();
		}
		Logger.info("mf-make");
		Form<UserModel> frm = Form.form(UserModel.class);
		UserModel model = UserDAL.createModelFromEntity(reply._entity);
		frm = frm.fill(model);
		return frm;
	}
	
	@Override
	public UserReply process(Command cmd)
	{
		begin(cmd);
		UserPresenter presenter = new UserPresenter(_ctx);
		
		UserReply reply = (UserReply) presenter.process(cmd);
		return reply;
	}
	
	public String getAllValidationErrors()
	{
		return dogetAllValidationErrors(binder);
	}
	
}
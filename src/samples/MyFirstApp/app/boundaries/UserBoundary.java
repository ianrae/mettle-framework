package boundaries;

import mef.entities.User;
import mef.presenters.UserPresenter;
import mef.presenters.UserReply;
import models.UserModel;

import org.mef.framework.binder.IFormBinder;
import org.mef.framework.boundaries.BoundaryBase;
import org.mef.framework.commands.Command;
import org.mef.framework.entities.Entity;
import org.mef.framework.replies.Reply;
import org.mef.framework.sfx.SfxContext;

import controllers.routes;
import play.api.mvc.Call;
import play.data.Form;

public class UserBoundary extends BoundaryBase
{
	public UserBoundary(SfxContext ctx)
	{
		super(ctx);
	}
	
	@Override
	protected IFormBinder createFormBinder(Command cmd, Entity entity)
	{
		//UserModel model = Boundary.convertToUserModel(User);
		Form<UserModel> UserForm =  Form.form(UserModel.class);
		UserFormBinder binder = new UserFormBinder(UserForm);
		return binder;
	}
	
	public UserReply addFormAndProcess(Command cmd)
	{
		Form<UserModel> UserForm =  Form.form(UserModel.class);
		UserFormBinder binder = new UserFormBinder(UserForm);
		cmd.setFormBinder(binder);
		return process(cmd);
	}
	
	public Form<User> makeForm(UserReply reply)
	{
		Form<User> frm =  Form.form(User.class);
		frm.fill(reply._entity);
		return frm;
	}
	
	//hmmm!!
	@Override
	public UserReply process(Command cmd)
	{
		begin(cmd);
		UserPresenter presenter = new UserPresenter(_ctx);
		
		Reply reply = presenter.process(cmd);
		if (reply.failed())
		{
			result = play.mvc.Results.redirect(routes.Owner.logout());
			return (UserReply) reply;
		}
		return (UserReply) reply;
	}
	
}
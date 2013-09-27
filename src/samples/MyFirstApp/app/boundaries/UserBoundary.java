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

import play.data.Form;
import play.data.validation.ValidationError;
import boundaries.binders.UserFormBinder;
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
	
	public Form<User> makeForm(UserReply reply)
	{
		Form<User> frm =  Form.form(User.class);
		frm = frm.fill(reply._entity);
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
		if (binder == null)
		{
			return "";
			
		}
		String s = "";
		Map<String, List<ValidationError>> map = (Map<String, List<ValidationError>>) binder.getValidationErrors();
		for(String key : map.keySet())
		{
			List<ValidationError> val = map.get(key);
			for(ValidationError err : val)
			{
				s += ", " + String.format("%s: %s", key, err.message());
			}
		}		
		return s;
	}
	
}
package controllers;

import mef.presenters.replies.UserReply;
import models.UserModel;

import org.mef.framework.commands.IndexCommand;
import org.mef.framework.replies.Reply;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import boundaries.UserBoundary;

public class UserC extends Controller 
{
//	static Form<UserModel> UserForm = Form.form(UserModel.class);  
	
	public static Result index() 
    {
		UserBoundary boundary = UserBoundary.create();
		UserReply reply = boundary.process(new IndexCommand());
		return renderOrForward(boundary, reply);
	}
	
    private static Result renderOrForward(UserBoundary boundary, UserReply reply)
    {
		if (reply.failed())
		{
			return redirect(routes.ErrorC.logout());
		}
		
		Form<UserModel> frm = null;
		switch(reply.getDestination())
		{
		case Reply.VIEW_INDEX:
			return ok(views.html.user.render(reply._allL));    	


		default:
			return play.mvc.Results.redirect(routes.ErrorC.logout());	
    	}
	}
}
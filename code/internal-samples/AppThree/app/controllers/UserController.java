//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package controllers;

import mef.presenters.replies.UserReply;
import org.mef.framework.commands.IndexCommand;
import org.mef.framework.replies.Reply;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import boundaries.UserBoundary;

public class UserController extends Controller
{
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
			return redirect(routes.ErrorController.logout());
		}

		switch(reply.getDestination())
		{
		case Reply.VIEW_INDEX:
			return ok(views.html.User.index.render(reply._allL));    	

		default:
			return play.mvc.Results.redirect(routes.ErrorController.logout());	
    	}
	}




}

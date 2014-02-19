
package controllers;

import mef.presenters.replies.UserReply;

import org.mef.framework.commands.CreateCommand;
import org.mef.framework.commands.IndexCommand;
import org.mef.framework.commands.NewCommand;
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
	
	public static Result newUser()
	{
		UserBoundary boundary = UserBoundary.create();
		UserReply reply = boundary.process(new NewCommand());
		return renderOrForward(boundary, reply);
	}
	public static Result create()
	{
		UserBoundary boundary = UserBoundary.create();
		UserReply reply = boundary.addFormAndProcess(new CreateCommand());
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
		case Reply.VIEW_NEW:
			return ok(views.html.User.newuser.render(boundary.makeForm(reply)));    	


		case Reply.FOWARD_NOT_AUTHENTICATED:
			return play.mvc.Results.redirect(routes.ErrorController.showError("Not logged in!"));	

		case Reply.FOWARD_NOT_AUTHORIZED:
			return play.mvc.Results.redirect(routes.ErrorController.showError("Not authorized to do that!"));	
		case Reply.FORWARD_INDEX:
			return redirect(routes.UserController.index());


		default:
			System.out.println(String.format("UNKOWN DEST: %d", reply.getDestination()));
			return play.mvc.Results.redirect(routes.ErrorController.logout());	
    	}
	}





}

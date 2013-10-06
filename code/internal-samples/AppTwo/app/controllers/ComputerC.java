package controllers;


import mef.presenters.replies.ComputerReply;

import org.mef.framework.commands.IndexCommand;
import org.mef.framework.replies.Reply;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import boundaries.ComputerBoundary;

public class ComputerC extends Controller
{
	public static Result index() 
    {
		ComputerBoundary boundary = ComputerBoundary.create();
		ComputerReply reply = boundary.process(new IndexCommand());
		return renderOrForward(boundary, reply);
	}
	
    private static Result renderOrForward(ComputerBoundary boundary, ComputerReply reply)
    {
		if (reply.failed())
		{
			return redirect(routes.ErrorC.logout());
		}
		
//		Form<UserModel> frm = null;
		switch(reply.getDestination())
		{
		case Reply.VIEW_INDEX:
			return ok(views.html.computer.render(reply._allL));    	


		default:
			return play.mvc.Results.redirect(routes.ErrorC.logout());	
    	}
	}
}

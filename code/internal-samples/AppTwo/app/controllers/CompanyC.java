package controllers;


import mef.presenters.replies.CompanyReply;

import org.mef.framework.commands.IndexCommand;
import org.mef.framework.replies.Reply;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import boundaries.CompanyBoundary;

public class CompanyC extends Controller
{
	public static Result index() 
    {
		CompanyBoundary boundary = CompanyBoundary.create();
		CompanyReply reply = boundary.process(new IndexCommand());
		return renderOrForward(boundary, reply);
	}
	
    private static Result renderOrForward(CompanyBoundary boundary, CompanyReply reply)
    {
		if (reply.failed())
		{
			return redirect(routes.ErrorC.logout());
		}
		
//		Form<UserModel> frm = null;
		switch(reply.getDestination())
		{
		case Reply.VIEW_INDEX:
			return ok(views.html.company.render(reply._allL));    	


		default:
			return play.mvc.Results.redirect(routes.ErrorC.logout());	
    	}
	}
}

//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package controllers;

import mef.presenters.replies.BlogReply;
import org.mef.framework.commands.IndexCommand;
import org.mef.framework.replies.Reply;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import boundaries.BlogBoundary;

public class BlogController extends Controller
{
	public static Result index() 
    {
		BlogBoundary boundary = BlogBoundary.create();
		BlogReply reply = boundary.process(new IndexCommand());
		return renderOrForward(boundary, reply);
	}

    private static Result renderOrForward(BlogBoundary boundary, BlogReply reply)
    {
		if (reply.failed())
		{
			return redirect(routes.ErrorController.logout());
		}

		switch(reply.getDestination())
		{
		case Reply.VIEW_INDEX:
			return ok(views.html.Blog.index.render(reply._allL));    	

		default:
			return play.mvc.Results.redirect(routes.ErrorController.logout());	
    	}
	}





}

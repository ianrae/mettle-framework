//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package controllers;

import mef.presenters.commands.IndexBlogCommand;
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
		BlogReply reply = boundary.process(new IndexBlogCommand());
		return renderOrForward(boundary, reply);
	}

    private static Result renderOrForward(BlogBoundary boundary, BlogReply reply)
    {
		if (reply.failed())
		{
			return redirect(routes.ErrorController.showError("failed"));
		}

		switch(reply.getDestination())
		{
		case Reply.VIEW_INDEX:
			return ok(views.html.Blog.index.render(reply._allL));    	

		case Reply.FOWARD_NOT_AUTHENTICATED:
			return play.mvc.Results.redirect(routes.ErrorController.showError("Not logged in!"));	

		case Reply.FOWARD_NOT_AUTHORIZED:
			return play.mvc.Results.redirect(routes.ErrorController.showError("Not authorized to do that!"));	

		default:
			return play.mvc.Results.redirect(routes.ErrorController.logout());	
    	}
	}





}

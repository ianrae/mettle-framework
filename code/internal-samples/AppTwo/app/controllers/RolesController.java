package controllers;

import mef.presenters.replies.RoleReply;
import models.RoleModel;

import org.mef.framework.commands.IndexCommand;
import org.mef.framework.replies.Reply;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import boundaries.RoleBoundary;

public class RolesController extends Controller 
{
//	static Form<RoleModel> RoleForm = Form.form(RoleModel.class);  
	
	public static Result index() 
    {
		RoleBoundary boundary = RoleBoundary.create();
		RoleReply reply = boundary.process(new IndexCommand());
		return renderOrForward(boundary, reply);
	}
	
    private static Result renderOrForward(RoleBoundary boundary, RoleReply reply)
    {
		if (reply.failed())
		{
			return redirect(routes.ErrorC.logout());
		}
		
		Form<RoleModel> frm = null;
		switch(reply.getDestination())
		{
		case Reply.VIEW_INDEX:
			return ok(views.html.Role.index.render(reply._allL));    	


		default:
			return play.mvc.Results.redirect(routes.ErrorC.logout());	
    	}
	}
}
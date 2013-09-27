package controllers;

import java.util.List;
import java.util.Map;

import org.mef.framework.commands.CreateCommand;
import org.mef.framework.commands.DeleteCommand;
import org.mef.framework.commands.EditCommand;
import org.mef.framework.commands.IndexCommand;
import org.mef.framework.commands.NewCommand;
import org.mef.framework.commands.ShowCommand;
import org.mef.framework.commands.UpdateCommand;
import org.mef.framework.replies.Reply;

import boundaries.ApplicationBoundary;
import boundaries.Boundary;
import boundaries.UserBoundary;
import boundaries.binders.UserFormBinder;

import mef.presenters.replies.HomePageReply;
import mef.presenters.replies.UserReply;
import play.*;
import play.mvc.*;
import play.core.Router.Routes;
import play.data.*;
import play.data.validation.ValidationError;
import models.*;
import mef.entities.User;

import views.html.*;

public class UserC extends Controller 
{
	static Form<UserModel> UserForm = Form.form(UserModel.class);  
	
	public static Result index() 
    {
		UserBoundary boundary = Boundary.createUserBoundary();
		UserReply reply = boundary.process(new IndexCommand());
		return doRenderOrForward(boundary, reply);
	}
    
    public static Result newUser() 
    {
		UserBoundary boundary = Boundary.createUserBoundary();
		Logger.info("newuser..");
		UserReply reply = boundary.process(new NewCommand());
		System.out.println("xBOUND.. " + reply._entity.name);
		return doRenderOrForward(boundary, reply);
	}
    
    public static Result createUser() 
    {
		UserBoundary boundary = Boundary.createUserBoundary();
		Logger.info("createuser..");
		UserReply reply = boundary.addFormAndProcess(new CreateCommand());
		return doRenderOrForward(boundary, reply);
	}
    
    public static Result deleteUser(Long id) 
    {
		UserBoundary boundary = Boundary.createUserBoundary();
		UserReply reply = boundary.process(new DeleteCommand(id));
		return doRenderOrForward(boundary, reply);
	}
    
    public static Result edit(Long id) 
    {
		UserBoundary boundary = Boundary.createUserBoundary();
		UserReply reply = boundary.process(new EditCommand(id));
		return doRenderOrForward(boundary, reply);
	}
    
    public static Result updateUser(Long id) 
    {
		UserBoundary boundary = Boundary.createUserBoundary();
		UserReply reply = (UserReply) boundary.addFormAndProcess(new UpdateCommand(id));
		return doRenderOrForward(boundary, reply);
	}
    public static Result show(Long id) 
    {
		UserBoundary boundary = Boundary.createUserBoundary();
		UserReply reply = boundary.process(new ShowCommand(id));
		return doRenderOrForward(boundary, reply);
	}
    
    
    
    private static Result doRenderOrForward(UserBoundary boundary, UserReply reply)
    {
		if (reply.failed())
		{
			return redirect(routes.Owner.logout());
		}
		
		Form<UserModel> frm = null;
		String errMsg = "";
		String flashKey = reply.getFlashKey();
		String flashMsg = (reply.getFlash() != null) ? reply.getFlash() : "";
		switch(reply.getDestination())
		{
		case Reply.VIEW_INDEX:
			return ok(views.html.user.render(reply._allL, UserForm));    	

		case Reply.VIEW_NEW:
			frm = boundary.makeForm(reply); 
			errMsg = boundary.getAllValidationErrors();
			errMsg = "some " + errMsg;
			
			return ok(views.html.usernew.render(reply._allL, frm, errMsg, flashMsg));

		case Reply.VIEW_EDIT:
			frm = boundary.makeForm(reply); 
			errMsg = boundary.getAllValidationErrors();
			errMsg = "some " + errMsg;
			return ok(views.html.useredit.render(reply._allL, frm, reply._entity.id, errMsg, flashMsg));

		case Reply.VIEW_SHOW:
			return ok(views.html.usershow.render(reply._entity));

		case Reply.FORWARD_INDEX:
		case Reply.FORWARD_NOT_FOUND:
			return Results.redirect(routes.UserC.index());
			//		public static final int FOWARD_ERROR = 108:

		default:
			return play.mvc.Results.redirect(routes.Owner.logout());	
    	}
	}
}

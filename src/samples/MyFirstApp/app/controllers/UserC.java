package controllers;

import java.util.List;

import org.mef.framework.commands.CreateCommand;
import org.mef.framework.commands.DeleteCommand;
import org.mef.framework.commands.EditCommand;
import org.mef.framework.commands.IndexCommand;
import org.mef.framework.commands.NewCommand;
import org.mef.framework.commands.UpdateCommand;
import org.mef.framework.replies.Reply;

import boundaries.ApplicationBoundary;
import boundaries.Boundary;
import boundaries.UserBoundary;
import boundaries.UserFormBinder;

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
	static Form<User> UserForm = Form.form(User.class);  
	
    public static Result index() 
    {
		UserBoundary boundary = Boundary.createUserBoundary();
		UserReply reply = boundary.process(new IndexCommand());
		return doRenderOrForward(boundary, reply, UserForm);
	}
    
    public static Result newUser() 
    {
		UserBoundary boundary = Boundary.createUserBoundary();
		UserReply reply = boundary.process(new NewCommand());
		if (boundary.result != null)
		{
			return boundary.result;
		}
		
		System.out.println("BOUND.. " + reply._entity.name);
		Form<User> frm = Form.form(User.class);
		frm = frm.fill(reply._entity);
		return doRenderOrForward(boundary, reply, UserForm);
	}
    
    public static Result createUser() 
    {
		UserBoundary boundary = Boundary.createUserBoundary();
		CreateCommand cmd = new CreateCommand();
		
		UserReply reply = boundary.addFormAndProcess(new CreateCommand());
		return doRenderOrForward(boundary, reply, UserForm);
	}
    
    public static Result deleteUser(Long id) 
    {
		UserBoundary boundary = Boundary.createUserBoundary();
		UserReply reply = boundary.process(new DeleteCommand(id));
		return doRenderOrForward(boundary, reply, UserForm);
	}
    
    public static Result edit(Long id) 
    {
		UserBoundary boundary = Boundary.createUserBoundary();
		UserReply reply = boundary.process(new EditCommand(id));
		Form<User> frm = Form.form(User.class);
		frm = frm.fill(reply._entity);
		return doRenderOrForward(boundary, reply, frm);
	}
    
    public static Result updateUser(Long id) 
    {
		UserBoundary boundary = Boundary.createUserBoundary();
		UserReply reply = (UserReply) boundary.process(new UpdateCommand(id), new User());
		return doRenderOrForward(boundary, reply, UserForm);
	}
    
    
    private static Result doRenderOrForward(UserBoundary boundary, UserReply reply, Form<User> frm)
    {
		if (boundary.result != null)
		{
			return boundary.result;
		}
		
		switch(reply.getDestination())
		{
		case Reply.VIEW_INDEX:
			return ok(
					views.html.user.render(reply._allL, UserForm)
					);    	

			case Reply.VIEW_NEW:
				return ok(
						views.html.usernew.render(reply._allL, frm)
					  );
				
			case Reply.VIEW_EDIT:
				return ok(
						views.html.useredit.render(reply._allL, frm, reply._entity.id)
				  );
				
			case Reply.FORWARD_INDEX:
			case Reply.FORWARD_NOT_FOUND:
				return Results.redirect(routes.UserC.index());
//		public static final int FOWARD_ERROR = 108:
			
			default:
				return play.mvc.Results.redirect(routes.Owner.logout());	
		}
    }
}

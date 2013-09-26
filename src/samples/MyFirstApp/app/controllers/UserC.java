package controllers;

import java.util.List;

import org.mef.framework.commands.CreateCommand;
import org.mef.framework.commands.DeleteCommand;
import org.mef.framework.commands.EditCommand;
import org.mef.framework.commands.IndexCommand;
import org.mef.framework.commands.NewCommand;
import org.mef.framework.commands.UpdateCommand;

import boundaries.ApplicationBoundary;
import boundaries.Boundary;
import boundaries.UserBoundary;
import boundaries.UserFormBinder;

import mef.presenters.HomePageReply;
import mef.presenters.UserReply;
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
		if (boundary.result != null)
		{
			return boundary.result;
		}
		
		System.out.println("BOUND!");
	return ok(
		views.html.user.render(reply._allL, UserForm)
	  );
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
	return ok(
		views.html.usernew.render(reply._allL, frm)
	  );
	}
    
    public static Result createUser() 
    {
		UserBoundary boundary = Boundary.createUserBoundary();
		CreateCommand cmd = new CreateCommand();
		
		UserReply reply = boundary.addFormAndProcess(new CreateCommand());
		if (boundary.result != null)
		{
			return boundary.result;
		}
		
		System.out.println("BOUND!");
	return ok(
		views.html.user.render(reply._allL, UserForm)
	  );
	}
    
    public static Result deleteUser(Long id) 
    {
		UserBoundary boundary = Boundary.createUserBoundary();
		UserReply reply = boundary.process(new DeleteCommand(id));
		if (boundary.result != null)
		{
			return boundary.result;
		}
		
	return ok(
		views.html.user.render(reply._allL, UserForm)
	  );
	}
    
    public static Result edit(Long id) 
    {
		UserBoundary boundary = Boundary.createUserBoundary();
		UserReply reply = boundary.process(new EditCommand(id));
		if (boundary.result != null)
		{
			return boundary.result;
		}
		
		System.out.println("BOUND.. " + reply._entity.name);
		Form<User> frm = Form.form(User.class);
		frm = frm.fill(reply._entity);
	return ok(
			views.html.useredit.render(reply._allL, frm)
	  );
	}
    
    public static Result updateUser() 
    {
		UserBoundary boundary = Boundary.createUserBoundary();
		UserReply reply = (UserReply) boundary.process(new UpdateCommand(), new User());
		if (boundary.result != null)
		{
			return boundary.result;
		}
		
	return ok(
			views.html.user.render(reply._allL, UserForm)
	  );
	}
}

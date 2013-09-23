package controllers;

import java.util.List;

import org.mef.framework.commands.IndexCommand;

import boundaries.ApplicationBoundary;
import boundaries.Boundary;

import mef.presenters.HomePageReply;
import play.*;
import play.mvc.*;
import play.core.Router.Routes;
import play.data.*;
import play.data.validation.ValidationError;
import models.*;

import views.html.*;

public class Application extends Controller 
{
	static Form<Task> taskForm = Form.form(Task.class);  
	
    public static Result index() {
//        return ok(index.render("Your new xxapplication is ready."));
//        return ok("hello Play!");
		  return redirect(routes.Application.tasks());
    }
	
	public static Result tasks() 
	{
		ApplicationBoundary boundary = Boundary.createApplicationBoundary();
		HomePageReply reply = (HomePageReply) boundary.process(new IndexCommand(), null);
		
		List<Task> L = Boundary.convertToTask(reply._allL);
//		List<Task> L = Task.all();
		Logger.info("xxLOGGERBOUND " + L.size());
		
		System.out.println("BOUND!");
	return ok(
		views.html.index.render(L, taskForm)
	  );
	}
  
	public static Result newTask() {
	 Form<Task> filledForm = taskForm.bindFromRequest();
	  if(filledForm.hasErrors()) {
//		return badRequest(
//		  views.html.index.render(Task.all(), filledForm)
//		);
		  flash("flash_content", "hi !");
		  
		  java.util.Map<String, List<ValidationError>> map = filledForm.errors();		  
		  for(String key : map.keySet())
		  {
			  List<ValidationError> val = map.get(key);
			  for(ValidationError err : val)
			  {
				  System.out.println(String.format("%s: %s", key, err.message()));
			  }
		  }
		return ok(
				  views.html.index.render(Task.all(), filledForm)
				);
		
	  } else {
		Task.create(filledForm.get());
		System.out.println("#T = " + Task.all().size());
		return redirect(routes.Application.tasks());  
	  }
  }

	public static Result deleteTask(Long id) {
	 Task.delete(id);
	  return redirect(routes.Application.tasks());	
	}  

	public static Result logout() {
	return ok("ok...");
	}
}
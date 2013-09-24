package controllers;

import java.util.List;

import org.mef.framework.commands.CreateCommand;
import org.mef.framework.commands.DeleteCommand;
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
	static Form<TaskModel> taskForm = Form.form(TaskModel.class);  
	
    public static Result index() {
//        return ok(index.render("Your new xxapplication is ready."));
//        return ok("hello Play!");
		  return redirect(routes.Application.tasks());
    }
	
	public static Result tasks() 
	{
		ApplicationBoundary boundary = Boundary.createApplicationBoundary();
		HomePageReply reply = (HomePageReply) boundary.process(new IndexCommand());
		
		List<TaskModel> L = Boundary.convertToTaskModel(reply._allL);
//		List<Task> L = Task.all();
		Logger.info("xxLOGGERBOUND " + L.size());
		
		System.out.println("BOUND!");
	return ok(
		views.html.index.render(L, taskForm)
	  );
	}
  
	public static Result newTask() 
	{
		ApplicationBoundary boundary = Boundary.createApplicationBoundary();
		HomePageReply reply = (HomePageReply) boundary.process(new CreateCommand(), taskForm);
		
		if (reply.getForward() == null)
		{
		  flash("flash_content", "hi !");
		  
//		  java.util.Map<String, List<ValidationError>> map = filledForm.errors();		  
//		  for(String key : map.keySet())
//		  {
//			  List<ValidationError> val = map.get(key);
//			  for(ValidationError err : val)
//			  {
//				  System.out.println(String.format("%s: %s", key, err.message()));
//			  }
//		  }
		  return ok(
//				  views.html.index.render(TaskModel.all(), filledForm)
				  views.html.index.render(TaskModel.all(), taskForm) //!!fix later
				  );
		  
		} else {
//			TaskModel.create(filledForm.get());
			System.out.println("#T = " + TaskModel.all().size());
			return redirect(routes.Application.tasks());  
		}
  }

	public static Result deleteTask(Long id) 
	{
		//	 Task.delete(id);
		ApplicationBoundary boundary = Boundary.createApplicationBoundary();
		HomePageReply reply = (HomePageReply) boundary.process(new DeleteCommand(id));
		return redirect(routes.Application.tasks());	
	}  

	public static Result logout() {
	return ok("ok...");
	}
}

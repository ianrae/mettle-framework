package controllers;

import java.util.List;

import org.mef.framework.commands.IndexCommand;
import org.mef.framework.commands.Command;
import org.mef.framework.replies.Reply;
import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;

import boundaries.Boundary;

import mef.presenters.HomePagePresenter;
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
	public static class Bound extends SfxBaseObj
	{
		public Bound(SfxContext ctx)
		{
			super(ctx);
		}
		public Reply process(Command cmd, Object route)
		{
			HomePagePresenter presenter = new HomePagePresenter(_ctx);
			
			Reply reply = presenter.process(cmd);
			if (reply.failed())
			{
				return null; //some hard-code error page
			}
			else if (reply.getForward() != null) //change to forward
			{
				return null; //return route
			}
			return reply;
			
		}
	}
	
	
	static Form<Task> taskForm = Form.form(Task.class);  
	
    public static Result index() {
//        return ok(index.render("Your new xxapplication is ready."));
//        return ok("hello Play!");
		  return redirect(routes.Application.tasks());
    }
	
	public static Result tasks() 
	{
		Boundary.getHomePresenter(); //inits dal in svcloc
		Bound boundary = new Bound(Boundary.theContext);
		HomePageReply resp = (HomePageReply) boundary.process(new IndexCommand(), null);
		
		List<Task> L = Boundary.convertToTask(resp._allL);
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

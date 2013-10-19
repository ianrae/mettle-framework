package controllers;

import models.Flight;
import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {
  
    public static Result index() {
//        return ok(index.render("Your new application is ready."));
    	
    	Logger.info("INCONTROLLER!!!!");
//    	Flight flight = new Flight();
////    	flight.setName("airOne");
//    	flight.name = "aixrOne";
//    	flight.save();
    	 PLogger.log("xxHere's my log message");    	
//		return ok(views.html.flight.render(flight));    	
		return ok(views.html.index.render(""));    	
    	
    }
  
}

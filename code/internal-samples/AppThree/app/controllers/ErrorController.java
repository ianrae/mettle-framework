package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import models.*;

import views.html.*;

public class ErrorController extends Controller {
	
    public static Result index() {
        return ok("hello Play!");
    }
	
    public static Result showError(String error)
    {
    	return ok(views.html.error.render(error));
    }

	public static Result logout() {
	return ok(
		views.html.error.render("")
	  );

	}
}

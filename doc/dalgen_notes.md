MEF
==========

Introduction
-------------
MEF is a fast TDD framework for Play Java applications.  It's based on the Robert Martin "entity-boundary-interactor" architecture
described here.  

http://confreaks.net/videos/759-rubymidwest2011-keynote-architecture-the-lost-years

This architecture separates application code from any dependency on database, the web, or even the Play framework.
The core of your app is POJO.  Unit tests run extremely fast (no need for entity manager or fakeApplication).

In MEF, each model class now becomes two classe: An "entity object" that contains only public fields, and a "DAL object" that manages
persistence.  Entity and DAL classes are created with a code generation tool called DALGEN.  DALGEN creates mock DALs for unit testing,
and real DALs for integration with your eBean or JPA Model object (which DALGEN also creates).  The Model object is fully annotated
with JPA and validation annotations.

Terminology
-------------
In his talk, Robert Martin, uses a number of terms, not because they are particularly elegant, but to avoid confusion with the 
existing model, view, controller terms.

 * Entity -- data-only object that represents a domain object. like a C struct.
 * Interactor -- contains business logic.  Receives requests and produces responses.
 * Boundary -- sits between the app and the MEF code, managing their interaction.
 * Gateway -- database persistence hidden behind interfaces.
 
MEF uses slightly different terminology.

 * Entity -- POJO class. no code. only public fields
 * Presenter -- contains business logic. 
 * Boundary -- same as above.
 * DAL -- Data Access Layer. database persistence hidden behind interfaces.
 
Pros and Cons
------------------
MEF apps are
 * faster to develop due to better TDD experience
 * easier to port if Play changes significantly or you want an Android version of your app
 * less boilerplate code to write. DALGEN!
 
Cons:
 * more classes
 * easy things are a bit harder

 
DAL
-------
For each entity, DALGEN creates a number of classes. For example, a 'Task' entity would result in
 * Task - the entity object
 * ITaskDAL -- interface to the DAL for Task objects
 * MockTaskDAL -- for unit testing
 * TaskDAL -- the real DAL
 * TaskModel -- the Play Model-based class. Used by TaskDAL. Can be either eBean or JPA.
 * TaskDALUtils -- helper class for converting between Task and TaskModel
 
DALGEN reads an XML file that specifies the entities

<entities orm="ebean" >
<entity name="Task">
<field>@Id Long id</field>
<field>@Required String label</field>
<query>find_by_label</query>
<interface extend="true" />
<mock extend="true" />
<real extend="true" />
</entity>
</entities>

Run DALGEN from the root directory of your application.

Directory Structure
--------------------
The "*" shows where DALGEN puts files

 app
   boundaries
   controllers
   models  *
   views
 dal
   interfaces  *
   mocks       *
   reals       *
   utils       *
 mef
   entities    *
   presenters
   unittests
   
Presenters
---------------
Each controller in your Play application has a corresponding presenter.  The process method processes a command and
produces a reply ready to be rendered by the view.

	Reply process(Command cmd);

This method uses reflection to call a matching doXYZ() method in the presenter. For example, a DeleteCommand would
call a onDeleteCommand method.
	
For a TaskController you would create these MEF classses

 * TaskPresenter
 * TaskReply

Command objects contain the web request's CGI params and/or form data.  
@{play.mvc.Http.Context.current().args(key)=value}
GET   /clients/:id          controllers.Clients.show(id: Long)  
cgi: public java.util.Map<java.lang.String,java.lang.String[]> asFormUrlEncoded()
or maybe:    DynamicForm data = Form.form().bindFromRequest(); // will read each parameter from post and provide their values through map accessor methods
 


Reply objects contain all the data needed to render that page. All data is entity objects.
Reply object support
 * failed -- something bad happened, goto error page
 * forward -- redirect.  Is a string that represents a route. 
 * view -- which view to render. "DEFAULT" means render the normal view for that controller method
 * flashMessage
 
The reply object returned by process does have to match the command. 

Sample method

  Reply onEditCommand(EditCommand cmd)
  {
     Task task = _dal.findById(cmd.id);
	 IFormBinder binder = cmd.getFormBinder();
	 binder.init_empty_form(task);
	 return _reply; //already created for you
  }


Filters
-------
Presenters can have filters attached. "Pre" filters are run by Presenter.process() before the presenter's doXYZ method is called.
They can adjust the command or terminate further processing.  For example, an auth filter might check if the user is currently logged in
and return a redirect reply if not.
"Post" filters are run after doXYZ()

  
Service Locator
-----------------
All objects in MEF have access to a service locator object, that will create DALs, etc as needed.

Views
--------
Play already has excellent views.  MEF does not do any view code.  The reply object should contain enough data to render
the page.

Play views can be easily tested as well (no fakeApplication needed).

Lifecycle of a Web Request
------------------------

 * Play receieves the request and routes to a controller
 * Controller method uses boundary to create a presenter and command object
   * presenter is loaded with any filters it has been configured for
 * Presenter.process called. It calls any pre-filters.
 * onXYZ() called.  It generally uses one or more DALs to get or modify entities. It returns a reply object.
 * controller does one of two things:
   * redirects to a route specified by the reply
   * renders the view specified by the reply
   
Forms
------
On a 'new' or 'edit' action the presenter will create an empty form.  It will use an IFormBinder that calls

   Form<Task> form = taskForm...bind from Task 
   
Controller would do
   Form<Task> form = (Form<Task>)reply.formBinder.getForm();
   views.html.index.render(form, ...
   
On postback, the controller would create a formbinder holding the form data, and the presenter would (secretly) call

  Form<TaskModel> filledForm = taskForm.bindFromRequest();
  and   filledForm.get() to get the TaskModel

Recall that TaskModel has the validation annotations, and optionally a validate method.

Partial Forms
---------------
Sometimes you want to edit only a few fields of an entity. How to avoid validation of the other (empty) fields.

DALGEN can create a PartialTask class that hooks into the Task DALs.  Also creates a PartialTaskModel for validation.

DAL and eBean and JPA
--------------
Each entity class has a field

   public Object carrier;
   
This holds the corresponding model object for that entity.  That is, if you call TaskDAL.findById(45) you will get a Task
object (with id 45) whose carrier is the TaskModel object returned by eBean (or JPA).

And the model object holds its corresponding entity 

	@Transient
	private Object entity;
 
JPA does object comparison to determine what has changed in an object.   Since all the data is in the entity, JPA can
call all the getters and see that the object has changed. Good.

eBean does bytecode enhancement.  Therefore, since the presenters work with the entity, none of the model objects getters and
setters are called.  So eBean may believe nothing has changed in the object.  Until I find a better way, the DAL will simply
use each setter in save so that everything gets saved.

   void save(Task entity)
   {
      TaskModel model = (TaskModel)entity.carrier;
	  if (model == null)
	  {
		model = new TaskModel();
		copyToModel(entity, model);
	  }
	  //ebean-only (all except id)
	  model.setLabel(entity.label);
	  model.setEnabled(entity.enabled);
	  model.save();
   }	  

  
Associations
---------------
User has many Tasks

User
 List<Tasks> tasks;
 
-for now when we get an object from DB, we get whole thing

 Task findById(long id)
 {
    TaskModel model = TaskModel.finder.find(id);
    if (model != null)
    {
       Task entity = new Task();
       entity.carrier = model;
       copyTo(model, entity);	   //entity.label = model.getLabel(), ...
    }
  }
  
-support lazy later!
  //perhaps copyTo skips List<Task> tasks.
  //presenter logic would have to many that tasks is null and do
     user.tasks = User.loadTasks();
  //or user.tasks would be a LazyList that would load when first accessed
  
What is inside MEF and what is outside?
--------------------------------------   
MEF has two main goals.  Fast TDD and abstracting away the web-ness of an app.  MEF apps should
be much easier to port to mobile apps, desktop apps, etc.

Should authentication, for example, be handled inside MEF or outside (in the controller).
There is a basic tradeoff here.  To be inside, you need to create interfaces and mock objects.
But you now get much faster TDD of that feature.

However, if the support in the controller is already very good, then leave it there. Play authentication
uses annotations in your controller and is easy to use. You can still
test with integration tests.

DALGEN
---------------

	dalgen /version    prints out version
	
	dalgan new         generates mef directories
	
	dalgen             runs and generates files, ovewriting any existing files
	
	startup
	cdir is current dir
	read dalgen.xml --> error out
	for each entity
	   generate entity, idal, mockdal, realdal, utils, model --> all into textbuffers
	   
	if all ok then
	  write out all textbuffers
	  
deploy
    dalgen-lib.jar
	dalgen.bat
	
	
-------------------------------24SepTue
to do
------
done formbinder
done codegen 

-codegen of boundar
-user all 6 methods
-dal should work properly with carrier and entity


CODEGEN

 Entity
 Model
 DalInterface
 MockDAL
 RealDAL
 
 
 
 

+    public void setPhone(Phone val) {
 		this.phone = val;
-		if (val != null)
-		{
-			Phone entity = PhoneDAL.createEntityFromModel(phone);
-			this.entity.phone = entity;
-		}
-		else
-		{
-			this.entity.phone = null;
-		}
+        this.entity.phone = val;

 


DALAUTH

Users - users table with id

UserGroup - id, userId.  So UserGroup:User is many-to-many 

Subject - id, "User.*", "User.5", "UserGroup.*", "UserGroup.6"


Tables - table with all id,tablename
-also "System" is a table group that means everything. same as Table.*

TableGroup - id, tableId.  So TableGroup:Table is many-to-many
 -PatientTables
 -DoctorTables

Target - id, "Table.*", "Table.5", "TableGroup.*", "TableGroup.5"

Role:
 Viewer, Editor, Full
 -no None role because absence in auth tables means not authorized
 
Auth -the main table
 - subjectId, targetId, roleId

 UserGroup.admin  System  Full  -admins are superusers
 UserGroup.hospitalAdmins PatientTables  Viewer -managers can view any table
 UserGroup.hospitalAdmins DoctorTables  Viewer -managers can view any table
 
 User.5  PatientTables.6  Full -Dr X has full access to his patients
 -when create a doctor user
   -create target  idPatientTables, callit "Dr X's Patients"
   -then all patient tables have targetId -that is the targetId of their doctor
   -add auth: DrX, idDrXPatients, Full
   -so auth is user -> subject  current-table.targetId => role
 
 -so for an entity can look it up by its tblTables.id or by record.targetId

 -superuser:   user.groupId is admin and then search auth: user.groupId, system
   -if role is full then allow everything
   
 Caching. create another table - userId, queryId, boolResult
  -on every auth-query add to cache
  -clear cache if any auth tables modified
  
-----------------------------28SepSat
1. got debug in eclipse going
 play debug run
 eclipse: setup debug config for 'remote application' press New button, port 9999

MANYTOONE
@ManyToOne PhoneModel phone; //means the class defining the many-to-one is on the one end. it has foreign key 

http://stackoverflow.com/questions/13811844/jpa-onetomany-and-manytoone-relationships
Says this defines user_id key in Post, not foreign key in User
public class User{
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="user")
    public List<Post> posts;
}
public class Post {
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn("user_id")
    public User user;
}

 
2. got ebean fetch working so a UserModel query will get its PhoneModel
	UserModel t = Ebean.find(UserModel.class).fetch("phone").where().eq("id", id).findUnique();		

   UserModel                       so createEntityFromModel should invoke createEntityFromModel on its entities fields
     id
     name
	 phone
     [entity
        id
        name
        phone
      ]

3. Lazy loading.  An important feature User.phone only gets loaded on first access.  How to do this in MEF?

User.phone in entity won't work. It's a field.  Options are:

a) use phone field if eager and change to getter/setters if change to eager loading. Ugly and forces code changes all over the place

b) use getters/setters. So user.name would still be a field, but user.getPhone()/setPhone()
 user would have boolean lazy_load_needed. if true it would call the dal which would simply refer to any field (other than id) in phone
   UserModel t = ...;  t.phone.name(); //force lazy load
http://www.poornerd.com/2013/01/04/play-framework-2-0-ebean-lazy-loading-and-object-references-that-are-null/

c) create a slug Phone. so user.phone points to an object that does lazy loading. not sure how to do that!

d) leave it as a field and add a load() method.  Then app would have to know that if user.phone is null, call load()
This is also ugly and dangerous if change between eager and lazy.

for now, try d)
  public Phone phone;
    private boolean phone_needs_loading;
    public void loadPhone()
    {
    	if (phone_needs_loading)
    	{
    		//IUserDAL.loadPhone() which will do user.phone.getName()
    		phone_needs_loading = false;
    	}
    }
	
	
		entity.phone = PhoneDAL.createEntityFromModel(t.getPhone());
		

DB SEED
2.sql added records but then ebean wanted to use same ids.
  #following screws up auto-generating of ids by ebean
#insert into user_model  (id,name,phone_id) values (  1,'MacBook Pro 15.4 inch',1);

in Global.onStart do
  Phone phone = dal.find_by_name("apple")
  if phone == null : phone = new Phone(), ..init fields.. dal.save()
  
  @if(userForm("phone") != null) { aaa: @userForm("phone.name").value()
                            } else { nil }


MASSIVE REORG OF DIRECTORY STRUCTURE

-play run wasn't finding mef framework, since its now a separate project
-so edited build.scala added

	resolvers += Resolver.file("meflib.var", file("lib")) transactional()   

the export Framework to myfirstapp\lib\meflib.jar

 
 C:\Users\ian\Documents\GitHub\dalgen\code\tools\dalgen\src\samples\MyFirstApp
 
 C:\Users\ian\Documents\GitHub\dalgen\code\samples\MyFirstApp

------------------------4OctFri
loading has-a

  {  "id": "2", "name": "Apple Computer Inc" }
 ,{  "id": "3", "name": "IBM" }
 ,{  "id": "4", "name": "Gencorp" }

When load set save id and set to 0.
then create map knownObj<id, obj>

Then somehow load Computer

  {  "id": "0", "name": "CanadianTire" "company": "2"}

-when read comany="2", look it up in knowObj 

http://wiki.fasterxml.com/JacksonInFiveMinutes json tutorial
      User[] arUser = mapper.readValue(json, User[].class);
-fails if don't have complete User with embedded phone


    entity.phone = PhoneDAO.createEntityFromModel(t.getPhone());

        t.setPhone(PhoneDAO.createModelFromEntity(entity.phone));

-------------
boundary
init
appboundarie
dao

public com.avaje.ebean.PagingList<T> findPagingList(int pageSize)

If you are building a stateless web application and not keeping the PagingList over multiple requests then there is not much to be gained in using PagingList. Instead you can just use Query.setFirstRow(int) and Query.setMaxRows(int).

IPLAY MOCKING DB
http://blog.jooq.org/2013/02/20/easy-mocking-of-your-database/
http://stackoverflow.com/questions/1151550/is-there-a-set-of-stubs-mocks-for-jdbc-available-anywhere

https://github.com/Lugribossk/play-rest-experiment/blob/master/test/controllers/api/PersonControllerInMemTest.java
http://eng.42go.com/speed-up-play-tests-direct/ a competitor but is Scala!

http://stackoverflow.com/questions/19089412/reading-from-a-file-in-play-on-heroku how to read files as resources
https://groups.google.com/forum/#!searchin/play-framework/h2$20database$20test/play-framework/jjZuwIHNMIk/WBRq6e57NC4J using H2
https://groups.google.com/forum/#!searchin/play-framework/h2$20database$20test/play-framework/NeOuWOXYTNs/zFMlEpBsTXwJ critic of Play for TDD
http://hsqldb.org/doc/2.0/guide/index.html
and H2
http://java-success.blogspot.com/2012/10/sql-tutorial-with-hsqldb.html
http://www.javaworkspace.com/connectdatabase/connectHSQLDB.do

PLAY
http://twitter.github.io/scala_school/sbt.html


-------------------------10OctTue
HEROKU
-AppTwo
-stark-shelf-1549.herokuapp.com

http://stark-shelf-1549.herokuapp.com
-works!
-copied in Computer example.
 -deleted one of the views (not needed)
 -edit build.scala
addSbtPlugin("play" % "sbt-plugin" % Option(System.getProperty("play.version")).getOrElse("2.1.3"))
 -works!
-play eclipse

STEPS TO ADD MEF
-gen:app to create mef.xml and copy in some files
-add lib/mef.jar
-test/mef/OtherTests.java - Initializer.init()
-modify build.scala
-create an entity: gen:entity
-write some entity tests
-create a presenter: gen:presenter
-write presenter tests
-uncomment stuff in Initializer!!
-YIKES
 -EntityLoader and Dal..Loader not created by codegen
 -EntityLoader is all entangled. can't create just Company
 -first test must do Initializer.createContext not init

-use presenter in Controller
-update view
-deploy to heroku

--------------------12OctSat
MORE STEPS TO ADD MEF TO COMPUTER SAMPLE
-add line in 1.sql so EBean will generate it (since table now called computer_model, not computer)
-add common-io to build.scala
-add ErrorC
-add all routes to routes

-Computer works, can see 571 computers with paging and sorting
-? should reply contain orderBy, etc
-DONE get filter working
-DONE get Company showing
-DONE Add
-DONE Delete
-DONE Edit

what about ds.createQuery(Person.class).filter("id >", 950).filter("id <", 960).asList() ?

update not working
http://stackoverflow.com/questions/12881576/ebean-2-7-3-play-framework-2-0-4-optimisticlockexception-nullpointerexcept

http://blog.matthieuguillermin.fr/2012/11/ebean-and-the-optimisticlockexception/

performance = Double.valueOf(String.format("%.10f", performance).replace(",", "."));

------------------------------17OctThur
https://groups.google.com/forum/#!topic/play-framework/F72DYs2MPsA Rails-Play comparison
http://typesafe.com/blog/introducing-slick Slick
https://github.com/freekh/play-slick
https://github.com/rails/arel like slik but for rails

MCOMPUTER - UPDATE NOT WORKING
-can see that id is null, and fiddler shows id is in the url but doesn't end up being parsed by form binder
-MyFirst app uses different way:
POST    /users/update           controllers.UserC.updateUser(id: Long)
http://localhost:9000/users/update?id=1

-here id does get parsed

-deployed to heroku
-added meflib.jar to build.scala
http://stark-shelf-1549.herokuapp.com
-app error: says i should set -DapplyEvolutions.default=true

heroku pg:reset DATABASE --confirm stark-shelf-1549   sleepy-hollows-7957
-twice!
heroku restart
heroku ps
-but only 123 computers should be 571

-here's the problem:
update computer_model set company_id=43
where id=529 and name='Samsung Galaxy Tab' and introduced=2010-09-02 00:00:00.0
and discontinued is null and company_id=42

OK WHAT'S NEXT -- REMINDME APP
-login
-add phone # email (to receive an sms)
-if don't receive reply in X hours, send email to work email
-so need
 -twitter bootstrap
 -authentication: login/logout/conf-email/block/ user auth admin pages
 -user: name,pwd,email,isAdmin
 -send email
 -receive email (background task)
-seed data
 -admin user 
 
http://securesocial.ws/
 
 
--------------------------------18OctFri
PLAY MODULE
http://www.objectify.be/wordpress/?p=363
created mmod and publish-local

So
-play new mmod
-copy everything into mmod\project-code
-delete routes, empty app.conf
-delete views and application.java
-write controllers\plogger.java
-clean, publish-local
-go look in $PLAY/repository/local
C:\ianTools\play\play-2.1.2\repository\local\mmod\mmod_2.10\1.0-SNAPSHOT\jars

to use in an app:
-build.scala, add:     "mmod" % "mmod_2.10" % "1.0-SNAPSHOT"
-play eclipse to update project
-restart eclipse
-in Application.java do PLogger.message("sdfsdf");
-works!

http://developer.vz.net/2012/03/16/writing-a-play-2-0-module/ plugins

https://github.com/playframework/modules.playframework.org/wiki eventually publish to here

So how do we do this?
create module mettle
 project-code/app/org.mettle.framework.zzz.java
              app/org.mettle.tools.dalgen
 build and publish

Then once add to build.scala and re-gen eclipse proj, can do:
 import org.mettle.framework

-can add to controllers, mef.*, and tests

publishing on github:
http://java.dzone.com/articles/publishing-play-2-modules
my normal github url is https://github.com/ianrae/mcomputer
Pages:
Your project page has been created at http://ianrae.github.io/mcomputer . 
-wait: i need to create a new public repo: https://github.com/ianrae/ianrae.github.io
-so, you publish-local then copy jars and everything into github, releases/ dir

							  
 
 https://github.com/hakandilek/play2-crud a bit like mettle!
 

schaloner / schaloner.github.com

http://schaloner.github.io/releases/README


----------------------------19OctSat
METTLE module
-created and published
C:\ianTools\play\play-2.1.2\repository\local\mettle\mettle_2.10\1.0-SNAPSHOT

So in apptwo, build.scala
add: "mettle" % "mettle_2.10" % "1.0-SNAPSHOT"
remove:
	resolvers += Resolver.file("meflib.jar", file("lib")) transactional()	

-added dalgen to mettle.
-renamed dalgen to mgen
-AppTwo
 -created new folder mgen where i will put codegen stuff
 -crap. need sfxlib.jar added to AppTwo
 

C:\Users\ian\Documents\GitHub\dalgen\code\internal-samples\AppTwo
C:\Users\ian\Documents\GitHub\dalgen\code\mettle\app\org\mef\tools\mgen\resources
C:\Users\ian\Documents\GitHub\mcomputer 
 
http://stackoverflow.com/questions/14601982/what-sbt-build-setting-can-i-use-with-the-play-2-framework-that-will-include-sp

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





 
 
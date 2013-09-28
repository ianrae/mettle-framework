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

resolvers += Resolver.file("my-test-repo", file("test")) transactional()

   
 
 


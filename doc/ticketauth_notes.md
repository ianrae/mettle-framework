Ticket-Based Auth

An authorization system that is a separate module you can add to your app.
Re-uses a lot of existing data relationships in your data to allow for powerful
authorization schemes.

The main goal of TBA is data-specific auth.  Not just, am i an "Editor", but 
am I an "Editor" on this article.

You add code to your controllers to limit access using the pattern.  TBA does
not work at the database level.  Your app code is free to read and write anything.
However, your controllers, which are basically publicly acessible on the internet
need authorization code to ensure only valid users with appropriate permissions can
use that controller.

Subject-Role-Target architecture.
TBA uses a three-part rule:

Does subject S have role R in target T?

If they do then they allowed to continue, if not they are sent
to an error page.

The main auth table is

  userId, subjectId, RoleId, targetId

(Only one of userId and subjectId can be non-null. Users are implicitly subjects -- see below).  
  
Authorization is additive.  You begin with none and then are given auth by
matches in the auth tables.

Subjects
Subjects are generally users. Although TBA supports an extensible scheme
where other entities in your system can be subjects.

You add rules for subjects to give them permissions to various parts of your system.

Roles
Roles a simply a set of strings that you give meaning to.  A simple set of
roles might be:
  Viewer - can viewer data
  Editor - can modify data
    
Roles are used in your controllers.  The main auth method is

 boolean isAuth(String roleName, Long targetTicketId);
 
This checks if the current user has the given role for the specified target.
	
Targets
Targets are entities in your system that you want to be under control
of authorization.  Examples:
 * only students in class X can view the Class X home page
 * only teacher of class X can add messages to the Class X home page
 * school district coordinators can view the home page of any class in their district.

Tickets
A ticket is anything you want to act as a subject or target.  The TBA ticket table
contains all the tickets in your system.

Tickets are an abstraction that let TBA be independent of the rest of your database.

Target Tickets
Generally you will associate a ticket per item of some other table.  For example, in
a school web site, the Class table could include a ticketId column.  Every time a 
new class is created, a new ticket is created and associated with the class.

Then you can add authorization rules, such as Teacher T1 has Role "Owner" of Class C1,
where class's ticketId in is the target.

Targets must be tickets.

Subject Tickets
In TBA users are implictly subjects.  The authorization table supports two types of rules

 *  userId, roleId, targetTicketId  
 *  subjectTicketId, roleId, targetTicketId
  
You can add per-user authorization directly without having to create a subject ticket.

How do you manage groups of users?  There could be a user-group table in TBA but that
would result in a lot of duplicate data to keep in sync, since your system probably already
supports various groups of users.  For example, students in a school, students in a class,
teachers in a school, etc.

Instead, TBA allows the subject to be a ticket.  In fact, a ticket can be both the subject
and target of itself.  For example, we want the students to class X to be able to view
their class X home page.   We could add a rule:

class-X-ticket 35 has "Viewer" role on class-X-ticket.

In our ClassHomePage controller, we would add code to loop through the student's classes
and check if any class's ticket had "Viewer" role on itself.  If a match is found, the
student can view the home page.

The God Rule
For admins, we would usually create a system ticket, and then add a rule

system-ticket has "Everything" role on system-ticket.

Then any user who has isAdmin flag set to true would have access to everything in the system.
The code would look like this:

 if (user.isAdmin && isAuth(systemTicketId, "Everything", systemTicketId))
 { //allowed
   //..
   
Other Types of Subject Tickets
A school system has students, classes, teachers, schools, districts, and states.

The school management system already tracks various relationships. For example:
 * a teacher can teach at multiple schools (within a district)
 * a school is a member of one district
 * a student is a member of one school
 * a student can be in multiple classes
 * etc.

You could create school-level permissions by creating a school ticket and using
it as a subject-of-itself rule.  Then your code could allow any staff member of 
a school to do certain things.

For fine-grain control, create multiple tickets per school.  A schoolAdmin ticket
would let the principal and other administrators have certain permissions.
A schoolStaff ticket would let other members of staff have lesser permissions.

This is a code-centric model.  Your class home page might be checking for admin rules,
teacher rules, class-level rules, or school-level rules.

 
 
 

   

	 
	 
	   

 
  






better algorithm:  our main API that presenters call is

bool isAuth(userId, objTypeName, authId, roleName); //return true if user has role on object

      isAuth(userId, "Post", post.authId, "Editor"); //auth a port

      isAuth(userId, "Post", null, "Editor"); //auth the post table

-get userGrpId and objectGrpId and roleId
-create L
-L += query roles where userId=userId and authId=authId
-L += query roles where userGrpId=userGrpId and authId=authId
-cache L with key userId
-cache at-most N of these, kick out random one
-then can eval(L) where each obj in L is: userId,userGrpId,authId,roleId
-find if any matches for roleId. most users will have < 50 items in L
-nice balance, we only query db for roles once then keep for most of session
-clear cache if any changes to user,usergrp,authId,role tables
 -actually can be smater
    user: only clear if Delete
    userGrp: clear if and CRUD action
    authId: clear if Delete (will never be updates)
    role: clear if any action

Role Chains: viewer -> editor -> owner
-the isAuth only checks for a single role. We sometimes want escalating roles,where
each new role has all the permissions of the ones below it.
-viewer  can read
-editor  can update (and read)
-owner   can create and delete (and update and read)

-perhaps our Admin CRUD would just add 3 rows for an owner, so Bob is a viewer,editor, and owner of object 35
-otherwise the auth code would have to implicitly search for multiple roles

This auth based on Subject has Role on Object.  No notion of 'read' or 'write' permisions, just check for roles.
Rails CRUD on an auth controlled entity
 Index   Viewer on Post table
 Show    Viewer on Post.id
 New     Owner on Post table
 Create  Owner on Post table
 Edit    Editor on Post.id
 Update  Editor on Post.id
 Delete  Owner on Post.id

-so if have 200 tables and 20 groups and Role Chain is 3 then need
  3 rows per group per table, so 12000 entries in role table

ObjectGroupId
-each auth controlled table now has two columns
   authId
   authGrpId
   -only one can be non-null
-now can group a bunch of tables together as a single 'object' for auth
-is more complicate. you create a new authGrpId for a logical thing such as a blog post, even
 if it results if different numbers of rows in the various blog tables
-so if one table is the master then do new-authgrp-id for every new row in master table
-then all slave tables would use the authgrpid of the their master record

Or..instead of authId being for tables, they are more per-controller which is a logical thing.
So for blog posts there is a main Post table but also PostHistory, PostComments tables.
-only create authId for new record in Post table since all methods in Post controller will have access to the
post object.

-so if have 50 controllers and 20 groups and Role Chain is 3 then need
  3 rows per group per table, so 3000 entries in role table
 



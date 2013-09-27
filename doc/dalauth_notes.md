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
  
  
   
 
 


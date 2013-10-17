Mettle Entity Framework (MEF)
===========================


Intro
----------

MEF is a fast TDD framework for Play Java applications.  It's based on the Robert Martin "entity-boundary-interactor" architecture described here.  

http://confreaks.net/videos/759-rubymidwest2011-keynote-architecture-the-lost-years

The goal of this architecture is to remove platform dependencies from application code.  That is, remove dependencies on databases and the web.
The core of your app is POJO.  Unit tests run extremely fast (no need for entity manager or fakeApplication).

In MEF, each model class now becomes two classe: An *entity* that contains only public fields, and a *DAO* that manages persistence.  
Entity and DAO classes are created with a code generation tool called MGEN.  MGEN creates mock DAOs for unit testing, and real DALs for integration with your 
eBean or JPA Model object (which mefgeneration also creates).  The Model object is fully annotated with JPA and validation annotations.

Terminology
-------------
In his talk, Robert Martin uses a number of terms, not because they are particularly elegant, but to avoid confusion with the 
existing MVC terms.

 * Entity -- data-only object that represents a domain object. like a C struct.
 * Interactor -- contains business logic.  Receives requests and produces responses.
 * Boundary -- sits between the app and the MEF code, managing their interaction.
 * Gateway -- database persistence hidden behind interfaces.
 
MEF uses slightly different terminology.

 * Entity -- POJO class. no code. only public fields
 * Presenter -- contains business logic. 
 * Boundary -- same as above.
 * DAL -- Data Access Layer. Consists of one DAO per entity. database persistence.
 
Advantages
------------------
MEF apps are
 * faster to develop due to better TDD experience
 * easier to port if Play changes significantly or you want an Android version of your app
 * less boilerplate code to write. 
 
Sample Application
-------------------
Mettle comes with several samples.  

A MEF implementation of the Computer-Database sample is [here](https://github.com/ianrae/mcomputer). A version of this app
 is [running on Heroku](http://stark-shelf-1549.herokuapp.com).
 
 
 

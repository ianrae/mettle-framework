package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.Constraint;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity 
public class Flight extends Model {

    @Id
    public Long id;
    
    @Required 
    public String name;
    
    

	/**
     * Generic query helper for entity Computer with id Long
     */
    public static Finder<Long,Flight> find = new Finder<Long,Flight>(Long.class, Flight.class); 
    
    
}

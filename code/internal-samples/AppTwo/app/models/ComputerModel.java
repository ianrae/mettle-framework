//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package models;

//import org.mef.framework.entities.Entity;
import play.data.*;
import java.util.*;
import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.Constraints.*;
import javax.persistence.*;
import mef.entities.*;
import boundaries.daos.*;
import java.util.Date;
@Entity
public class ComputerModel extends Model
{
	@Transient
    public Computer entity = new Computer(); //needed else get illegalStateException


	public static Finder<Long,ComputerModel> find = new Finder(
			Long.class, ComputerModel.class
			);  

	public static List<ComputerModel> all() {
		return find.all();
	}
	public static void delete(Long id) {
		find.ref(id).delete();
	}

    //getters and setters
           @Id 
    private Long id;

    public Long getId() {
        return this.id;
    }
    public void setId(Long val) {
		this.id = val;
		this.entity.id = val;

    }

   @Required 
    private String name;

    public String getName() {
        return this.name;
    }
    public void setName(String val) {
		this.name = val;
		this.entity.name = val;

    }

   @Formats.DateTime(pattern="yyyy-MM-dd") 
    private Date introduced;

    public Date getIntroduced() {
        return this.introduced;
    }
    public void setIntroduced(Date val) {
		this.introduced = val;
		this.entity.introduced = val;

    }

}

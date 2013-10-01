//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package models;

//import org.mef.framework.entities.Entity;
import play.data.*;
import java.util.*;
import play.db.ebean.*;
import play.data.validation.Constraints.*;
import javax.persistence.*;
import mef.entities.*;
import boundaries.dals.*;
@Entity
public class PhoneModel extends Model
{
	@Transient
    public Phone entity = new Phone(); //needed else get illegalStateException


	public static Finder<Long,PhoneModel> find = new Finder(
			Long.class, PhoneModel.class
			);  

	public static List<PhoneModel> all() {
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
    public void setId(Long id) {
		this.id = id;
		this.entity.id = id;

    }

   @Required 
    private String name;

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
		this.name = name;
		this.entity.name = name;

    }

}

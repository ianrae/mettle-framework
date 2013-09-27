package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import mef.entities.Phone;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

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
    Long id;

    public Long getId() {
        return this.id;
    }
    public void setId(Long val) {
		this.id = val;
        this.entity.id = val;
    }

    @Required
    String name;

    public String getName() {
        return this.name;
    }
    public void setName(String val) {
		this.name = val;
        this.entity.name = val;
    }

}

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
public class UserModel extends Model
{
	public UserModel()
	{}
	
	@Transient
    public User entity = new User(); //needed else get illegalStateException


	public static Finder<Long,UserModel> find = new Finder(
			Long.class, UserModel.class
			);  

	public static List<UserModel> all() {
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
    public void setName(String val) {
		this.name = val;
		this.entity.name = val;

    }

   @ManyToOne 
    private PhoneModel phone;

    public PhoneModel getPhone() {
        return this.phone;
    }
    public void setPhone(PhoneModel phone) {
		this.phone = phone;
		this.entity.phone = PhoneDAL.createEntityFromModel(phone);

    }

}

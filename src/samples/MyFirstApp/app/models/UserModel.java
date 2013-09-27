package models;

//import org.mef.framework.entities.Entity;
import play.data.*;
import java.util.*;
import play.db.ebean.*;
import play.data.validation.Constraints.*;
import javax.persistence.*;
import mef.entities.*;

@Entity
public class UserModel extends Model
{
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
    Long id;

    public Long getId() {
        return this.id;
    }
    public void setId(Long val) {
		this.id = val;
        this.entity.id = val;
    }

    String name;

    public String getName() {
        return this.name;
    }
    public void setName(String val) {
		this.name = val;
        this.entity.name = val;
    }

}

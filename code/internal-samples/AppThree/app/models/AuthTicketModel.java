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
import org.mef.framework.auth.AuthRole;
import org.mef.framework.auth.AuthTicket;
@Entity
public class AuthTicketModel extends Model
{
	@Transient
    public AuthTicket entity = new AuthTicket(); //needed else get illegalStateException


	public static Finder<Long,AuthTicketModel> find = new Finder(
			Long.class, AuthTicketModel.class
			);  

	public static List<AuthTicketModel> all() {
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

    private String name;

    public String getName() {
        return this.name;
    }
    public void setName(String val) {
		this.name = val;
		this.entity.name = val;

    }

}

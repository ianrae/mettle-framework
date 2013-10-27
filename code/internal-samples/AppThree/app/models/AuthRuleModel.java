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
public class AuthRuleModel extends Model
{
	@Transient
    public AuthRule entity = new AuthRule(); //needed else get illegalStateException


	public static Finder<Long,AuthRuleModel> find = new Finder(
			Long.class, AuthRuleModel.class
			);  

	public static List<AuthRuleModel> all() {
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

   @ManyToOne 
    private UserModel user;

    public UserModel getUser() {
        return this.user;
    }
    public void setUser(UserModel val) {
		this.user = val;
		this.entity.user = UserDAO.createEntityFromModel(val);

    }

    private Long roleId;

    public Long getRoleId() {
        return this.roleId;
    }
    public void setRoleId(Long val) {
		this.roleId = val;
		this.entity.roleId = val;

    }

    private Long ticketId;

    public Long getTicketId() {
        return this.ticketId;
    }
    public void setTicketId(Long val) {
		this.ticketId = val;
		this.entity.ticketId = val;

    }

}

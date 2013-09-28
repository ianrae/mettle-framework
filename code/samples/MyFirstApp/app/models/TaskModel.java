package models;

//import org.mef.framework.entities.Entity;
import play.data.*;
import java.util.*;
import play.db.ebean.*;
import play.data.validation.Constraints.*;
import javax.persistence.*;
import mef.entities.*;

@Entity
public class TaskModel extends Model
{
	@Transient
    public Task entity = new Task();


	public static Finder<Long,TaskModel> find = new Finder(
			Long.class, TaskModel.class
			);  


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

    String label;

    public String getLabel() {
        return this.label;
    }
    public void setLabel(String val) {
		this.label = val;
        this.entity.label = val;
    }

    boolean enabled;

    public boolean getEnabled() {
        return this.enabled;
    }
    public void setEnabled(boolean val) {
		this.enabled = val;
        this.entity.enabled = val;
    }

}

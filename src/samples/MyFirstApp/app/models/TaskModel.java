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
    private Task entity;
    public void setEntity(Task entity)
    {
        this.entity = entity;
    }
	public Task getEntity()
	{
		return this.entity;
	}


	public static Finder<Long,TaskModel> find = new Finder(
			Long.class, TaskModel.class
			);  


    //getters and setters
            Long id;

    public Long getId() {
        return this.entity.id;
    }
    public void forceId(Long val) {
        this.entity.id = val;
    }

    String label;

    public String getLabel() {
        return this.entity.label;
    }
    public void setLabel(String val) {
        this.entity.label = val;
    }

    boolean enabled;

    public boolean getEnabled() {
        return this.entity.enabled;
    }
    public void setEnabled(boolean val) {
        this.entity.enabled = val;
    }

}

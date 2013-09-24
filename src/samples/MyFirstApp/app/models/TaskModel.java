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

	@Id
	private Long id;
	
	@Required 
	private String label;
	
//	private boolean enabled;

    //getters and setters
    public Long getId() {
        return this.entity.id;
    }
    public void setId(Long val) {
    	this.id = val;
        this.entity.id = val;
    }

    public String getLabel() 
    {
    	return label;
//        return this.entity.label;
    }
    public void setLabel(String val) {
    	this.label = val;
        this.entity.label = val;
    }

//    public boolean getEnabled() {
//        return this.entity.enabled;
//    }
//    public void setEnabled(boolean val) {
//        this.entity.enabled = val;
//        this.enabled = val;
//    }

}

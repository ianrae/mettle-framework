package models;

import java.util.*;
import play.db.ebean.*;
import play.data.validation.Constraints.*;
import javax.persistence.*;

@Entity
public class Task extends Model {    

	@Id
	private Long id;
	public Long getId() 
	{
		return id;
	}
	public void setId(Long val)
	{
		id = val;
	}

	@Required
	private String label;
	public String getLabel() 
	{
		return label;
	}
	public void setLabel(String label) 
	{
		this.label = label;
	}

	public static Finder<Long,Task> find = new Finder(
			Long.class, Task.class
			);  

	public static List<Task> all() {
		return find.all();
	}

	public static void create(Task task) {
		task.save();
	}

	public static void delete(Long id) {
		find.ref(id).delete();
	}

	//--add all getters and setters to avoid known conflicts between EBEAN bytecode-gen and Play bytecode-gen


}
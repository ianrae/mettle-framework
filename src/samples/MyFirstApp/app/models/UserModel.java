package models;

import java.util.*;
import play.db.ebean.*;
import play.data.validation.Constraints.*;
import javax.persistence.*;

import mef.entities.User;

@Entity
public class UserModel extends Model 
{    
	@Transient
	public User entity = new User(); //needed else get illegalStateException
	
	@Id
	private Long id;
	public Long getId() 
	{
		return id;
	}
	public void setId(Long val)
	{
		id = val;
        this.entity.id = val;
	}

	@Required
	private String name;
	public String getName() 
	{
		return name;
	}
	public void setName(String val) 
	{
		this.name = val;
        this.entity.name = val;
	}

	public static Finder<Long,UserModel> find = new Finder(
			Long.class, UserModel.class
			);  

	public static List<UserModel> all() {
		return find.all();
	}

//	public static void create(UserModel task) {
//		task.save();
//	}

	public static void delete(Long id) {
		find.ref(id).delete();
	}

	//--add all getters and setters to avoid known conflicts between EBEAN bytecode-gen and Play bytecode-gen


}
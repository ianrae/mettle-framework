//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.xx

package mef.entities;

import org.mef.framework.entities.Entity;
import mef.gen.*;
import mef.entities.*;
import java.util.Date;
public class User extends Entity
{
	public User()
	{}
	public User(User entity)
	{
		this.id = entity.id;
		this.name = entity.name;
	}
	public User( String name)
	{
		this.name = name;
	}
    public Long id;

    public String name;

}

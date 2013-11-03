//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.
//THIS IS A CUSTOM FILE
package mef.entities;

import org.mef.framework.entities.Entity;
import mef.gen.*;
import mef.entities.*;
import java.util.Date;
import org.mef.framework.auth.AuthRole;
import org.mef.framework.auth.AuthTicket;
public class User extends Entity
{
	public User()
	{}


	public User( String name)
	{
		this.name = name;
	}

	public User(User entity)
	{
		this.id = entity.id;
		this.name = entity.name;
	}
    public Long id;

    public String name;

}

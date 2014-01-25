//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.entities;

import mef.gen.User_GEN;

import org.mef.framework.entities.Entity;
import java.util.Date;

public class User extends User_GEN
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




}

//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package org.mef.framework.auth;

import org.mef.framework.entities.Entity;


public class AuthRole extends Entity
{
	public AuthRole()
	{}


	public AuthRole( String name)
	{
		this.name = name;
	}

	public AuthRole(AuthRole entity)
	{
		this.id = entity.id;
		this.name = entity.name;
	}
    public Long id;

    public String name;

}

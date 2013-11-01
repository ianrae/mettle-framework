//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package org.mef.framework.auth;

import org.mef.framework.entities.Entity;

public class AuthSubject extends Entity
{
	public AuthSubject()
	{}


	public AuthSubject( String name)
	{
		this.name = name;
	}

	public AuthSubject(AuthSubject entity)
	{
		this.id = entity.id;
		this.name = entity.name;
	}
    public Long id;

    public String name;

}

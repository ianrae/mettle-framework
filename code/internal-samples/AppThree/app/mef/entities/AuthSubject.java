//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.entities;

import org.mef.framework.entities.Entity;
import mef.gen.*;
import mef.entities.*;
import java.util.Date;
public class AuthSubject extends Entity
{
	public AuthSubject()
	{}


	public AuthSubject( String name, Long userId)
	{
		this.name = name;
		this.userId = userId;
	}

	public AuthSubject(AuthSubject entity)
	{
		this.id = entity.id;
		this.name = entity.name;
		this.userId = entity.userId;
	}
    public Long id;

    public String name;

    public Long userId;

}
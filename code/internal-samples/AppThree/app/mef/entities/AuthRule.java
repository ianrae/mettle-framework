//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.entities;

import org.mef.framework.entities.Entity;
import mef.gen.*;
import mef.entities.*;
import java.util.Date;
public class AuthRule extends Entity
{
	public AuthRule()
	{}


	public AuthRule( User user, AuthRole role, Ticket ticket)
	{
		this.user = user;
		this.role = role;
		this.ticket = ticket;
	}

	public AuthRule(AuthRule entity)
	{
		this.id = entity.id;
		this.user = entity.user;
		this.role = entity.role;
		this.ticket = entity.ticket;
	}
    public Long id;

    public User user;

    public AuthRole role;

    public Ticket ticket;

}

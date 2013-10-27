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


	public AuthRule( User user, Long roleId, Long ticketId)
	{
		this.user = user;
		this.roleId = roleId;
		this.ticketId = ticketId;
	}

	public AuthRule(AuthRule entity)
	{
		this.id = entity.id;
		this.user = entity.user;
		this.roleId = entity.roleId;
		this.ticketId = entity.ticketId;
	}
    public Long id;

    public User user;

    public Long roleId;

    public Long ticketId;

}

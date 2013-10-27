//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.entities;

import org.mef.framework.entities.Entity;
import mef.gen.*;
import mef.entities.*;
import java.util.Date;
public class AuthRules extends Entity
{
	public AuthRules()
	{}


	public AuthRules( Long userId, Long roleId, Long ticketId)
	{
		this.userId = userId;
		this.roleId = roleId;
		this.ticketId = ticketId;
	}

	public AuthRules(AuthRules entity)
	{
		this.id = entity.id;
		this.userId = entity.userId;
		this.roleId = entity.roleId;
		this.ticketId = entity.ticketId;
	}
    public Long id;

    public Long userId;

    public Long roleId;

    public Long ticketId;

}

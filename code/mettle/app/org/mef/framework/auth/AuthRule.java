//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package org.mef.framework.auth;

import org.mef.framework.entities.Entity;

public class AuthRule extends Entity
{
	public AuthRule()
	{}


	public AuthRule( AuthSubject subject, AuthRole role, AuthTicket ticket)
	{
		this.subject = subject;
		this.role = role;
		this.ticket = ticket;
	}

	public AuthRule(AuthRule entity)
	{
		this.id = entity.id;
		this.subject = entity.subject;
		this.role = entity.role;
		this.ticket = entity.ticket;
	}
    public Long id;

    public AuthSubject subject;

    public AuthRole role;

    public AuthTicket ticket;

}

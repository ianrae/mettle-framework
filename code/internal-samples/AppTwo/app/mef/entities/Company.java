//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.entities;

import org.mef.framework.entities.Entity;
import mef.gen.*;
import mef.entities.*;
import java.util.Date;
public class Company extends Entity
{
	public Company()
	{}

	public Company( String name)
	{
		this.name = name;
	}

	public Company(Company entity)
	{
		this.id = entity.id;
		this.name = entity.name;
	}
    public Long id;

    public String name;

}

//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.xx

package mef.entities;

import org.mef.framework.entities.Entity;
import mef.gen.*;
import mef.entities.*;
import java.util.Date;
public class Computer extends Entity
{
	public Computer()
	{}

	public Computer( String name, Date introduced)
	{
		this.name = name;
		this.introduced = introduced;
	}
    public Long id;

    public String name;

    public Date introduced;

}

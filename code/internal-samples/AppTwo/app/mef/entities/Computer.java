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

	public Computer( String name, Date introduced, Date discontinued, Company company)
	{
		this.name = name;
		this.introduced = introduced;
		this.discontinued = discontinued;
		this.company = company;
	}
    public Long id;

    public String name;

    public Date introduced;

    public Date discontinued;

    public Company company;

}

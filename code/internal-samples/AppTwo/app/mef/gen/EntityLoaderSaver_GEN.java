//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.gen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import mef.entities.*;
import mef.daos.*;
import java.util.Date;



public class EntityLoaderSaver_GEN 
{


	public static long saveOrUpdate(User obj, User existing, IUserDAO dao)
	{
		if (existing != null)
		{
			obj.id = existing.id;
			//copy everything 
						existing.name = obj.name;
			

			dao.update(existing); //inserts or updates 
		}
		else
		{
			obj.id = 0L;
			dao.save(obj); //inserts or updates 
		}
		return obj.id;
	}
	public static long saveOrUpdate(Company obj, Company existing, ICompanyDAO dao)
	{
		if (existing != null)
		{
			obj.id = existing.id;
			//copy everything 
						existing.name = obj.name;
			

			dao.update(existing); //inserts or updates 
		}
		else
		{
			obj.id = 0L;
			dao.save(obj); //inserts or updates 
		}
		return obj.id;
	}
	public static long saveOrUpdate(Computer obj, Computer existing, IComputerDAO dao)
	{
		if (existing != null)
		{
			obj.id = existing.id;
			//copy everything 
						existing.name = obj.name;

						existing.introduced = obj.introduced;

						existing.discontinued = obj.discontinued;

						existing.company = obj.company;
			

			dao.update(existing); //inserts or updates 
		}
		else
		{
			obj.id = 0L;
			dao.save(obj); //inserts or updates 
		}
		return obj.id;
	}
	public static long saveOrUpdate(Role obj, Role existing, IRoleDAO dao)
	{
		if (existing != null)
		{
			obj.id = existing.id;
			//copy everything 
						existing.name = obj.name;
			

			dao.update(existing); //inserts or updates 
		}
		else
		{
			obj.id = 0L;
			dao.save(obj); //inserts or updates 
		}
		return obj.id;
	}
}

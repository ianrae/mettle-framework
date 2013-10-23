package mef.gen;

import mef.daos.ICompanyDAO;
import mef.daos.IComputerDAO;
import mef.daos.IRoleDAO;
import mef.entities.Company;
import mef.entities.Computer;
import mef.entities.Role;

public class EntityLoaderSaver_GEN 
{
	public static long saveOrUpdate(Company obj, Company existing, ICompanyDAO dao)
	{
		if (existing != null)
		{
			obj.id = existing.id;
			//copy everything except name and id
			//nothing
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
			//copy everything except name and id
			//nothing
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
			//copy everything except name and id
			existing.company = obj.company;
			existing.discontinued = obj.discontinued;
			existing.introduced = obj.introduced;
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

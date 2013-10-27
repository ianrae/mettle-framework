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
	public static long saveOrUpdate(Ticket obj, Ticket existing, ITicketDAO dao)
	{
		if (existing != null)
		{
			obj.id = existing.id;
			//copy everything 
			

			dao.update(existing); //inserts or updates 
		}
		else
		{
			obj.id = 0L;
			dao.save(obj); //inserts or updates 
		}
		return obj.id;
	}
	public static long saveOrUpdate(AuthRule obj, AuthRule existing, IAuthRuleDAO dao)
	{
		if (existing != null)
		{
			obj.id = existing.id;
			//copy everything 
						existing.user = obj.user;

						existing.role = obj.role;

						existing.ticket = obj.ticket;
			

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

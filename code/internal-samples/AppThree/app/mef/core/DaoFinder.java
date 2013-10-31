package mef.core;

import mef.daos.IAuthRoleDAO;
import mef.daos.IUserDAO;

public class DaoFinder 
{
	public static IUserDAO getUserDao()
	{
		IUserDAO dao = (IUserDAO) Initializer.getDAO(IUserDAO.class);
		return dao;
	}

	public static IAuthRoleDAO getAuthRoleDao()
	{
		IAuthRoleDAO dao = (IAuthRoleDAO) Initializer.getDAO(IAuthRoleDAO.class);
		return dao;
	}
}

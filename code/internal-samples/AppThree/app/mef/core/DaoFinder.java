//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.core;

import java.util.ArrayList;
import java.util.List;
import org.mef.framework.dao.IDAO;
import mef.daos.*;
import mef.daos.mocks.*;
import boundaries.daos.*;
import org.mef.framework.sfx.SfxContext;
import java.util.Date;
import org.mef.framework.auth.AuthRole;
import org.mef.framework.auth.AuthTicket;

public class DaoFinder  
{
    	public static IUserDAO getUserDao()
	{
		IUserDAO dao = (IUserDAO) Initializer.getDAO(IUserDAO.class);
		return dao;
	}
	public static IBlogDAO getBlogDao()
	{
		IBlogDAO dao = (IBlogDAO) Initializer.getDAO(IBlogDAO.class);
		return dao;
	}
	public static IAuthSubjectDAO getAuthSubjectDao()
	{
		IAuthSubjectDAO dao = (IAuthSubjectDAO) Initializer.getDAO(IAuthSubjectDAO.class);
		return dao;
	}
	public static IAuthRoleDAO getAuthRoleDao()
	{
		IAuthRoleDAO dao = (IAuthRoleDAO) Initializer.getDAO(IAuthRoleDAO.class);
		return dao;
	}
	public static IAuthTicketDAO getAuthTicketDao()
	{
		IAuthTicketDAO dao = (IAuthTicketDAO) Initializer.getDAO(IAuthTicketDAO.class);
		return dao;
	}
	public static IAuthRuleDAO getAuthRuleDao()
	{
		IAuthRuleDAO dao = (IAuthRuleDAO) Initializer.getDAO(IAuthRuleDAO.class);
		return dao;
	}
}

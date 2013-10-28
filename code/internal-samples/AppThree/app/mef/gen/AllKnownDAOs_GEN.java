//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.gen;

import java.util.ArrayList;
import java.util.List;
import org.mef.framework.dao.IDAO;
import mef.daos.*;
import mef.daos.mocks.*;
import boundaries.daos.*;
import org.mef.framework.sfx.SfxContext;
import java.util.Date;

public class AllKnownDAOs_GEN  
{
public List<IDAO> registerDAOs(SfxContext ctx, boolean createMocks)
{
	ArrayList<IDAO> L = new ArrayList<IDAO>();
    if (createMocks)
{
	IAuthUserDAO dal = new MockAuthUserDAO();
	ctx.getServiceLocator().registerSingleton(IAuthUserDAO.class, dal);
	L.add(dal);
}
else
{
	IAuthUserDAO dal = new AuthUserDAO();
	ctx.getServiceLocator().registerSingleton(IAuthUserDAO.class, dal);
	L.add(dal);
}	if (createMocks)
{
	IAuthRoleDAO dal = new MockAuthRoleDAO();
	ctx.getServiceLocator().registerSingleton(IAuthRoleDAO.class, dal);
	L.add(dal);
}
else
{
	IAuthRoleDAO dal = new AuthRoleDAO();
	ctx.getServiceLocator().registerSingleton(IAuthRoleDAO.class, dal);
	L.add(dal);
}	if (createMocks)
{
	IAuthTicketDAO dal = new MockAuthTicketDAO();
	ctx.getServiceLocator().registerSingleton(IAuthTicketDAO.class, dal);
	L.add(dal);
}
else
{
	IAuthTicketDAO dal = new AuthTicketDAO();
	ctx.getServiceLocator().registerSingleton(IAuthTicketDAO.class, dal);
	L.add(dal);
}	if (createMocks)
{
	IAuthRuleDAO dal = new MockAuthRuleDAO();
	ctx.getServiceLocator().registerSingleton(IAuthRuleDAO.class, dal);
	L.add(dal);
}
else
{
	IAuthRuleDAO dal = new AuthRuleDAO();
	ctx.getServiceLocator().registerSingleton(IAuthRuleDAO.class, dal);
	L.add(dal);
}	
	return L;
}
}

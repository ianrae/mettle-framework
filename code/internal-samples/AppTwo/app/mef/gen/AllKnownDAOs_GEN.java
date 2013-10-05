//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.gen;

import java.util.ArrayList;
import java.util.List;
import org.mef.framework.dao.IDAO;
import mef.daos.*;
import mef.daos.mocks.*;
import boundaries.daos.*;
import org.mef.framework.sfx.SfxContext;

public class AllKnownDAOs_GEN  
{
public List<IDAO> registerDAOs(SfxContext ctx, boolean createMocks)
{
	ArrayList<IDAO> L = new ArrayList<IDAO>();
    if (createMocks)
{
	IUserDAO dal = new MockUserDAO();
	ctx.getServiceLocator().registerSingleton(IUserDAO.class, dal);
	L.add(dal);
}
else
{
	IUserDAO dal = new UserDAO();
	ctx.getServiceLocator().registerSingleton(IUserDAO.class, dal);
	L.add(dal);
}	if (createMocks)
{
	ICompanyDAO dal = new MockCompanyDAO();
	ctx.getServiceLocator().registerSingleton(ICompanyDAO.class, dal);
	L.add(dal);
}
else
{
	ICompanyDAO dal = new CompanyDAO();
	ctx.getServiceLocator().registerSingleton(ICompanyDAO.class, dal);
	L.add(dal);
}	if (createMocks)
{
	IComputerDAO dal = new MockComputerDAO();
	ctx.getServiceLocator().registerSingleton(IComputerDAO.class, dal);
	L.add(dal);
}
else
{
	IComputerDAO dal = new ComputerDAO();
	ctx.getServiceLocator().registerSingleton(IComputerDAO.class, dal);
	L.add(dal);
}	
	return L;
}
}

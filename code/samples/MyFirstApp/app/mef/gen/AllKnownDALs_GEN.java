//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.gen;

import java.util.ArrayList;
import java.util.List;
import org.mef.framework.dal.IDAL;
import mef.daos.*;
import mef.daos.mocks.*;
import boundaries.daos.*;
import org.mef.framework.sfx.SfxContext;

public class AllKnownDALs_GEN  
{
public List<IDAL> registerDALs(SfxContext ctx, boolean createMocks)
{
	ArrayList<IDAL> L = new ArrayList<IDAL>();
    if (createMocks)
{
	ITaskDAO dal = new MockTaskDAO();
	ctx.getServiceLocator().registerSingleton(ITaskDAO.class, dal);
	L.add(dal);
}
else
{
	ITaskDAO dal = new TaskDAO();
	ctx.getServiceLocator().registerSingleton(ITaskDAO.class, dal);
	L.add(dal);
}	if (createMocks)
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
	IPhoneDAO dal = new MockPhoneDAO();
	ctx.getServiceLocator().registerSingleton(IPhoneDAO.class, dal);
	L.add(dal);
}
else
{
	IPhoneDAO dal = new PhoneDAO();
	ctx.getServiceLocator().registerSingleton(IPhoneDAO.class, dal);
	L.add(dal);
}	
	return L;
}
}

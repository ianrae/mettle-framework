//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.gen;

import java.util.ArrayList;
import java.util.List;
import org.mef.framework.dal.IDAL;
import mef.dals.*;
import mef.dals.mocks.*;
import boundaries.dals.*;
import org.mef.framework.sfx.SfxContext;

public class AllKnownDALs_GEN  
{
public List<IDAL> registerDALs(SfxContext ctx, boolean createMocks)
{
	ArrayList<IDAL> L = new ArrayList<IDAL>();
    if (createMocks)
{
	ITaskDAL dal = new MockTaskDAL();
	ctx.getServiceLocator().registerSingleton(ITaskDAL.class, dal);
	L.add(dal);
}
else
{
	ITaskDAL dal = new TaskDAL();
	ctx.getServiceLocator().registerSingleton(ITaskDAL.class, dal);
	L.add(dal);
}	if (createMocks)
{
	IUserDAL dal = new MockUserDAL();
	ctx.getServiceLocator().registerSingleton(IUserDAL.class, dal);
	L.add(dal);
}
else
{
	IUserDAL dal = new UserDAL();
	ctx.getServiceLocator().registerSingleton(IUserDAL.class, dal);
	L.add(dal);
}	if (createMocks)
{
	IPhoneDAL dal = new MockPhoneDAL();
	ctx.getServiceLocator().registerSingleton(IPhoneDAL.class, dal);
	L.add(dal);
}
else
{
	IPhoneDAL dal = new PhoneDAL();
	ctx.getServiceLocator().registerSingleton(IPhoneDAL.class, dal);
	L.add(dal);
}	
	return L;
}
}

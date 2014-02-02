//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.gen;

import java.util.ArrayList;
import java.util.List;
import org.mef.framework.dao.IDAO;
import org.mef.framework.fluent.QueryContext;

import mef.daos.*;
import mef.gen.*;

import mef.daos.mocks.*;
import mef.entities.User;
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
	MockUserDAO dal = new MockUserDAO();
	dal.init(ctx);

	ctx.getServiceLocator().registerSingleton(IUserDAO.class, dal);
	L.add(dal);
}
else
{
	IUserDAO dal = new UserDAO();
	ctx.getServiceLocator().registerSingleton(IUserDAO.class, dal);
	L.add(dal);
}	
	return L;
}
}


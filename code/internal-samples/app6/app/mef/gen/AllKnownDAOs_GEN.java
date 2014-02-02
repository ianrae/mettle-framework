//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.gen;

import java.util.ArrayList;
import java.util.List;
import org.mef.framework.dao.IDAO;
import mef.daos.*;
import mef.gen.*;

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
	IUserDAO dao = new MockUserDAO();
	ctx.getServiceLocator().registerSingleton(IUserDAO.class, dao);
	L.add(dao);
	dao.init(ctx);
}
else
{
	IUserDAO dao = new UserDAO();
	ctx.getServiceLocator().registerSingleton(IUserDAO.class, dao);
	L.add(dao);
}	

	return L;
}
}


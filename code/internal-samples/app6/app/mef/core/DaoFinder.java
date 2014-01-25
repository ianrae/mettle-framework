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

public class DaoFinder  
{
    	public static IUserDAO getUserDao()
	{
		IUserDAO dao = (IUserDAO) MettleInitializer.getDAO(IUserDAO.class);
		return dao;
	}
}

package mef;

import static org.junit.Assert.*;

import mef.core.Initializer;
import mef.daos.IAuthRuleDAO;
import mef.daos.IAuthRoleDAO;
import mef.daos.IAuthTicketDAO;
import mef.daos.IAuthSubjectDAO;
import mef.daos.IUserDAO;
import mef.daos.mocks.MockAuthRuleDAO;
import mef.daos.mocks.MockAuthRoleDAO;
import mef.daos.mocks.MockAuthTicketDAO;
import mef.daos.mocks.MockAuthSubjectDAO;
import mef.daos.mocks.MockUserDAO;
import mef.entities.AuthRule;
import mef.entities.AuthRole;
import mef.entities.AuthTicket;
import mef.entities.AuthSubject;

import org.junit.Before;
import org.junit.Test;
import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;

public class UserTests extends BaseTest
{

	@Test
	public void test() 
	{
		init();
		assertNotNull(_userDao);
		Initializer.loadSeedData(Initializer.theCtx);
		
		assertEquals(1, _userDao.size());
		assertEquals("admin", _userDao.find_by_name("admin").name);
	}

	
	//--- helpers ---
	private MockUserDAO _userDao;
	
	
	@Before
	public void init()
	{
		super.init();
		_userDao = (MockUserDAO) Initializer.getDAO(IUserDAO.class);
	}
	

}

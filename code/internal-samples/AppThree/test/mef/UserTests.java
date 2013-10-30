package mef;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import mef.core.Initializer;
import mef.daos.IAuthTicketDAO;
import mef.daos.IUserDAO;
import mef.daos.mocks.MockAuthTicketDAO;
import mef.daos.mocks.MockUserDAO;

import org.junit.Before;
import org.junit.Test;

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
		
		assertEquals(1, _ticketDao.size());
	}

	
	//--- helpers ---
	private MockUserDAO _userDao;
	private MockAuthTicketDAO _ticketDao;
	
	
	@Before
	public void init()
	{
		super.init();
		_userDao = (MockUserDAO) Initializer.getDAO(IUserDAO.class);
		_ticketDao = (MockAuthTicketDAO) Initializer.getDAO(IAuthTicketDAO.class);
	}
	

}

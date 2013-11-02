package mef;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import mef.core.DaoFinder;
import mef.core.Initializer;
import mef.daos.IAuthRoleDAO;
import mef.daos.IAuthRuleDAO;
import mef.daos.IAuthSubjectDAO;
import mef.daos.IAuthTicketDAO;
import mef.daos.IUserDAO;
import mef.daos.mocks.MockAuthRoleDAO;
import mef.daos.mocks.MockAuthRuleDAO;
import mef.daos.mocks.MockAuthSubjectDAO;
import mef.daos.mocks.MockAuthTicketDAO;
import mef.daos.mocks.MockUserDAO;
import mef.entities.AuthRule;

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
		
		assertEquals(1, _roleDao.size());
		assertEquals(1, _ruleDao.size());
		assertEquals(1, _subjectDao.size());
		assertEquals("Admin", _subjectDao.all().get(0).name);
		
		AuthRule rule = _ruleDao.all().get(0);
		assertEquals("Admin", rule.subject.name);
		assertEquals("Full", rule.role.name);
		assertEquals(null, rule.ticket);
	}

	
	//--- helpers ---
	private MockUserDAO _userDao;
	private MockAuthTicketDAO _ticketDao;
	private MockAuthRoleDAO _roleDao;
	private MockAuthRuleDAO _ruleDao;
	private MockAuthSubjectDAO _subjectDao;
	
	
	@Before
	public void init()
	{
		super.init();
		_roleDao = (MockAuthRoleDAO) DaoFinder.getAuthRoleDao();
		_subjectDao = (MockAuthSubjectDAO) DaoFinder.getAuthSubjectDao();
		
		_ticketDao = (MockAuthTicketDAO) DaoFinder.getAuthTicketDao();
		_ruleDao = (MockAuthRuleDAO) DaoFinder.getAuthRuleDao();
		_userDao = (MockUserDAO) DaoFinder.getUserDao();
	}
	

}

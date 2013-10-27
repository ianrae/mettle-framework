package mef;

import static org.junit.Assert.*;

import mef.core.Initializer;
import mef.daos.IAuthRuleDAO;
import mef.daos.IRoleDAO;
import mef.daos.ITicketDAO;
import mef.daos.IUserDAO;
import mef.daos.mocks.MockAuthRuleDAO;
import mef.daos.mocks.MockRoleDAO;
import mef.daos.mocks.MockTicketDAO;
import mef.daos.mocks.MockUserDAO;
import mef.entities.AuthRule;
import mef.entities.Role;
import mef.entities.User;

import org.junit.Before;
import org.junit.Test;

public class RoleTests extends BaseTest
{

	@Test
	public void test() 
	{
		init();
		IRoleDAO dao = this.getDAO();
		assertNotNull(dao);
		assertNotNull(_ticketDao);
		assertNotNull(_ruleDao);
		
		buildRoles();
	}

	@Test
	public void testAuthRules() 
	{
		init();
		buildRoles();
		buildUsers();
		Role role = _roleDao.find_by_name("Viewer");
		User u = _userDao.find_by_name("alice");
		
		AuthRule rule = new AuthRule(u.id, role.id, null);
		_ruleDao.save(rule);
		
	}

	
	//--- helpers ---
	private void buildRoles()
	{
		Role role = new Role("Viewer");
		_roleDao.save(role);
		role = new Role("Viewer");
		_roleDao.save(role);
		
		assertEquals(2, _roleDao.size());
	}
	
	private void buildUsers()
	{
		User u = new User("alice");
		_userDao.save(u);
		u = new User("bob");
		_userDao.save(u);
		
		assertEquals(2, _userDao.size());
	}
	
	private MockRoleDAO _roleDao;
	private MockTicketDAO _ticketDao;
	private MockAuthRuleDAO _ruleDao;
	private MockUserDAO _userDao;
	
	
	@Before
	public void init()
	{
		super.init();
		_roleDao = getDAO();
		_ticketDao = (MockTicketDAO) Initializer.getDAO(ITicketDAO.class);
		_ruleDao = (MockAuthRuleDAO) Initializer.getDAO(IAuthRuleDAO.class);
		_userDao = (MockUserDAO) Initializer.getDAO(IUserDAO.class);
	}
	
	private MockRoleDAO getDAO()
	{
		MockRoleDAO dal = (MockRoleDAO) Initializer.getDAO(IRoleDAO.class); 
		return dal;
	}
	
}

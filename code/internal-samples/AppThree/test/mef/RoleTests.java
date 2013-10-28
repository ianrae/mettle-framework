package mef;

import static org.junit.Assert.*;

import mef.core.Initializer;
import mef.daos.IAuthRuleDAO;
import mef.daos.IAuthRoleDAO;
import mef.daos.IAuthTicketDAO;
import mef.daos.IUserDAO;
import mef.daos.mocks.MockAuthRuleDAO;
import mef.daos.mocks.MockAuthRoleDAO;
import mef.daos.mocks.MockAuthTicketDAO;
import mef.daos.mocks.MockUserDAO;
import mef.entities.AuthRule;
import mef.entities.AuthRole;
import mef.entities.AuthTicket;
import mef.entities.User;

import org.junit.Before;
import org.junit.Test;
import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;

public class RoleTests extends BaseTest
{
	public interface IAuthorizer
	{
		boolean isAuth(User u, AuthRole role, AuthTicket ticket);
	}
	
	public static class MyAuthorizer extends SfxBaseObj implements IAuthorizer
	{
		private static IAuthRoleDAO _roleDao;
		private static IAuthTicketDAO _ticketDao;
		private static IAuthRuleDAO _ruleDao;
		private static IUserDAO _userDao;
		
		public MyAuthorizer(SfxContext ctx)
		{
			super(ctx);
			_roleDao = (IAuthRoleDAO) Initializer.getDAO(IAuthRoleDAO.class);
			_ticketDao = (IAuthTicketDAO) Initializer.getDAO(IAuthTicketDAO.class);
			_ruleDao = (IAuthRuleDAO) Initializer.getDAO(IAuthRuleDAO.class);
			_userDao = (IUserDAO) Initializer.getDAO(IUserDAO.class);
		}
		
		@Override
		public boolean isAuth(User u, AuthRole role, AuthTicket ticket) 
		{
			AuthRule rule = _ruleDao.find_by_user_and_role_and_ticket(u, role, ticket);
			
			return (rule != null);
		}
		
	}

	@Test
	public void test() 
	{
		init();
		IAuthRoleDAO dao = this.getDAO();
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
		buildTickets();
		AuthRole role = _roleDao.find_by_name("Viewer");
		User u = _userDao.find_by_name("alice");
		AuthTicket t = _ticketDao.findById(1L);
		assertNotNull(t);
		
		AuthRule rule = new AuthRule(u, role, t);
		_ruleDao.save(rule);
		
		MyAuthorizer auth = new MyAuthorizer(_ctx);
		assertFalse(auth.isAuth(null, null, null));

		assertTrue(auth.isAuth(u, role, t));
		
		User u2 = _userDao.find_by_name("bob");
		assertFalse(auth.isAuth(u2, role, t));
		
		AuthTicket t2 = _ticketDao.findById(2L);
		assertFalse(auth.isAuth(u, role, t2));
		
		
	}

	@Test
	public void testNullUser() 
	{
		init();
		buildRoles();
		buildUsers();
		buildTickets();
		AuthRole role = _roleDao.find_by_name("Viewer");
		AuthTicket t = _ticketDao.findById(1L);
		assertNotNull(t);
		
		AuthRule rule = new AuthRule(null, role, t);
		_ruleDao.save(rule);
		assertEquals(1, _ruleDao.size());
		
		MyAuthorizer auth = new MyAuthorizer(_ctx);
		assertFalse(auth.isAuth(null, null, null));

		assertTrue(auth.isAuth(null, role, t));
		assertFalse(auth.isAuth(null, role, null));
		
		User u2 = _userDao.find_by_name("bob");
		assertFalse(auth.isAuth(u2, role, t));
		
	}
	
	//--- helpers ---
	private void buildRoles()
	{
		AuthRole role = new AuthRole("Viewer");
		_roleDao.save(role);
		role = new AuthRole("Viewer");
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
	
	private void buildTickets()
	{
		AuthTicket t = new AuthTicket();
		_ticketDao.save(t);
		assertEquals(1, _ticketDao.size());
	}
	
	private MockAuthRoleDAO _roleDao;
	private MockAuthTicketDAO _ticketDao;
	private MockAuthRuleDAO _ruleDao;
	private MockUserDAO _userDao;
	
	
	@Before
	public void init()
	{
		super.init();
		_roleDao = getDAO();
		_ticketDao = (MockAuthTicketDAO) Initializer.getDAO(IAuthTicketDAO.class);
		_ruleDao = (MockAuthRuleDAO) Initializer.getDAO(IAuthRuleDAO.class);
		_userDao = (MockUserDAO) Initializer.getDAO(IUserDAO.class);
	}
	
	private MockAuthRoleDAO getDAO()
	{
		MockAuthRoleDAO dal = (MockAuthRoleDAO) Initializer.getDAO(IAuthRoleDAO.class); 
		return dal;
	}
	
}

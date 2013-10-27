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
import mef.entities.Ticket;
import mef.entities.User;

import org.junit.Before;
import org.junit.Test;
import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;

public class RoleTests extends BaseTest
{
	public interface IAuthorizer
	{
		boolean isAuth(User u, Role role, Ticket ticket);
	}
	
	public static class MyAuthorizer extends SfxBaseObj implements IAuthorizer
	{
		private static IRoleDAO _roleDao;
		private static ITicketDAO _ticketDao;
		private static IAuthRuleDAO _ruleDao;
		private static IUserDAO _userDao;
		
		public MyAuthorizer(SfxContext ctx)
		{
			super(ctx);
			_roleDao = (IRoleDAO) Initializer.getDAO(IRoleDAO.class);
			_ticketDao = (ITicketDAO) Initializer.getDAO(ITicketDAO.class);
			_ruleDao = (IAuthRuleDAO) Initializer.getDAO(IAuthRuleDAO.class);
			_userDao = (IUserDAO) Initializer.getDAO(IUserDAO.class);
		}
		
		@Override
		public boolean isAuth(User u, Role role, Ticket ticket) 
		{
			AuthRule rule = _ruleDao.find_by_user_and_role_and_ticket(u, role, ticket);
			
			return (rule != null);
		}
		
	}

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
		buildTickets();
		Role role = _roleDao.find_by_name("Viewer");
		User u = _userDao.find_by_name("alice");
		Ticket t = _ticketDao.findById(1L);
		assertNotNull(t);
		
		AuthRule rule = new AuthRule(u.id, role.id, t.id);
		_ruleDao.save(rule);
		
		MyAuthorizer auth = new MyAuthorizer(_ctx);
		assertFalse(auth.isAuth(null, null, null));

		assertTrue(auth.isAuth(u, role, t));
		
		User u2 = _userDao.find_by_name("bob");
		assertFalse(auth.isAuth(u2, role, t));
		
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
	
	private void buildTickets()
	{
		Ticket t = new Ticket();
		_ticketDao.save(t);
		assertEquals(1, _ticketDao.size());
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

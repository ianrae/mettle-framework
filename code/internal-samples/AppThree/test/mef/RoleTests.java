package mef;

import static org.junit.Assert.*;

import mef.core.Initializer;
import mef.daos.IAuthRuleDAO;
import mef.daos.IAuthRoleDAO;
import mef.daos.IAuthTicketDAO;
import mef.daos.IAuthUserDAO;
import mef.daos.mocks.MockAuthRuleDAO;
import mef.daos.mocks.MockAuthRoleDAO;
import mef.daos.mocks.MockAuthTicketDAO;
import mef.daos.mocks.MockAuthUserDAO;
import mef.entities.AuthRule;
import mef.entities.AuthRole;
import mef.entities.AuthTicket;
import mef.entities.AuthUser;

import org.junit.Before;
import org.junit.Test;
import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;

public class RoleTests extends BaseTest
{
	public interface IAuthorizer
	{
		void init(IAuthUserDAO userDao, IAuthRoleDAO roleDao, IAuthTicketDAO ticketDao, IAuthRuleDAO ruleDao);
		boolean isAuth(AuthUser u, AuthRole role, AuthTicket ticket);
	}
	
	public static class MyAuthorizer extends SfxBaseObj implements IAuthorizer
	{
		private IAuthRoleDAO _roleDao;
		private IAuthTicketDAO _ticketDao;
		private IAuthRuleDAO _ruleDao;
		private IAuthUserDAO _userDao;
		
		public MyAuthorizer(SfxContext ctx)
		{
			super(ctx);
		}
		
		@Override
		public boolean isAuth(AuthUser u, AuthRole role, AuthTicket ticket) 
		{
			AuthRule rule = _ruleDao.find_by_user_and_role_and_ticket(u, role, ticket);
			return (rule != null);
		}

		@Override
		public void init(IAuthUserDAO userDao, IAuthRoleDAO roleDao,
				IAuthTicketDAO ticketDao, IAuthRuleDAO ruleDao) 
		{
			this._userDao = userDao;
			this._roleDao = roleDao;
			this._ticketDao = ticketDao;
			this._ruleDao = ruleDao;
			
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
		AuthUser u = _userDao.find_by_name("alice");
		AuthTicket t = _ticketDao.findById(1L);
		assertNotNull(t);
		
		AuthRule rule = new AuthRule(u, role, t);
		_ruleDao.save(rule);
		
		MyAuthorizer auth = createAuthorizer();
		assertFalse(auth.isAuth(null, null, null));

		assertTrue(auth.isAuth(u, role, t));
		
		AuthUser u2 = _userDao.find_by_name("bob");
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
		
		MyAuthorizer auth = createAuthorizer();
		assertFalse(auth.isAuth(null, null, null));

		assertTrue(auth.isAuth(null, role, t));
		assertFalse(auth.isAuth(null, role, null));
		
		AuthUser u2 = _userDao.find_by_name("bob");
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
		AuthUser u = new AuthUser("alice");
		_userDao.save(u);
		u = new AuthUser("bob");
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
	private MockAuthUserDAO _userDao;
	
	
	@Before
	public void init()
	{
		super.init();
		_roleDao = getDAO();
		_ticketDao = (MockAuthTicketDAO) Initializer.getDAO(IAuthTicketDAO.class);
		_ruleDao = (MockAuthRuleDAO) Initializer.getDAO(IAuthRuleDAO.class);
		_userDao = (MockAuthUserDAO) Initializer.getDAO(IAuthUserDAO.class);
	}
	
	private MockAuthRoleDAO getDAO()
	{
		MockAuthRoleDAO dal = (MockAuthRoleDAO) Initializer.getDAO(IAuthRoleDAO.class); 
		return dal;
	}
	
	private MyAuthorizer createAuthorizer()
	{
		MyAuthorizer auth = new MyAuthorizer(_ctx);
		auth.init((IAuthUserDAO)Initializer.getDAO(IAuthUserDAO.class), 
				(IAuthRoleDAO)Initializer.getDAO(IAuthRoleDAO.class),
				(IAuthTicketDAO) Initializer.getDAO(IAuthTicketDAO.class),
				(IAuthRuleDAO) Initializer.getDAO(IAuthRuleDAO.class));
		
		return auth;
	}	

}

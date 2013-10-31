package mef;

import static org.junit.Assert.*;

import mef.core.Initializer;
import mef.core.MyAuthorizer;
import mef.daos.IAuthRuleDAO;
import mef.daos.IAuthRoleDAO;
import mef.daos.IAuthTicketDAO;
import mef.daos.IAuthSubjectDAO;
import mef.daos.mocks.MockAuthRuleDAO;
import mef.daos.mocks.MockAuthRoleDAO;
import mef.daos.mocks.MockAuthTicketDAO;
import mef.daos.mocks.MockAuthSubjectDAO;
import mef.entities.AuthRule;
import mef.entities.AuthRole;
import mef.entities.AuthTicket;
import mef.entities.AuthSubject;

import org.junit.Before;
import org.junit.Test;

public class RoleTests extends BaseTest
{
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
		buildSubjects();
		buildTickets();
		AuthRole role = _roleDao.find_by_name("Viewer");
		AuthSubject subj = _userDao.find_by_name("alice");
		AuthTicket t = _ticketDao.findById(1L);
		assertNotNull(t);
		
		AuthRule rule = new AuthRule(subj, role, t);
		_ruleDao.save(rule);
		
		MyAuthorizer auth = createAuthorizer();
		assertFalse(auth.isAuth(null, null, null));

		assertTrue(auth.isAuth(subj, role, t));
		
		AuthSubject subj2 = _userDao.find_by_name("bob");
		assertFalse(auth.isAuth(subj2, role, t));
		
		AuthTicket t2 = _ticketDao.findById(2L);
		assertFalse(auth.isAuth(subj, role, t2));
		
		
	}

	@Test
	public void testNullUser() 
	{
		init();
		buildRoles();
		buildSubjects();
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
		
		AuthSubject subj2 = _userDao.find_by_name("bob");
		assertFalse(auth.isAuth(subj2, role, t));
		
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
	
	private void buildSubjects()
	{
		AuthSubject subj = new AuthSubject("alice");
		_userDao.save(subj);
		subj = new AuthSubject("bob");
		_userDao.save(subj);
		
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
	private MockAuthSubjectDAO _userDao;
	
	
	@Before
	public void init()
	{
		super.init();
		_roleDao = getDAO();
		_ticketDao = (MockAuthTicketDAO) Initializer.getDAO(IAuthTicketDAO.class);
		_ruleDao = (MockAuthRuleDAO) Initializer.getDAO(IAuthRuleDAO.class);
		_userDao = (MockAuthSubjectDAO) Initializer.getDAO(IAuthSubjectDAO.class);
	}
	
	private MockAuthRoleDAO getDAO()
	{
		MockAuthRoleDAO dal = (MockAuthRoleDAO) Initializer.getDAO(IAuthRoleDAO.class); 
		return dal;
	}
	
	private MyAuthorizer createAuthorizer()
	{
		MyAuthorizer auth = new MyAuthorizer(_ctx);
		auth.init((IAuthSubjectDAO)Initializer.getDAO(IAuthSubjectDAO.class), 
				(IAuthRoleDAO)Initializer.getDAO(IAuthRoleDAO.class),
				(IAuthTicketDAO) Initializer.getDAO(IAuthTicketDAO.class),
				(IAuthRuleDAO) Initializer.getDAO(IAuthRuleDAO.class));
		
		return auth;
	}	

}

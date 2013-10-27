package mef;

import static org.junit.Assert.*;

import mef.core.Initializer;
import mef.daos.IRoleDAO;
import mef.daos.ITicketDAO;
import mef.daos.mocks.MockRoleDAO;
import mef.daos.mocks.MockTicketDAO;
import mef.entities.Role;

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
		
		buildRoles();
	}

	
	//--- helpers ---
	private void buildRoles()
	{
		Role role = new Role("Viewer");
		_dao.save(role);
		role = new Role("Viewer");
		_dao.save(role);
		
		assertEquals(2, _dao.size());
	}
	
	
	private MockRoleDAO _dao;
	private MockTicketDAO _ticketDao;
	
	@Before
	public void init()
	{
		super.init();
		_dao = getDAO();
		_ticketDao = (MockTicketDAO) Initializer.getDAO(ITicketDAO.class);
	}
	
	private MockRoleDAO getDAO()
	{
		MockRoleDAO dal = (MockRoleDAO) Initializer.getDAO(IRoleDAO.class); 
		return dal;
	}
	
}

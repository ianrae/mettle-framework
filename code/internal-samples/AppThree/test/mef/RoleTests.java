package mef;

import static org.junit.Assert.*;

import mef.daos.IRoleDAO;
import mef.daos.mocks.MockRoleDAO;
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
	@Before
	public void init()
	{
		super.init();
		_dao = getDAO();
	}
	
	private MockRoleDAO getDAO()
	{
		MockRoleDAO dal = (MockRoleDAO) _ctx.getServiceLocator().getInstance(IRoleDAO.class); 
		return dal;
	}
	
}

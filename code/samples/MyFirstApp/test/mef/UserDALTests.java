package mef;

import static org.junit.Assert.*;

import java.util.List;

import mef.dals.IUserDAL;
import mef.dals.mocks.MockUserDAL;
import mef.entities.User;
import mef.presenters.UserPresenter;

import org.junit.Before;
import org.junit.Test;

public class UserDALTests extends BaseTest
{

	@Test
	public void test() 
	{
		buildDAL();
		assertEquals(2, _dal.size());
		
		List<User> L = _dal.search_by_name("aaaa");
		assertEquals(0, L.size());
		L = _dal.search_by_name("bob");
		assertEquals(1, L.size());
	}
	
	
	//---------- helpers ------------
	private void buildDAL()
	{
		User u = new User();
		Long id = 1L;
		u.id = id++;
		u.name = "bob";
		u.phone = null;
		_dal.save(u);
		
		u = new User();
		u.id = id++;
		u.name = "alice";
		u.phone = null;
		_dal.save(u);
	}
	
	private MockUserDAL _dal;
	@Before
	public void init()
	{
		super.init();
		_dal = getDAL();
	}
	
	private MockUserDAL getDAL()
	{
		MockUserDAL dal = (MockUserDAL) _ctx.getServiceLocator().getInstance(IUserDAL.class); 
		return dal;
	}

}

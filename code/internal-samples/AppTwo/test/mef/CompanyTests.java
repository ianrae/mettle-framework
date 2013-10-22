package mef;

import static org.junit.Assert.*;

import mef.core.Initializer;
import mef.daos.ICompanyDAO;
import mef.entities.Company;

import org.junit.Test;

public class CompanyTests extends BaseTest
{

	@Test
	public void test() 
	{
		Initializer.createContext(true); //don't call init in unit tests
		assertNotNull(Initializer.theCtx);
		
		Initializer.loadSeedData(Initializer.theCtx);
		
		ICompanyDAO dao = (ICompanyDAO) Initializer.getDAO(ICompanyDAO.class);
		assertNotNull(dao);
		assertEquals(42, dao.size());
		
		Company c = new Company("abc");
		dao.save(c);
		assertEquals(43, dao.size());
	}

	@Test(expected = RuntimeException.class)
	public void testDoubleSave() 
	{
		Initializer.createContext(true); //don't call init in unit tests
		assertNotNull(Initializer.theCtx);
		
		Initializer.loadSeedData(Initializer.theCtx);
		
		ICompanyDAO dao = (ICompanyDAO) Initializer.getDAO(ICompanyDAO.class);
		assertNotNull(dao);
		assertEquals(42, dao.size());
		
		Company c = new Company("abc");
		dao.save(c);
		assertEquals(43, dao.size());
		dao.save(c); //throws exception
	}
}

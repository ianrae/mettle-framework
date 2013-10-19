package mef;

import static org.junit.Assert.*;

import mef.core.EntityLoader;
import mef.core.Initializer;
import mef.daos.ICompanyDAO;
import mef.daos.IComputerDAO;
import mef.daos.mocks.MockCompanyDAO;
import mef.daos.mocks.MockComputerDAO;
import mef.entities.Company;
import mef.entities.Computer;

import org.junit.Test;
import org.mef.framework.utils.ResourceReader;

public class JsonTests extends BaseTest
{

	@Test
	public void test() throws Exception
	{
		this.init();

		String json = ResourceReader.readSeedFile("json1.txt");
		EntityLoader loader = new EntityLoader(_ctx);
		loader.loadAll(json);
		
		assertEquals(42, getDAO().size());
		assertEquals(571, getComputerDAO().size());
		
		Computer computer = getComputerDAO().all().get(0);
		assertEquals("MacBook Pro 15.4 inch", computer.name);
		
		Company company = computer.company;
		assertEquals("Apple Inc.", company.name);
	}
	
	private ICompanyDAO getDAO()
	{
		MockCompanyDAO dal = (MockCompanyDAO) Initializer.getDAO(ICompanyDAO.class); 
		return dal;
	}
	
	private IComputerDAO getComputerDAO()
	{
		MockComputerDAO dal = (MockComputerDAO) Initializer.getDAO(IComputerDAO.class); 
		return dal;
	}

}

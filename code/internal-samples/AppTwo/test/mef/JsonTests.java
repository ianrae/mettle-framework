package mef;

import static org.junit.Assert.*;

import mef.core.EntityLoader;
import mef.daos.ICompanyDAO;
import mef.daos.mocks.MockCompanyDAO;

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
	}
	
	private ICompanyDAO getDAO()
	{
		MockCompanyDAO dal = (MockCompanyDAO) _ctx.getServiceLocator().getInstance(ICompanyDAO.class); 
		return dal;
	}
	

}

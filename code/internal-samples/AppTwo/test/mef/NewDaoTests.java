package mef;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import mef.entities.Company;

import org.junit.Test;


public class NewDaoTests {

	public static class NewDao
	{
		List<Company> _L = new ArrayList<Company>();
		
		public NewDao(List<Company> L)
		{
			_L = L;
		}
		
		public int size()
		{
			return _L.size();
		}
		public List<Company> all()
		{
			return _L;
		}
	}
	@Test
	public void test() 
	{
		NewDao dao = new NewDao(buildCompanies());
		assertEquals(3, dao.size());
		assertEquals(3, dao.all().size());
	}
	
	
	
	//------------ helper -------------
	List<Company> buildCompanies()
	{
		ArrayList<Company> L = new ArrayList<Company>();
		
		L.add(new Company("UL900"));
		L.add(new Company("AC710"));
		L.add(new Company("AC900"));
		return L;
	}
	

}

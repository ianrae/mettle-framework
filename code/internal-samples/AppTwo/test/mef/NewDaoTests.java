package mef;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import mef.entities.Company;
import org.junit.Test;
import org.mef.framework.entitydb.EntityDB;
import org.mef.framework.entitydb.IValueMatcher;


public class NewDaoTests extends BaseTest
{

	public static class NewDao
	{
		List<Company> _L = new ArrayList<Company>();
		EntityDB<Company> _entityDB = new EntityDB<Company>();
		
		public NewDao(List<Company> L)
		{
			for(Company cc : L)
			{
				save(cc);
			}
		}
		
		public int size()
		{
			return _L.size();
		}
		public List<Company> all()
		{
			return _L;
		}
		
	    public void save(Company entity) 
	    {
	    	if (entity.id == null)
			{
	    		entity.id = new Long(0L);
	    	}

	        delete(entity.id); //remove existing
	        if (entity.id == 0)
	        {
	        	entity.id = nextAvailIdNumber();
	        }

	         _L.add(entity);
	     }
		public void update(Company entity) 
		{
			this.save(entity);
		}
	    public void delete(long id) 
	    {
	        Company entity = this.findById(id);
	        if (entity != null)
	        {
	            _L.remove(entity);
	        }
	    }
	    public Company findById(long id) 
	    {
	        for(Company entity : _L)
	        {
	            if (entity.id == id)
	            {
	                return entity;
	            }
	        }
	        return null; //not found
	    }


	    private Long nextAvailIdNumber() 
	    {
	    	long used = 0;
	        for(Company entity : _L)
	        {
	            if (entity.id > used)
	            {
	                used = entity.id;
	            }
	        }
	        return used + 1;
		}
		
		
		public Company find_by_name(String name)
		{
			Company entity = _entityDB.findFirstMatch(_L, "name", name);
			return entity;
		}
		public List<Company> find_all_by_name(String name)
		{
			List<Company> list1 = _entityDB.findMatches(_L, "name", name);
			return list1;
		}
		public Company find_by_id(Long id)
		{
			Company entity = _entityDB.findFirstMatch(_L, "id", id);
			return entity;
		}
		public Company find_by_name_and_id(String name, Long id)
		{
			List<Company> list1 = _entityDB.findMatches(_L, "name", name);
			List<Company> list2 = _entityDB.findMatches(_L, "id", id);
			List<Company> list3 = _entityDB.intersection(list1, list2);
			
			if (list3.size() == 0)
			{
				return null;
			}
			else
			{
				return list3.get(0);
			}
		}
		public Company find_like_name(String name)
		{
			Company entity = _entityDB.findFirstMatch(_L, "name", name, IValueMatcher.LIKE);
			return entity;
		}
		public Company find_ilike_name(String name)
		{
			Company entity = _entityDB.findFirstMatch(_L, "name", name, IValueMatcher.ILIKE);
			return entity;
		}
		public List<Company> all_order_by(String fieldName, String orderBy)
		{
			ArrayList<Company> tmpL = new ArrayList<Company>();
			tmpL.addAll(_L);
			List<Company> list1 = tmpL;
			_entityDB.orderBy(list1, fieldName, orderBy, String.class);
			return list1;
		}
	}
	
	
	
	@Test
	public void test() 
	{
		NewDao dao = new NewDao(buildCompanies());
		assertEquals(3, dao.size());
		assertEquals(3, dao.all().size());
		
		Company cc = dao.find_by_name(("AC710"));
		assertEquals("AC710", cc.name);
		cc = dao.find_by_name(("zzzzz"));
		assertEquals(null, cc);
		
		for(Company tmp : dao.all())
		{
			log(String.format("%d: %s", tmp.id, tmp.name));
		}
		
		cc = dao.find_by_id(1L);
		assertEquals("UL900", cc.name);
		cc = dao.find_by_id(99L);
		assertEquals(null, cc);
		
		cc = dao.find_by_name_and_id("AC710", 2L);
		assertEquals("AC710", cc.name);
		
		cc = dao.find_by_name_and_id("AC710", 1L);
		assertEquals(null, cc);
		cc = dao.find_by_name_and_id("UL900", 1L);
		assertEquals(1L, cc.id.longValue());
	}
	
	@Test
	public void testFindAll()
	{
		NewDao dao = new NewDao(buildCompanies());
		assertEquals(3, dao.size());

		List<Company> list1 = dao.find_all_by_name("AC710");
		assertEquals(1, list1.size());
		assertEquals("AC710", list1.get(0).name);
	}
	
	@Test
	public void testFindLike()
	{
		NewDao dao = new NewDao(buildCompanies());
		assertEquals(3, dao.size());

		Company cc = dao.find_like_name("AC%");
		assertEquals("AC710", cc.name);
		cc = dao.find_like_name("ac%");
		assertEquals(null, cc);
		
		cc = dao.find_like_name("%710");
		assertEquals("AC710", cc.name);
		
		cc = dao.find_like_name("%C7%");
		assertEquals("AC710", cc.name);
	}
	@Test
	public void testFindILike()
	{
		NewDao dao = new NewDao(buildCompanies());
		assertEquals(3, dao.size());

		Company cc = dao.find_ilike_name("AC%");
		assertEquals("AC710", cc.name);
		cc = dao.find_ilike_name("ac%");
		assertEquals("AC710", cc.name);
		
		cc = dao.find_ilike_name("%c710");
		assertEquals("AC710", cc.name);
	}
	
	@Test
	public void testFindAllOrderBy()
	{
		NewDao dao = new NewDao(buildCompanies());
		assertEquals(3, dao.size());

		List<Company> list1 = dao.all_order_by("name", "asc");
		assertEquals(3, list1.size());
		assertEquals("AC710", list1.get(0).name);
		chkL(list1, "AC710", "AC900", "UL900");

		list1 = dao.all_order_by("name", "desc");
		assertEquals(3, list1.size());
		chkL(list1, "UL900", "AC900", "AC710");
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
	
	private void chkL(List<Company> L, String val0, String val1, String val2)
	{
		assertEquals(val0, L.get(0).name);
		assertEquals(val1, L.get(1).name);
		assertEquals(val2, L.get(2).name);
	}
}

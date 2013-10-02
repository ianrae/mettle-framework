import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;


public class BeanDBTests 
{
	public static class Flight
	{
		String flight;
		String model;
		Integer num;
		int  nVal;
		
		public Flight(String flight, String model, Integer num)
		{
			this.flight = flight;
			this.model = model;
			this.num = num;
			this.nVal = num + 100;
		}
	}
	
	
	//Whole idea is we don't need a fully emulated sql db like H2.
	//(a)we are dealing with objects (which can be assumed to be fully eagerly loaded)
	//(b)Mock DAL has the actual objects, and doesn't make copies, so object instances
	//   are unique. Never would get two object with same .id.
	
	public static class EntityDB
	{
		//hmm should union just work in whole objects. If same object (Flight 55) in both
		//lists, shouldn't result only be in result once!!
		private List<Flight> union(List<Flight> L, List<Flight> L2) 
		{
			ArrayList<Flight> L3 = new ArrayList<BeanDBTests.Flight>();
			for(Flight f : L)
			{
				L3.add(f);
			}
			for(Flight f : L2)
			{
				if (! L3.contains(f))
				{
					L3.add(f);
				}
			}
			
			return L3;
		}
		
		private List<Flight> intersection(List<Flight> L, List<Flight> L2) 
		{
			ArrayList<Flight> L3 = new ArrayList<BeanDBTests.Flight>();
			for(Flight f : L)
			{
				if (L2.contains(f))
				{
					L3.add(f);
				}
			}
			
			return L3;
		}
		
		public boolean isMatchStr(Flight obj, String fieldName, String valueToMatch) 
		{
			if (fieldName == null)
			{
				return false;
			}
			
			Object value = getFieldValue(obj, fieldName);
			if (value == null)
			{
				return false;
			}
			String s = value.toString();
			return (s.equalsIgnoreCase(valueToMatch));
		}
		public boolean isMatchInt(Flight obj, String fieldName, Integer valueToMatch) 
		{
			if (fieldName == null)
			{
				return false;
			}
			
			Object value = getFieldValue(obj, fieldName);
			if (value == null)
			{
				return false;
			}
			
			if (value instanceof Integer)
			{
				Integer n = (Integer)value;
				return (n.compareTo(valueToMatch) == 0);
			}
			else
			{
				return false;
			}
		}


		public Object getFieldValue(Flight obj, String fieldName) 
		{
			if (fieldName == null)
			{
				return false;
			}
			
			Field field = null;
			Object value = null;
			try 
			{
				field = obj.getClass().getDeclaredField(fieldName);
				field.setAccessible(true);
				value = field.get(obj);
			}
			catch (SecurityException e) 
			{
				// TODO Auto-generated catch block
//				e.printStackTrace();
			} 
			catch (NoSuchFieldException e) 
			{
				// TODO Auto-generated catch block
//				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			}

			return value;
		}
		
		public Flight findFirstMatch(List<Flight> L, String fieldName, String valueToMatch) 
		{
			for(Flight f : L)
			{
				if (isMatchStr(f, fieldName, valueToMatch))
				{
					return f;
				}
			}
			return null;
		}
	}
	public static class DBChecker
	{
		public boolean ensureUnique(List<Flight> L)
		{
			HashMap<Flight, String> map = new HashMap<BeanDBTests.Flight, String>();
			for(Flight f : L)
			{
				map.put(f, "1");
			}
			
			return (L.size() == map.size());
		}
		
	}
	public static class Query
	{
		private List<Flight> resultL = new ArrayList<BeanDBTests.Flight>();
		EntityDB db = new EntityDB();
		
		public void add(List<Flight> L)
		{
			resultL.addAll(L);
		}
		
		public int size()
		{
			return resultL.size();
		}
		
		public void union(List<Flight> L)
		{
			resultL = db.union(resultL, L);
		}

		public void intersect(List<Flight> L)
		{
			resultL = db.intersection(resultL, L);
		}
	}
	
	@Test
	public void testOr() 
	{
		log("--testOr--");
		List<Flight> L = this.buildFlights();
		assertEquals(3, L.size());
		List<Flight> L2 = this.buildFlights();
		assertEquals(3, L2.size());
		
		EntityDB db = new EntityDB();
		List<Flight> L3 = db.union(L, L2);
		assertEquals(6, L3.size());
		chkAllUnique(L, L2, L3);
	}

	@Test
	public void testUnique() 
	{
		log("--testUnique--");
		List<Flight> L = this.buildFlights();
		assertEquals(3, L.size());
		L.add(L.get(0)); //add dups
		
		EntityDB db = new EntityDB();
		assertEquals(false, ensureUnique(L));
	}
	
	@Test
	public void testIntersectionNone() 
	{
		log("--testIntersectionNone--");
		List<Flight> L = this.buildFlights();
		List<Flight> L2 = this.buildFlights();
		
		EntityDB db = new EntityDB();
		List<Flight> L3 = db.intersection(L, L2);
		assertEquals(0, L3.size());
		chkAllUnique(L, L2, L3);
	}

	@Test
	public void testIntersectionOne() 
	{
		log("--testIntersectionOne--");
		List<Flight> L = this.buildFlights();
		assertEquals(3, L.size());
		List<Flight> L2 = this.buildFlights();
		assertEquals(3, L2.size());
		L2.add(L.get(0));
		
		EntityDB db = new EntityDB();
		List<Flight> L3 = db.intersection(L, L2);
		assertEquals(1, L3.size());
		chkAllUnique(L, L2, L3);
		assertSame(L.get(0), L3.get(0));
	}

	@Test
	public void testIsMatch() throws Exception
	{
		log("--testIsMatch--");
		List<Flight> L = this.buildFlights();
		
		Flight one = L.get(0);
		EntityDB db = new EntityDB();
		boolean b = db.isMatchStr(one, "flight", "abc");
		assertEquals(false, b);
		assertEquals(true, db.isMatchStr(one, "flight", "UL900"));
		assertEquals(true, db.isMatchStr(one, "flight", "ul900"));
		
		assertEquals(false, db.isMatchStr(one, "flight", ""));
		assertEquals(false, db.isMatchStr(one, "flight", null));
		assertEquals(false, db.isMatchStr(one, "", null));
		assertEquals(false, db.isMatchStr(one, null, null));
	}
	
	@Test
	public void testIsMatchInt() throws Exception
	{
		log("--testIsMatchInt--");
		List<Flight> L = this.buildFlights();
		
		Flight one = L.get(0);
		EntityDB db = new EntityDB();
		boolean b = db.isMatchInt(one, "flight", 14);
		assertEquals(false, b);
		
		//Integer
		assertEquals(false, db.isMatchInt(one, "num", 14));
		assertEquals(true, db.isMatchInt(one, "num", 10));

		//int
		assertEquals(false, db.isMatchInt(one, "nVal", 14));
		assertEquals(true, db.isMatchInt(one, "nVal", 110));
	}
	
	@Test
	public void testFindMatch() throws Exception
	{
		log("--testFindMatch--");
		List<Flight> L = this.buildFlights();
		
		EntityDB db = new EntityDB();
		Flight f = db.findFirstMatch(L, "flight", "abc");
		assertEquals(null, f);
		
		assertSame(L.get(0), db.findFirstMatch(L, "flight", "UL900"));
		assertSame(L.get(1), db.findFirstMatch(L, "flight", "AC710"));
		assertSame(L.get(2), db.findFirstMatch(L, "flight", "AC900"));
	}
	
	@Test
	public void testQueryUnion() throws Exception
	{
		Query q = new Query();
		assertEquals(0, q.size());
		List<Flight> L = this.buildFlights();
		q.add(L);
		assertEquals(3, q.size());
		List<Flight> L2 = this.buildFlights();
		q.union(L2);
		
		assertEquals(6, q.size()); //L2 has different instances (but same values)
	}
	@Test
	public void testQueryUnionRepeatInstances() throws Exception
	{
		Query q = new Query();
		assertEquals(0, q.size());
		List<Flight> L = this.buildFlights();
		q.add(L);
		assertEquals(3, q.size());
		q.union(L); //add same objects again
		
		assertEquals(3, q.size()); 
	}
	@Test
	public void testQueryIntersection() throws Exception
	{
		Query q = new Query();
		assertEquals(0, q.size());
		List<Flight> L = this.buildFlights();
		List<Flight> L2 = new ArrayList<Flight>();
		L2.add(L.get(1));
		
		q.add(L);
		q.intersect(L2);
		
		assertEquals(1, q.size()); //L2 has different instances (but same values)
	}
	
	
	

	//------------- helper functions -------------
	private void log(String s)
	{
		System.out.println(s);
	}
	
	List<Flight> buildFlights()
	{
		ArrayList<Flight> L = new ArrayList<BeanDBTests.Flight>();
		
		L.add(new Flight("UL900", "Spitfire", 10));
		L.add(new Flight("AC710", "Airbus", 11));
		L.add(new Flight("AC900", "Boeing", 12));
		return L;
	}
	private Object ensureUnique(List<Flight> L) 
	{
		DBChecker checker = new DBChecker();
		return checker.ensureUnique(L);
	}
	private void chkAllUnique(List<Flight> L, List<Flight> L2, List<Flight> L3)
	{
		chkUnique(L);
		chkUnique(L2);
		chkUnique(L3);
	}
	private void chkUnique(List<Flight> L)
	{
		assertEquals(true, ensureUnique(L));
	}
	
	
	

}

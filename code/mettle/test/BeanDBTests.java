import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mef.framework.entitydb.DBChecker;
import org.mef.framework.entitydb.EntityDB;
import org.mef.framework.entitydb.IValueMatcher;
import org.mef.framework.entitydb.Query;


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
	
	
	
	@Test
	public void testOr() 
	{
		log("--testOr--");
		List<Flight> L = this.buildFlights();
		assertEquals(3, L.size());
		List<Flight> L2 = this.buildFlights();
		assertEquals(3, L2.size());
		
		EntityDB<Flight> db = new EntityDB<Flight>();
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
		
		assertEquals(false, ensureUnique(L));
	}
	
	@Test
	public void testIntersectionNone() 
	{
		log("--testIntersectionNone--");
		List<Flight> L = this.buildFlights();
		List<Flight> L2 = this.buildFlights();
		
		EntityDB<Flight> db = new EntityDB<Flight>();
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
		
		EntityDB<Flight> db = new EntityDB<Flight>();
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
		EntityDB<Flight> db = new EntityDB<Flight>();
		boolean b = db.isMatchObject(one, "flight", "abc", String.class, IValueMatcher.EXACT);
		assertEquals(false, b);
		assertEquals(true, db.isMatchObject(one, "flight", "UL900", String.class, IValueMatcher.EXACT));
		assertEquals(false, db.isMatchObject(one, "flight", "ul900", String.class, IValueMatcher.EXACT));
		assertEquals(true, db.isMatchObject(one, "flight", "ul900", String.class, IValueMatcher.CASE_INSENSITIVE));
		
		assertEquals(false, db.isMatchObject(one, "flight", "", String.class, IValueMatcher.EXACT));
		assertEquals(false, db.isMatchObject(one, "flight", null, String.class, IValueMatcher.EXACT));
		assertEquals(false, db.isMatchObject(one, "", null, String.class, IValueMatcher.EXACT));
		assertEquals(false, db.isMatchObject(one, null, null, String.class, IValueMatcher.EXACT));
	}
	
	@Test
	public void testIsMatchInt() throws Exception
	{
		log("--testIsMatchInt--");
		List<Flight> L = this.buildFlights();
		
		Flight one = L.get(0);
		EntityDB<Flight> db = new EntityDB<Flight>();
		boolean b = db.isMatchObject(one, "num", 14, Integer.class, IValueMatcher.EXACT);
		assertEquals(false, b);
		
		//Integer
		assertEquals(false, db.isMatchObject(one, "num", 14, Integer.class, IValueMatcher.EXACT));
		assertEquals(true, db.isMatchObject(one, "num", 10, Integer.class, IValueMatcher.EXACT));

		//int
		assertEquals(false, db.isMatchObject(one, "nVal", 14, Integer.class, IValueMatcher.EXACT));
		assertEquals(true, db.isMatchObject(one, "nVal", 110, Integer.class, IValueMatcher.EXACT));
	}
	
	@Test
	public void testFindMatch() throws Exception
	{
		log("--testFindMatch--");
		List<Flight> L = this.buildFlights();
		
		EntityDB<Flight> db = new EntityDB<Flight>();
		Flight f = db.findFirstMatch(L, "flight", "abc");
		assertEquals(null, f);
		
		assertSame(L.get(0), db.findFirstMatch(L, "flight", "UL900"));
		assertSame(L.get(1), db.findFirstMatch(L, "flight", "AC710"));
		assertSame(L.get(2), db.findFirstMatch(L, "flight", "AC900"));
	}
	
	@Test
	public void testFindMatchAll() throws Exception
	{
		log("--testFindMatchAll--");
		List<Flight> L = this.buildFlights();
		
		EntityDB<Flight> db = new EntityDB<Flight>();
		List<Flight> resL = db.findMatches(L, "flight", "abc");
		assertEquals(0, resL.size());
		
		assertSame(L.get(0), db.findFirstMatch(L, "flight", "UL900"));
		assertSame(L.get(1), db.findFirstMatch(L, "flight", "AC710"));
		assertSame(L.get(2), db.findFirstMatch(L, "flight", "AC900"));
	}
	@Test
	public void testQueryUnion() throws Exception
	{
		Query<Flight> q = new Query<Flight>();
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
		Query<Flight> q = new Query<Flight>();
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
		Query<Flight> q = new Query<Flight>();
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
		DBChecker<Flight> checker = new DBChecker<Flight>();
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
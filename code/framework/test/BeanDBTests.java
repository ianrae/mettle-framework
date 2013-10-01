import static org.junit.Assert.*;

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
		
		public Flight(String flight, String model)
		{
			this.flight = flight;
			this.model = model;
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
		
		List<Flight> L3 = union(L, L2);
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
		assertEquals(3, L.size());
		List<Flight> L2 = this.buildFlights();
		assertEquals(3, L2.size());
		
		List<Flight> L3 = this.intersection(L, L2);
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
		
		List<Flight> L3 = this.intersection(L, L2);
		assertEquals(1, L3.size());
		chkAllUnique(L, L2, L3);
		assertSame(L.get(0), L3.get(0));
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
	
	private List<Flight> union(List<Flight> L, List<Flight> L2) 
	{
		ArrayList<Flight> L3 = new ArrayList<BeanDBTests.Flight>();
		for(Flight f : L)
		{
			L3.add(f);
		}
		for(Flight f : L2)
		{
			L3.add(f);
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
	
	private boolean ensureUnique(List<Flight> L)
	{
		HashMap<Flight, String> map = new HashMap<BeanDBTests.Flight, String>();
		for(Flight f : L)
		{
			map.put(f, "1");
		}
		
		return (L.size() == map.size());
	}


	//------------- helper functions -------------
	private void log(String s)
	{
		System.out.println(s);
	}
	
	List<Flight> buildFlights()
	{
		ArrayList<Flight> L = new ArrayList<BeanDBTests.Flight>();
		
		L.add(new Flight("UL900", "Spitfire"));
		L.add(new Flight("AC710", "Airbus"));
		L.add(new Flight("AC900", "Boeing"));
		return L;
		
	}
}

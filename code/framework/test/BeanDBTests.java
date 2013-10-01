import static org.junit.Assert.*;

import java.util.ArrayList;
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

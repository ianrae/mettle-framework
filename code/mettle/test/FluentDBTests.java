import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mef.framework.entities.Entity;
import org.mef.framework.entitydb.EntityDB;

import tools.BaseTest;



public class FluentDBTests extends BaseTest
{
	public static class Hotel extends Entity
	{
		String flight;
		String model;
		Integer num;
		int  nVal;
		
		public Hotel(String flight, String model, Integer num)
		{
			this.flight = flight;
			this.model = model;
			this.num = num;
			this.nVal = num + 100;
		}
	}
	
	
	
	public static class HotelDao
	{
		public List<Hotel> dataL;
//		public List<QueryX> queryL = new ArrayList<QueryX>();
		public FluentTests.QueryContext<Hotel> queryctx = new FluentTests.QueryContext<Hotel>();

		public HotelDao()
		{
			queryctx.queryL = new ArrayList<FluentTests.QStep>();
		}

		public FluentTests.Query1<Hotel> query()
		{
			queryctx.queryL = new ArrayList<FluentTests.QStep>();
			return new FluentTests.Query1<Hotel>(queryctx);
		}

		public void setActionProcessor(FluentTests.IQueryActionProcessor<Hotel> proc) 
		{
			queryctx.proc = proc;
		}
	}
	
	
	public static class MyHotelProc<Hotel> implements FluentTests.IQueryActionProcessor<Hotel>
	{
		private void log(String s)
		{
			System.out.println(s);
		}
		@Override
		public void start(List<FluentTests.QueryAction> actionL) 
		{
			log("start");
		}

		@Override
		public Hotel findOne() //exactly one
		{
			log("findOne");
			return null;
		}

		@Override
		public Hotel findAny() //0 or 1
		{
			log("findAny");
			return null;
		}
		@Override
		public List<Hotel> findMany() 
		{
			log("findMany");
			return new ArrayList<Hotel>();
		}
		@Override
		public long findCount() 
		{
			log("findCount");
			return 0L;
		}

		@Override
		public void processAction(int index, FluentTests.QueryAction action) 
		{
			log(String.format(" %d. %s", index, action.action));
		}
	}


	@Test
	public void testIntersectionNone() 
	{
		log("--testIntersectionNone--");
		List<Hotel> L = this.buildHotels();
		List<Hotel> L2 = this.buildHotels();
		
		EntityDB<Hotel> db = new EntityDB<Hotel>();
		List<Hotel> L3 = db.intersection(L, L2);
		assertEquals(0, L3.size());
	}
	
	@Test
	public void test2()
	{
			HotelDao dao = new HotelDao();
			dao.setActionProcessor(new MyHotelProc());
			
			Hotel h = dao.query().findAny();

			assertNotNull(h);
		
	}

	//--- helpers ---
	List<Hotel> buildHotels()
	{
		ArrayList<Hotel> L = new ArrayList<Hotel>();
		
		L.add(new Hotel("UL900", "Spitfire", 10));
		L.add(new Hotel("AC710", "Airbus", 11));
		L.add(new Hotel("AC900", "Boeing", 12));
		return L;
	}
	
}

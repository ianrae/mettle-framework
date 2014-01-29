import static org.junit.Assert.*;
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
//		public List<Hotel> dataL;
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


	public static class MyHotelProc implements FluentTests.IQueryActionProcessor<Hotel>
	{
		EntityDB<Hotel> db = new EntityDB<Hotel>();
		List<Hotel> dataL;
		List<Hotel> resultL;
		String orderBy;
		String orderAsc; //"asc" or "desc"
		int limit;
		
		public MyHotelProc(List<Hotel> hotelL)
		{
			this.dataL = hotelL;
		}
		
		private void log(String s)
		{
			System.out.println(s);
		}
		@Override
		public void start(List<FluentTests.QueryAction> actionL) 
		{
			resultL = null; //new ArrayList<Hotel>();
			orderBy = null;
			limit = -1;
			log("start");
		}

		@Override
		public Hotel findOne() //exactly one
		{
			log("findOne");
			return null;
		}

		private void initResultLIfNeeded()
		{
			if (resultL == null)
			{
				resultL = db.union(dataL, new ArrayList<Hotel>());
			}
			
			if (orderBy != null)
			{
				resultL = db.orderBy(resultL, orderBy, orderAsc, String.class);
			}
			
			if (limit >= 0)
			{
				if (limit > resultL.size())
				{
					limit = resultL.size();
				}
				resultL = resultL.subList(0, limit);
			}
		}
		@Override
		public Hotel findAny() //0 or 1
		{
			log("findAny");
			initResultLIfNeeded();
			
			if (resultL.size() == 0)
			{
				return null;
			}
			return resultL.get(0);
		}
		@Override
		public List<Hotel> findMany() 
		{
			log("findMany");
			initResultLIfNeeded();
			return resultL;
		}
		@Override
		public long findCount() 
		{
			log("findCount");
			initResultLIfNeeded();
			return resultL.size();
		}

		@Override
		public void processAction(int index, FluentTests.QueryAction qaction) 
		{
			String action = qaction.action;
			
			log(String.format(" %d. %s", index, action));
			
			if (action.equals("ALL"))
			{
				resultL = db.union(dataL, new ArrayList<Hotel>());
			}
			else if (action.equals("WHERE"))
			{
				resultL = db.findMatches(dataL, qaction.fieldName, (String)qaction.obj);
			}
			else if (action.equals("AND"))
			{
				List<Hotel> tmp1 = db.findMatches(dataL, qaction.fieldName, (String)qaction.obj);
				resultL = db.intersection(resultL, tmp1);
			}
			else if (action.equals("OR"))
			{
				List<Hotel> tmp1 = db.findMatches(dataL, qaction.fieldName, (String)qaction.obj);
				resultL = db.union(resultL, tmp1);
			}
			else if (action.equals("ORDERBY"))
			{
				orderBy = qaction.fieldName;
				orderAsc = (String) qaction.obj;
			}
			else if (action.equals("LIMIT"))
			{
				limit = (Integer)qaction.obj;
			}
			else if (action.equals("FETCH"))
			{ //nothing to do
			}
			else
			{
				throw new FluentTests.FluentException("ActionProc: unknown action: " + action);
			}
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
	public void testAnd()
	{
		init();
		String target = "Boeing";
		
		Hotel h = dao.query().where("model").eq(target).and("flight").eq("UL901").findAny();
		assertNotNull(h);
		assertEquals(target, h.model);
		assertEquals("UL901", h.flight);

		log("findManuy..");
		List<Hotel> L = dao.query().where("model").eq(target).and("flight").eq("UL901").findMany();
		assertEquals(1, L.size());
		h = L.get(0);
		assertEquals(target, h.model);
		assertEquals("UL901", h.flight);
		
		long count = dao.query().where("model").eq("Spitfire").and("flight").eq("UL901").findCount();
		assertEquals(1, count);
	}

	@Test
	public void testOr()
	{
		init();
		String target = "Boeing";
		
		Hotel h = dao.query().where("model").eq(target).or("flight").eq("UL901").findAny();
		assertNotNull(h);
		assertEquals(target, h.model);
		assertEquals("UL901", h.flight);

		log("findManuy..");
		List<Hotel> L = dao.query().where("model").eq(target).or("flight").eq("UL901").findMany();
		assertEquals(2, L.size());
		h = L.get(0);
		assertEquals(target, h.model);
		assertEquals("UL901", h.flight);
		h = L.get(1);
		assertEquals("Spitfire", h.model);
		assertEquals("UL901", h.flight);
		
		long count = dao.query().where("model").eq("Spitfire").or("flight").eq("UL901").findCount();
		assertEquals(3, count);
	}

	@Test
	public void testGood()
	{
		init();

		Hotel h = dao.query().where("model").eq("Spitfire").findAny();

		assertNotNull(h);
		assertEquals("Spitfire", h.model);
		assertEquals("UL900", h.flight);

		List<Hotel> L = dao.query().where("model").eq("Spitfire").findMany();
		assertEquals(2, L.size());
		h = L.get(0);
		assertEquals("Spitfire", h.model);
		assertEquals("UL900", h.flight);
		h = L.get(1);
		assertEquals("Spitfire", h.model);
		assertEquals("UL901", h.flight);
		
		long count = dao.query().where("model").eq("Spitfire").findCount();
		assertEquals(2, count);
	}

	@Test
	public void testEmpty()
	{
		init();

		Hotel h = dao.query().where("model").eq("zzz").findAny();
		assertNull(h);

		h = dao.query().where("model").eq("zzz").findUnique();
		assertNull(h);

		List<Hotel> L = dao.query().where("model").eq("zzz").findMany();
		assertEquals(0, L.size());
		
		long count = dao.query().where("model").eq("zzz").findCount();
		assertEquals(0, count);
	}
	
	@Test
	public void test3()
	{
		init();
		Hotel h = dao.query().findAny();
		assertNotNull(h);
		assertEquals("Spitfire", h.model);
		assertEquals("UL900", h.flight);

		log("findMany..");
		List<Hotel> L = dao.query().findMany();
		assertEquals(4, L.size());
		
		log("findCount..");
		long count = dao.query().findCount();
		assertEquals(4, count);
	}

	@Test(expected=FluentTests.FluentException.class)
	public void test4()
	{
		init();
		log("findUnique..");
		Hotel h = dao.query().findUnique();
		assertNull(h);
	}
	
	@Test
	public void testOrderBy()
	{
		init();
		String target = "UL901";
		
		Hotel h = dao.query().orderBy("model").where("flight").eq(target).findAny();
		assertNotNull(h);
		assertEquals(target, h.flight);
		assertEquals("Boeing", h.model);

		Hotel h2 = dao.query().orderBy("model", "asc").where("flight").eq(target).findAny();
		assertTrue(h2 == h);
		
		h = dao.query().orderBy("model", "desc").where("flight").eq(target).findAny();
		assertNotNull(h);
		assertEquals(target, h.flight);
		assertEquals("Spitfire", h.model);
	}

	@Test
	public void testLimit()
	{
		init();
		addMore();
		String target = "UL901";
		
		List<Hotel> L = dao.query().orderBy("model").limit(10).where("flight").eq(target).findMany();
		assertEquals(6, L.size());

		L = dao.query().orderBy("model").limit(3).where("flight").eq(target).findMany();
		assertEquals(3, L.size());
		L = dao.query().orderBy("model").limit(1).where("flight").eq(target).findMany();
		assertEquals(1, L.size());
		L = dao.query().orderBy("model").limit(0).where("flight").eq(target).findMany();
		assertEquals(0, L.size());
	}

	
	//--- helpers ---
	private HotelDao dao;
	List<Hotel> hotelL;
	
	private void init()
	{
		dao = new HotelDao();
		hotelL = this.buildHotels();
		dao.setActionProcessor(new MyHotelProc(hotelL));
	}
	
	List<Hotel> buildHotels()
	{
		ArrayList<Hotel> L = new ArrayList<Hotel>();

		L.add(new Hotel("UL900", "Spitfire", 10));
		L.add(new Hotel("UL901", "Spitfire", 13));
		L.add(new Hotel("AC710", "Airbus", 11));
		L.add(new Hotel("UL901", "Boeing", 12));
		return L;
	}

	private void addMore()
	{
		hotelL.add(new Hotel("UL901", "abc", 10));
		hotelL.add(new Hotel("UL901", "def", 10));
		hotelL.add(new Hotel("UL901", "ghi", 10));
		hotelL.add(new Hotel("UL901", "jkl", 10));
	}
}

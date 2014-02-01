import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mef.framework.binder.IFormBinder;
import org.mef.framework.dao.IDAO;
import org.mef.framework.entities.Entity;
import org.mef.framework.entitydb.EntityDB;
import org.mef.framework.entitydb.IValueMatcher;
import org.mef.framework.fluent.FluentException;
import org.mef.framework.fluent.IQueryActionProcessor;
import org.mef.framework.fluent.QStep;
import org.mef.framework.fluent.Query1;
import org.mef.framework.fluent.QueryAction;
import org.mef.framework.fluent.QueryContext;
import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;

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



	public static class HotelDao implements IDAO
	{
		//		public List<Hotel> dataL;
		public QueryContext<Hotel> queryctx = new QueryContext<Hotel>();
		List<Hotel> dataL;

		public HotelDao(List<Hotel> dataL)
		{
			queryctx.queryL = new ArrayList<QStep>();
			this.dataL = dataL;
		}

		public Query1<Hotel> query()
		{
			queryctx.queryL = new ArrayList<QStep>();
			return new Query1<Hotel>(queryctx);
		}

		public void setActionProcessor(IQueryActionProcessor<Hotel> proc) 
		{
			queryctx.proc = proc;
		}

		@Override
		public int size() 
		{
			return dataL.size();
		}

		@Override
		public void delete(long id) 
		{
			throw new RuntimeException("no del yet");
		}

		@Override
		public void updateFrom(IFormBinder binder) 
		{
			throw new RuntimeException("no del yet");
		}
	}


	public static class MyHotelProc<T>  extends SfxBaseObj implements IQueryActionProcessor<T>
	{
		EntityDB<T> db = new EntityDB<T>();
		List<T> dataL;
		List<T> resultL;
		String orderBy;
		String orderAsc; //"asc" or "desc"
		int limit;

		public MyHotelProc(SfxContext ctx, List<T> hotelL)
		{
			super(ctx);
			this.dataL = hotelL;
		}

		@Override
		public void start(List<QueryAction> actionL) 
		{
			resultL = null; //new ArrayList<T>();
			orderBy = null;
			limit = -1;
			log("start");
		}

		@Override
		public T findOne() //exactly one
		{
			log("findOne");
			return null;
		}

		private void initResultLIfNeeded()
		{
			if (resultL == null)
			{
				resultL = db.union(dataL, new ArrayList<T>());
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
		public T findAny() //0 or 1
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
		public List<T> findMany() 
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
		public void processAction(int index, QueryAction qaction) 
		{
			String action = qaction.action;

			log(String.format(" %d. %s", index, action));

			if (action.equals("ALL"))
			{
				resultL = db.union(dataL, new ArrayList<T>());
			}
			else if (action.equals("WHERE"))
			{
				resultL = findMatchByType(qaction);
			}
			else if (action.equals("AND"))
			{
				List<T> tmp1 = findMatchByType(qaction);
				resultL = db.intersection(resultL, tmp1);
			}
			else if (action.equals("OR"))
			{
				List<T> tmp1 = findMatchByType(qaction);
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
				throw new FluentException("ActionProc: unknown action: " + action);
			}
		}

		private List<T> findMatchByType(QueryAction qaction)
		{
			if (qaction.obj == null)
			{
				throw new FluentException("ActionProc: obj is null");
			}

			if (qaction.op.equals("eq"))
			{
				if (qaction.obj instanceof Long)
				{
					return db.findMatches(dataL, qaction.fieldName, (Long)qaction.obj);
				}
				else if (qaction.obj instanceof Integer)
				{
					return db.findMatches(dataL, qaction.fieldName, (Integer)qaction.obj);
				}
				else if (qaction.obj instanceof String)
				{
					return db.findMatches(dataL, qaction.fieldName, (String)qaction.obj);
				}
				else
				{
					throw new FluentException("ActionProc: unsupported obj type: " + qaction.obj.getClass().getSimpleName());
				}
			}
			else if (qaction.op.equals("lt"))
			{
				return doCompare(qaction, IValueMatcher.LT);
			}
			else if (qaction.op.equals("le"))
			{
				return doCompare(qaction, IValueMatcher.LE);
			}
			else if (qaction.op.equals("gt"))
			{
				return doCompare(qaction, IValueMatcher.GT);
			}
			else if (qaction.op.equals("ge"))
			{
				return doCompare(qaction, IValueMatcher.GE);
			}
			else if (qaction.op.equals("neq"))
			{
				return doCompare(qaction, IValueMatcher.NEQ);
			}
			else
			{
				throw new FluentException("ActionProc: unsupported op type: " + qaction.op);
			}
		}

		private List<T> doCompare(QueryAction qaction, int matchType)
		{
			if (qaction.obj instanceof Integer)
			{
				return db.findCompareMatches(dataL, qaction.fieldName, qaction.obj, Integer.class, matchType);
			}
			else if (qaction.obj instanceof String)
			{
				return db.findCompareMatches(dataL, qaction.fieldName, qaction.obj, String.class, matchType);
			}
			else if (qaction.obj instanceof Long)
			{
				return db.findCompareMatches(dataL, qaction.fieldName, qaction.obj, Long.class, matchType);
			}
			else
			{
				throw new FluentException("ActionProc: unsupported obj type: " + qaction.obj.getClass().getSimpleName());
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

	@Test(expected=FluentException.class)
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

	@Test
	public void testInt()
	{
		init();
		int target = 110;

		Hotel h = dao.query().where("nVal").eq(target).findAny();
		assertNotNull(h);
		assertEquals(target, h.nVal);
		assertEquals("Spitfire", h.model);

	}

	@Test
	public void testLT()
	{
		init();
		int target = 112;

		Hotel h = dao.query().where("nVal").lt(target).findAny();
		assertNotNull(h);
		assertEquals(110, h.nVal);

		long count = dao.query().where("nVal").lt(target).findCount();
		assertEquals(2, count);

		h = dao.query().where("model").lt("Bxx").findAny();
		assertNotNull(h);
		assertEquals("Airbus", h.model);
	}
	@Test
	public void testLE()
	{
		init();
		int target = 112;

		Hotel h = dao.query().where("nVal").le(target).findAny();
		assertNotNull(h);
		assertEquals(110, h.nVal);

		long count = dao.query().where("nVal").le(target).findCount();
		assertEquals(3, count);

		h = dao.query().where("model").le("Bxx").findAny();
		assertNotNull(h);
		assertEquals("Airbus", h.model);
	}
	@Test
	public void testGT()
	{
		init();
		int target = 112;

		Hotel h = dao.query().where("nVal").gt(target).findAny();
		assertNotNull(h);
		assertEquals(113, h.nVal);

		long count = dao.query().where("nVal").gt(target).findCount();
		assertEquals(1, count);

		h = dao.query().where("model").gt("Caa").findAny();
		assertNotNull(h);
		assertEquals("Spitfire", h.model);
	}
	@Test
	public void testGE()
	{
		init();
		int target = 112;

		Hotel h = dao.query().where("nVal").ge(target).findAny();
		assertNotNull(h);
		assertEquals(113, h.nVal);

		long count = dao.query().where("nVal").ge(target).findCount();
		assertEquals(2, count);

		h = dao.query().where("model").ge("Caa").findAny();
		assertNotNull(h);
		assertEquals("Spitfire", h.model);
	}

	@Test
	public void testNEQ()
	{
		init();
		int target = 110;

		Hotel h = dao.query().where("nVal").neq(target).findAny();
		assertNotNull(h);
		assertEquals(113, h.nVal);

		long count = dao.query().where("nVal").neq(target).findCount();
		assertEquals(3, count);

		h = dao.query().where("model").neq("Caa").findAny();
		assertNotNull(h);
		assertEquals("Spitfire", h.model);
	}

	//--- helpers ---
	private HotelDao dao;
	List<Hotel> hotelL;

	private void init()
	{
		this.createContext();
		hotelL = this.buildHotels();
		dao = new HotelDao(hotelL);
		dao.setActionProcessor(new MyHotelProc<Hotel>(_ctx, hotelL));
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

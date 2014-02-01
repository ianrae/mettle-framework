import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mef.framework.entitydb.EntityDB;
import org.mef.framework.fluent.EntityDBQueryProcessor;
import org.mef.framework.fluent.FluentException;
import org.mef.framework.fluent.ProcRegistry;
import org.mef.framework.fluent.QueryContext;

import testentities.Hotel;
import testentities.HotelDao;
import tools.BaseTest;



public class FluentDBTests extends BaseTest
{
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
		dao = new HotelDao(hotelL, new QueryContext<Hotel>(_ctx, Hotel.class));
		
		ProcRegistry register = initProcRegistry();
		register.registerDao(Hotel.class, new EntityDBQueryProcessor<Hotel>(_ctx, hotelL));
	}

	List<Hotel> buildHotels()
	{
		ArrayList<Hotel> L = new ArrayList<Hotel>();

		long id = 1L;
		L.add(new Hotel(id++,"UL900", "Spitfire", 10));
		L.add(new Hotel(id++,"UL901", "Spitfire", 13));
		L.add(new Hotel(id++,"AC710", "Airbus", 11));
		L.add(new Hotel(id++,"UL901", "Boeing", 12));
		return L;
	}

	private void addMore()
	{
		long id = 1L;
		hotelL.add(new Hotel(id++,"UL901", "abc", 10));
		hotelL.add(new Hotel(id++,"UL901", "def", 10));
		hotelL.add(new Hotel(id++,"UL901", "ghi", 10));
		hotelL.add(new Hotel(id++,"UL901", "jkl", 10));
	}
}

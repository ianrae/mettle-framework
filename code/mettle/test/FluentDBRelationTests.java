import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mef.framework.fluent.EntityDBQueryProcessor;
import org.mef.framework.fluent.ProcRegistry;
import org.mef.framework.fluent.QueryContext;

import testentities.Hotel;
import testentities.HotelDao;
import testentities.StreetAddress;
import tools.BaseTest;
import tools.testfiles.StreetAddressDao;



public class FluentDBRelationTests extends BaseTest
{

	@Test
	public void test()
	{
		init();
		String target = "King";

		StreetAddress h = dao.query().where("street").eq(target).findAny();
		assertNotNull(h);
		assertEquals(target, h.street);
		assertEquals(100, h.number);
	}

	@Test
	public void testFindEntity()
	{
		init();
		StreetAddress addr = this.addressL.get(0);
		Hotel hotel = this.hotelL.get(0);
		hotel.setAddr(addr);
		
		hotel = hotelDao.query().where("addr").eq(addr).findAny();

		assertNotNull(hotel);
		assertSame(addr, hotel.getAddr());

	}

	@Test
	public void testWhereInRelation()
	{
		init();
		StreetAddress addr = this.addressL.get(0);
		Hotel hotel = this.hotelL.get(0);
		hotel.setAddr(addr);
		
		String target = "King";
		hotel = hotelDao.query().where("addr.street").eq(target).findAny();
		assertNull(hotel);
		
		target = "Main";
		hotel = hotelDao.query().where("addr.street").eq(target).findAny();
		assertNotNull(hotel);
		assertEquals(target, hotel.getAddr().getStreet());

	}

	//--- helpers ---
	private StreetAddressDao dao;
	private HotelDao hotelDao;
	List<StreetAddress> addressL;
	List<Hotel> hotelL;

	private void init()
	{
		this.createContext();
		addressL = this.buildAddresses();
		hotelL = this.buildHotels();
		
		dao = new StreetAddressDao(addressL, new QueryContext<StreetAddress>(_ctx, StreetAddress.class));
		hotelDao = new HotelDao(hotelL, new QueryContext<Hotel>(_ctx, Hotel.class));

		ProcRegistry registry = initProcRegistry();
		EntityDBQueryProcessor<StreetAddress> addrProc = new EntityDBQueryProcessor<StreetAddress>(_ctx, addressL);
		EntityDBQueryProcessor<Hotel> hotelProc = new EntityDBQueryProcessor<Hotel>(_ctx, hotelL);
		
		registry.registerDao(StreetAddress.class, addrProc);
		registry.registerDao(Hotel.class, hotelProc);
	}

	private List<StreetAddress> buildAddresses()
	{
		ArrayList<StreetAddress> L = new ArrayList<StreetAddress>();

		long id = 50L;
		L.add(new StreetAddress(id++, "Main", 100));
		L.add(new StreetAddress(id++, "King", 100));
		return L;
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

}

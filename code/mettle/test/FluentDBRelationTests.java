import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mef.framework.binder.IFormBinder;
import org.mef.framework.dao.IDAO;
import org.mef.framework.fluent.EntityDBQueryProcessor;
import org.mef.framework.fluent.IQueryActionProcessor;
import org.mef.framework.fluent.QStep;
import org.mef.framework.fluent.Query1;
import org.mef.framework.fluent.QueryContext;

import testentities.Hotel;
import testentities.HotelDao;
import testentities.StreetAddress;
import tools.BaseTest;



public class FluentDBRelationTests extends BaseTest
{


	public static class StreetAddressDao implements IDAO
	{
		//		public List<Hotel> dataL;
		public QueryContext<StreetAddress> queryctx = new QueryContext<StreetAddress>();
		List<StreetAddress> dataL;

		public StreetAddressDao(List<StreetAddress> dataL)
		{
			queryctx.queryL = new ArrayList<QStep>();
			this.dataL = dataL;
			
			
		}

		public Query1<StreetAddress> query()
		{
			queryctx.queryL = new ArrayList<QStep>();
			return new Query1<StreetAddress>(queryctx);
		}

		public void setActionProcessor(IQueryActionProcessor<StreetAddress> proc) 
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


	@Test
	public void testAnd()
	{
		init();
		String target = "King";

		StreetAddress h = dao.query().where("street").eq(target).findAny();
		assertNotNull(h);
		assertEquals(target, h.street);
		assertEquals(100, h.number);

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
		dao = new StreetAddressDao(addressL);
		
		hotelL = this.buildHotels();
		hotelDao = new HotelDao(hotelL);
		
		dao.setActionProcessor(new EntityDBQueryProcessor<StreetAddress>(_ctx, addressL));
	}

	private List<StreetAddress> buildAddresses()
	{
		ArrayList<StreetAddress> L = new ArrayList<StreetAddress>();

		L.add(new StreetAddress("Main", 100));
		L.add(new StreetAddress("King", 100));
		return L;
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

}

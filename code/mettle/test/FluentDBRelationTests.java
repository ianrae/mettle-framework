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
import org.mef.framework.fluent.EntityDBQueryProcessor;
import org.mef.framework.fluent.FluentException;
import org.mef.framework.fluent.IQueryActionProcessor;
import org.mef.framework.fluent.QStep;
import org.mef.framework.fluent.Query1;
import org.mef.framework.fluent.QueryContext;

import tools.BaseTest;



public class FluentDBRelationTests extends BaseTest
{
	public static class StreetAddress extends Entity
	{
		public String street;
		public int number;

		public StreetAddress(String street, Integer num)
		{
			this.street = street;
			this.number = num;
		}
	}
	
	public static class Hotel extends Entity
	{
		public String flight;
		public String model;
		public Integer num;
		public int  nVal;
		public StreetAddress addr;

		public Hotel(String flight, String model, Integer num)
		{
			this.flight = flight;
			this.model = model;
			this.num = num;
			this.nVal = num + 100;
		}
	}



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
	List<StreetAddress> addressL;

	private void init()
	{
		this.createContext();
		addressL = this.buildHotels();
		dao = new StreetAddressDao(addressL);
		dao.setActionProcessor(new EntityDBQueryProcessor<StreetAddress>(_ctx, addressL));
	}

	List<StreetAddress> buildHotels()
	{
		ArrayList<StreetAddress> L = new ArrayList<StreetAddress>();

		L.add(new StreetAddress("Main", 100));
		L.add(new StreetAddress("King", 100));
		return L;
	}

}

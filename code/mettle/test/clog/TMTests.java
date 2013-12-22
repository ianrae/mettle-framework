package clog;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mef.framework.binder.IFormBinder;
import org.mef.framework.dao.IDAO;
import org.mef.framework.entities.Entity;
import org.mef.framework.sfx.SfxContext;

import persistence.ICLOGDAO;
import persistence.ThingManager;
import persistence.WorldParser;
import clog.JsonTests.AirportParser;
import clog.JsonTests.BigAirport;
import clog.JsonTests.BigAirportParser;
import clog.JsonTests.GateParser;
import clog.JsonTests.MultiAirportParser;

public class TMTests extends BaseJsonTest implements ICLOGDAO
{
	public class ClogEntity extends Entity
	{
		public ClogEntity()
		{}

		public ClogEntity(Long userId, String blob)
		{
			this.userId = userId;
			this.blob = blob;
		}

		public ClogEntity(ClogEntity entity)
		{
			this.id = entity.id;
			this.userId = entity.userId;
			this.blob = entity.blob;
		}

		public Long id;
		public Long userId;
	    public String blob; //holds all the json
	}
	
	
	public static class AirportThingManager extends ThingManager<BigAirport>
	{
		public AirportThingManager(SfxContext ctx)
		{
			super(ctx);
		}

		@Override
		protected WorldParser createWorldParser()
		{
			WorldParser parser = new WorldParser(_ctx);
			parser.addParser("Airport", new AirportParser(_ctx));
			parser.addParser("BigAirport", new BigAirportParser(_ctx));
			parser.addParser("Gate", new GateParser(_ctx));
			parser.addParser("MultiAirport", new MultiAirportParser(_ctx));
			return parser;
		}
	}
	
	
	@Test
	public void test() throws Exception
	{
		init();
		ICLOGDAO dao = this;
		_ctx.getServiceLocator().registerSingleton(ICLOGDAO.class, dao);
		
		AirportThingManager tm = new AirportThingManager(_ctx);
		
		Long clogId = 1L;
		BigAirport airport = tm.load(clogId);
		assertEquals(1, airport.id);
		
		assertEquals(0, tm.saveCounter);
		tm.saveIfNeeded();
		assertEquals(0, tm.saveCounter);
		tm.setDirty();
		assertEquals(0, tm.saveCounter); //no save yet
		tm.saveIfNeeded();
		assertEquals(1, tm.saveCounter); 
	}
	
	
	
	//--- helpers ---
	private String getJson() 
	{
		String s = "{'rootType':'BigAirport','root': {'id':1,'flag':true,'name':'bob','gate':{'id':2},'size':56}, 'refs': [ {'type': 'Gate', 'things': [ {'id':2,'name':'gate1'} ] } ] }";
		return fix(s);
	}


	//------ implement ICLOGDAO ----
	@Override
	public int size() {
		return 1;
	}
	@Override
	public void delete(long id) {
	}
	@Override
	public void updateFrom(IFormBinder binder) {
	}
	@Override
	public ClogEntity findById(long id) 
	{
		ClogEntity entity = new ClogEntity();
		entity.blob = this.getJson();
		return entity;
	}


	@Override
	public List<ClogEntity> all() 
	{
		ArrayList<ClogEntity> L = new ArrayList<TMTests.ClogEntity>();
		L.add(findById(1));
		return L;
	}
	@Override
	public void save(ClogEntity entity) 
	{
	}
	@Override
	public void update(ClogEntity entity)
	{
	}

}

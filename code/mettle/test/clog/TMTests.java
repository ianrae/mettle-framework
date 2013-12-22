package clog;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;
import org.mef.framework.sfx.SfxErrorTracker;

import persistence.WorldParser;
import clog.JsonTests.AirportParser;
import clog.JsonTests.BigAirport;
import clog.JsonTests.BigAirportParser;
import clog.JsonTests.GateParser;
import clog.JsonTests.MultiAirport;
import clog.JsonTests.MultiAirportParser;

import tools.BaseTest;

public class TMTests extends BaseJsonTest
{
	public static class AirportThingManager extends SfxBaseObj
	{
		private BigAirport loadedObj;
		private boolean loaded; //have attempted a load
		
		public AirportThingManager(SfxContext ctx)
		{
			super(ctx);
		}

		public boolean load(String json) throws Exception 
		{
			WorldParser parser = createWorldParser();
			loadedObj = (BigAirport) parser.parse(json);
			
			loaded =  (loadedObj != null);
			return loaded;
		}
		
		public BigAirport getLoadedObj()
		{
			return loadedObj;
		}
		
		public BigAirport load() throws Exception
		{
			if (! loaded)
			{
				load("");
			}
			return loadedObj;
		}
		
		
		private WorldParser createWorldParser()
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
		AirportThingManager tm = new AirportThingManager(_ctx);
		
		String json = getJson();
		boolean b = tm.load(json);
		assertEquals(true, b);
		
		BigAirport airport = tm.getLoadedObj();
		assertEquals(1, airport.id);
	}
	
	
	
	private String getJson() 
	{
		String s = "{'rootType':'BigAirport','root': {'id':1,'flag':true,'name':'bob','gate':{'id':2},'size':56}, 'refs': [ {'type': 'Gate', 'things': [ {'id':2,'name':'gate1'} ] } ] }";
		return fix(s);
	}



	//--- helpers ---
	

}

package clog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mef.framework.sfx.SfxContext;
import org.mef.framework.sfx.SfxErrorTracker;

import persistence.BaseParser;
import persistence.IIdGenerator;
import persistence.ReferenceList;
import persistence.WorldParser;
import tools.BaseTest;

public class JsonTests extends BaseTest
{
	public static class Thing
	{
		public int id;
	}
	public static class Airport extends Thing
	{
		public boolean flag;
		public String name;
		public int size;
	}
	public static class Gate extends Thing
	{
		public String name;
	}
	public static class BigAirport extends Airport
	{
		public Gate gate;
	}
	
	public static class AirportParser extends BaseParser
	{
		public AirportParser(SfxContext ctx)
		{
			super(ctx);
		}
		
		protected Thing createObj()
		{
			return new Airport();
		}
		
		protected void onParse(Thing targetParam) throws Exception
		{
			Airport target = (Airport)targetParam;
			parseId(target);
			target.flag = helper.getBool("flag");
			target.name = helper.getString("name");
			target.size = helper.getInt("size");
		}

		@Override
		protected void onRender(Thing targetParam, IIdGenerator generator) 
		{
			Airport target = (Airport)targetParam;
			obj.put("id", target.id);
			obj.put("flag", target.flag);
			obj.put("name", target.name);
			obj.put("size", target.size);
		}
	}
	
	public static class GateParser extends BaseParser
	{
		public GateParser(SfxContext ctx)
		{
			super(ctx);
		}
		protected Thing createObj()
		{
			return new Gate();
		}
		
		protected void onParse(Thing targetParam) throws Exception
		{
			Gate target = (Gate) targetParam;
			parseId(target);
			target.name = helper.getString("name");
		}
		
		@Override
		protected void onRender(Thing targetParam, IIdGenerator generator) 
		{
			Gate target = (Gate) targetParam;
			obj.put("id", target.id);
			obj.put("name", target.name);
		}
	}
	
	public static class BigAirportParser extends AirportParser
	{
		public BigAirportParser(SfxContext ctx)
		{
			super(ctx);
		}
		
		protected Thing createObj()
		{
			return new BigAirport();
		}
		
		protected void onParse(Thing targetParam) throws Exception
		{
			super.onParse(targetParam);
			this.addRef(targetParam, "gate", Gate.class);
//			BigAirport target = (BigAirport) targetParam;
//			JSONObject jo = getEntity("gate");
//			GateParser inner1 = new GateParser(_ctx);
//			target.gate = inner1.parseFromJO(jo);
		}
		
		@Override
		protected void resolve(String refName, Thing refObj, Object targetParam) throws Exception
		{
			if (refName.equals("gate"))
			{
				BigAirport target = (BigAirport) targetParam;
				target.gate = (Gate) refObj;
			}
		}
		
		@Override
		protected void onRender(Thing targetParam, IIdGenerator generator) 
		{
			super.onRender(targetParam, generator);
			BigAirport target = (BigAirport) targetParam;
			this.renderRef("gate", target.gate);
			
		}
	}
	

	@Test
	public void test3b() throws Exception
	{
		log("--test3b---");
		init();
		WorldParser parser = new WorldParser(_ctx);
		String s = "{'rootType':'Airport','root': RRR }";
		s = s.replace("RRR", "{'id':1,'flag':true,'name':'bob','size':56}");
		Airport airport = (Airport) parser.parse(fix(s));
		chkAirport(airport, true, "bob", 56);		
		assertEquals(1, airport.id);
		chkErrors(0);
	}
	
	@Test
	public void test3c() throws Exception
	{
		log("--test3c---");
		init();
		WorldParser parser = new WorldParser(_ctx);
		String s = "{'rootType':'BigAirport','root': RRR, 'refs': EEE  }";
		s = s.replace("RRR", "{'id':1,'flag':true,'name':'bob','size':56,'gate':{'id':2} }");
		s = s.replace("EEE", "[ GGG ]");
		s = s.replace("GGG", "{ 'type': 'Gate', 'things': [{'id':2, 'name':'gate1'}] }");
		BigAirport airport = (BigAirport) parser.parse(fix(s));
		chkAirport(airport, true, "bob", 56);		
		assertEquals(1, airport.id);
		chkAirportGate(airport, 2, "gate1");
		chkErrors(0);
		
		log("render..");
		parser = new WorldParser(_ctx);
		String output = parser.render(airport);
		log(output);
		chkErrors(0);
		
		log("re-parse");
		parser = new WorldParser(_ctx);
		airport = (BigAirport) parser.parse(output);
		chkAirport(airport, true, "bob", 56);		
		assertEquals(1, airport.id);
		chkAirportGate(airport, 2, "gate1");
		chkErrors(0);
	}
	
	@Test
	public void test4() throws Exception
	{
		log("--test4---");
		init();
		AirportParser parser = new AirportParser(_ctx);
		String s = "{'id':1,'flag':true,'name':'bob','size':56}";
		s = fix(s);
		Airport obj = (Airport) parser.parse(s);
		assertNotNull(obj);
		chkAirport(obj, true, "bob", 56);		
		assertEquals(1, obj.id);
		
		chkErrors(0);
		log("render..");
		
		WorldParser pp = new WorldParser(_ctx);
		String output = parser.render(obj, pp);
		log(output);
		assertEquals(s, output);
	}

	private void chkErrors(int i) 
	{
		SfxErrorTracker tracker = (SfxErrorTracker) _ctx.getServiceLocator().getInstance(SfxErrorTracker.class);
		assertEquals(i, tracker.getErrorCount());
	}

	@Test
	public void test5() throws Exception
	{
		log("--test5---");
		this.init();
		GateParser parser = new GateParser(_ctx);
		String s = "{'id':1,'flag':true,'name':'bob','size':56}"; //works with extra stuff!
		Gate obj = (Gate) parser.parse(fix(s));
		assertNotNull(obj);
		assertEquals("bob", obj.name);
		assertEquals(1, obj.id);
		
		chkErrors(0);
	}
	
	@Test
	public void test6() throws Exception
	{
		log("--test6---");
		this.init();
		BigAirportParser parser = new BigAirportParser(_ctx);
		parser.refL = new ReferenceList();
		String s = "{'id':1,'flag':true,'name':'bob','size':56,'gate':{'id':2, 'name':'gate1'}}"; //works with extra stuff!
		BigAirport obj = (BigAirport) parser.parse(fix(s));
		assertNotNull(obj);
		chkAirport(obj, true, "bob", 56);		
		assertNull(obj.gate);
		assertEquals(1, obj.id);
		chkErrors(0);
	}
	
	@Test
	public void test7() throws Exception
	{
		log("--test7---");
		init();
		Gate g1 = new Gate();
		g1.name = "gate1";
		Gate g2 = new Gate();
		g2.name = "gate2";
		BigAirport airport = new BigAirport();
		airport.flag = false;
		airport.name = "halifax";
		airport.gate = g1;
		
		log("render..");
		WorldParser parser = new WorldParser(_ctx);
		parser = new WorldParser(_ctx);
		String output = parser.render(airport);
		log(output);
		chkErrors(0);
		
	}	
	//------- helpers---------
	private String fix(String s)
	{
		s = s.replace('\'', '"');
		return s;
	}
	
	private void init()
	{
		this.createContext();
		SfxErrorTracker tracker = new SfxErrorTracker(_ctx);
		_ctx.getServiceLocator().registerSingleton(SfxErrorTracker.class, tracker);
	}
	
	private void chkAirport(Airport airport, boolean b, String name, int size)
	{
		assertNotNull("nil", airport);
		assertEquals(b, airport.flag);
		assertEquals(name, airport.name);
		assertEquals(size, airport.size);
	}
	private void chkAirportGate(BigAirport airport, int id, String name)
	{
		assertNotNull(airport.gate);
		assertEquals(id, airport.gate.id);
		assertEquals(name, airport.gate.name);
	}
}

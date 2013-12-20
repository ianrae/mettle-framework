package clog;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;
import org.mef.framework.sfx.SfxContext;
import org.mef.framework.sfx.SfxErrorTracker;

import persistence.BaseParser;
import persistence.ReferenceDesc;
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
		protected void onRender(Thing targetParam) 
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
		protected void onRender(Thing targetParam) 
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
		protected void onRender(Thing targetParam) 
		{
			super.onRender(targetParam);
			BigAirport target = (BigAirport) targetParam;
			this.renderRef("gate", target.gate);
			
		}
	}
	
	@Test
	public void test() 
	{
		JSONObject obj=new JSONObject();
		obj.put("name","foo");
		obj.put("num",new Integer(100));
		obj.put("balance",new Double(1000.21));
		obj.put("is_vip",new Boolean(true));
		obj.put("nickname",null);
		log(obj.toString());
	}
	
	@Test
	public void test2() throws Exception
	{
		log("--test2---");
		JSONParser parser=new JSONParser();
		String s="[0,{\"1\":{\"2\":{\"3\":{\"4\":[5,{\"6\":7}]}}}}]";
		Object obj=parser.parse(s);
		JSONArray array=(JSONArray)obj;
		log(String.format("el2 is: %s", array.get(1)));

		JSONObject obj2=(JSONObject)array.get(1);
		log(obj2.get("1").toString());    

		log("more..");
		s = "{'balance':1000.21,'num':100,'nickname': 'foo'}";
		log(s);
		s = fix(s);
		log(s);
		JSONObject obj3 = (JSONObject) parser.parse(s);
		Double dd = (Double) obj3.get("balance");
		Double expected = 1000.21;
		assertEquals(expected, dd);

		Long n = (Long) obj3.get("num");
		assertEquals(100, n.longValue());
		
		String nick = (String) obj3.get("nickname");
		assertEquals("foo", nick);
	}

	@Test
	public void test3() throws Exception
	{
		log("--test3---");
		JSONParser parser=new JSONParser();
		String s = "{'nobody':null,'enabled':true}";
		log(s);
		s = fix(s);
		log(s);
		JSONObject obj3 = (JSONObject) parser.parse(s);
		Object oo = obj3.get("nobody");
		assertEquals(null, oo);

		Boolean b = (Boolean) obj3.get("enabled");
		assertEquals(true, b);
		boolean bb = b;
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
		assertNotNull(airport);
		assertEquals(true, airport.flag);
		assertEquals("bob", airport.name);
		assertEquals(56, airport.size);
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
		assertNotNull(airport);
		assertEquals(true, airport.flag);
		assertEquals("bob", airport.name);
		assertEquals(56, airport.size);
		assertEquals(1, airport.id);
		
		//parser.resolveRefs();
		assertNotNull(airport.gate);
		assertEquals(2, airport.gate.id);
		assertEquals("gate1", airport.gate.name);

		chkErrors(0);
		
		log("render..");
		parser = new WorldParser(_ctx);
		String output = parser.render(airport);
		log(output);
		//assertEquals("ss", output);
		
		log("re-parse");
		parser = new WorldParser(_ctx);
		airport = (BigAirport) parser.parse(output);
		assertNotNull(airport);
		assertEquals(true, airport.flag);
		assertEquals("bob", airport.name);
		assertEquals(56, airport.size);
		assertEquals(1, airport.id);
		
		//parser.resolveRefs();
		assertNotNull(airport.gate);
		assertEquals(2, airport.gate.id);
		assertEquals("gate1", airport.gate.name);
	}
	@Test
	public void testRefSort()
	{
		ArrayList<ReferenceDesc> L = new ArrayList<ReferenceDesc>();
		
		ReferenceDesc desc = new ReferenceDesc();
		desc.refClass = Gate.class;
		L.add(desc);
		desc = new ReferenceDesc();
		desc.refClass = BigAirport.class;
		L.add(desc);
		desc = new ReferenceDesc();
		desc.refClass = Airport.class;
		L.add(desc);
		desc = new ReferenceDesc();
		desc.refClass = BigAirport.class;
		L.add(desc);
		
		Comparator<ReferenceDesc> comparator = new Comparator<ReferenceDesc>() {
		    public int compare(ReferenceDesc c1, ReferenceDesc c2) {
		        String s1 = c1.refClass.getSimpleName();
		        String s2 = c2.refClass.getSimpleName();
		        return s1.compareTo(s2);
		    }
		};		
		
		Collections.sort(L, comparator);
		
		
		ArrayList<String> nameL = new ArrayList<String>();
		for(ReferenceDesc dd : L)
		{
			String s = dd.refClass.getSimpleName();
			log(s);
			if (! nameL.contains(s))
			{
				nameL.add(s);
			}
		}
//		
		for(String tmp : nameL)
		{
			log("name: " + tmp);
		}
	}
	
	@Test
	public void test4() throws Exception
	{
		log("--test4---");
		init();
		AirportParser parser = new AirportParser(_ctx);
		String s = "{'id':12,'flag':true,'name':'bob','size':56}";
		s = fix(s);
		Airport obj = (Airport) parser.parse(s);
		assertNotNull(obj);
		assertEquals(true, obj.flag);
		assertEquals("bob", obj.name);
		assertEquals(56, obj.size);
		assertEquals(12, obj.id);
		
		chkErrors(0);
		log("render..");
		
		String output = parser.render(obj);
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
		assertEquals(true, obj.flag);
		assertEquals("bob", obj.name);
		assertEquals(56, obj.size);
		assertNull(obj.gate);
		assertEquals(1, obj.id);
		
//		assertNotNull(obj.gate);
//		assertEquals(2, obj.gate.id);
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
}

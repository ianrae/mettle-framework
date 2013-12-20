package clog;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;
import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;
import org.mef.framework.sfx.SfxErrorTracker;

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
	
	public static class ReferenceDesc
	{
		public BaseParser parser;
		
		public String refName;  //name of file
		public Class refClass;
		public Object target;

		public int refId;
	}
	
	
	public static class ParserHelper extends SfxBaseObj
	{
		protected JSONObject obj;
		SfxErrorTracker tracker;
		
		public ParserHelper(SfxContext ctx, JSONObject obj)
		{
			super(ctx);
			this.obj = obj;
			
			tracker = (SfxErrorTracker) _ctx.getServiceLocator().getInstance(SfxErrorTracker.class);
		}
		
		public void errorOccured(String errMsg)
		{
			tracker.addError(errMsg);
		}
		
		public boolean getBool(String name)
		{
			Boolean b = (Boolean) obj.get(name);
			if (b == null)
			{
				//err
				return false;
			}
			return b;
		}
		public String getString(String name)
		{
			String s = (String) obj.get(name);
			return s; //can be null
		}
		public int getInt(String name)
		{
			Long val = (Long) obj.get(name);
			if (val == null)
			{
				//err
				return 0;
			}
			int n = val.intValue();
			return n;
		}
		
		public JSONObject getEntity(String name)
		{
			JSONObject val = (JSONObject) obj.get(name);
			return val; //can be null
		}
		public JSONArray getArray(String name)
		{
			JSONArray val = (JSONArray) obj.get(name);
			return val; //can be null
		}
	}
	
	public static class ParserDesc
	{
		public BaseParser parser;
		public ArrayList<Thing> refL = new ArrayList<JsonTests.Thing>();
		
		public ParserDesc(BaseParser parser)
		{
			this.parser = parser;
		}
	}
	
	
	public static class WorldParser extends SfxBaseObj
	{
		private JSONParser parser=new JSONParser();
		protected JSONObject obj;
		protected ArrayList<ReferenceDesc> refL = new ArrayList<JsonTests.ReferenceDesc>();
		protected ParserHelper helper;
		
//		private ArrayList<Gate> gateL = new ArrayList<JsonTests.Gate>();
		
		private HashMap<String,ParserDesc> parserMap = new HashMap<String, JsonTests.ParserDesc>();
		
		public WorldParser(SfxContext ctx)
		{
			super(ctx);
			
			parserMap.put("Airport", new ParserDesc(new AirportParser(_ctx)));
			parserMap.put("BigAirport", new ParserDesc(new BigAirportParser(_ctx)));
			parserMap.put("Gate", new ParserDesc(new GateParser(_ctx)));
		}
		
		protected JSONObject startParse(String input) throws Exception
		{
			this.obj = (JSONObject) parser.parse(input);
			this.helper = new ParserHelper(_ctx, obj);
			return obj;
		}
		
		public Object parse(String input) throws Exception
		{
			Object obj = doParse(input);
			
			if (obj != null)
			{
				JSONArray ooo = helper.getArray("refs");
				if (ooo != null)
				{
					for(int i = 0; i < ooo.size(); i++)
					{
						JSONObject val = (JSONObject) ooo.get(i);
						if (val != null)
						{
							String type = (String)val.get("type");
							JSONArray ggg = (JSONArray) val.get("things");
							loadThings(type, ggg);
						}
					}
				}
				
				resolveRefs();
			}
			return obj;
		}

		private void loadThings(String type, JSONArray ggg) throws Exception
		{
			if (ggg == null)
			{
				return;
			}
			ParserDesc desc = this.parserMap.get(type);
			
			for(int i = 0; i < ggg.size(); i++)
			{
				JSONObject val = (JSONObject) ggg.get(i);
				if (val != null)
				{
					BaseParser gp = (BaseParser) desc.parser; //re-using parser, careful!!
					Thing target = gp.parseFromJO(val);
					desc.refL.add(target);
				}
			}
		}
		
		
		
		private Thing doParse(String input) throws Exception
		{
			startParse(input);
			String s = helper.getString("rootType");	
			log(s);
			
			JSONObject val = helper.getEntity("root");
			ParserDesc desc = parserMap.get(s);
			
			if (desc != null)
			{
				BaseParser aparser = desc.parser;
				aparser.refL = refL;
				Thing target = aparser.parseFromJO(val);
				return target;
			}
			else
			{
				helper.errorOccured("unknown rootType: " + s);
				return null;
			}
		}
		
		
		private void resolveRefs() throws Exception
		{
			log(String.format("resolveRefs: %d", refL.size()));
			for(ReferenceDesc desc : refL)
			{
				Thing thing = findByRefId(desc);
				desc.parser.resolve(desc.refName, thing, desc.target);
			}
		}

		private Thing findByRefId(ReferenceDesc desc) 
		{
			String name = desc.refClass.getSimpleName();
			log(name);
			
			ParserDesc d2 = parserMap.get(name);
			if (d2 == null)
			{
				return null;
			}
			
			for(Thing thing : d2.refL)
			{
				if (thing.id == desc.refId)
				{
					return thing;
				}
			}
			return null;
		}

		public String render(Thing thing) 
		{
			String name = thing.getClass().getSimpleName();
			ParserDesc desc = parserMap.get(name);
			if (desc == null)
			{
				//err
				return null;
			}
			
			String rootStr = desc.parser.render(thing);
			
			String output = String.format("{'rootType':'%s','root': %s, 'refs':  }", name, rootStr);
//			s = s.replace("RRR", "{'id':1,'flag':true,'name':'bob','size':56,'gate':{'id':2} }");
//			s = s.replace("EEE", "[ GGG ]");
//			s = s.replace("GGG", "{ 'type': 'Gate', 'things': [{'id':2, 'name':'gate1'}] }");
			
			return output;
		}
	}
	
	public static abstract class BaseParser extends SfxBaseObj
	{
		private JSONParser parser=new JSONParser();
		protected JSONObject obj;
		public ArrayList<ReferenceDesc> refL;
		protected ParserHelper helper;
		
		public BaseParser(SfxContext ctx)
		{
			super(ctx);
		}
		
		protected JSONObject startParse(String input) throws Exception
		{
			this.obj = (JSONObject) parser.parse(input);
			this.helper = new ParserHelper(_ctx, obj);
			return obj;
		}
		
		protected void addRef(Object target, String refName, Class clazz)
		{
			JSONObject oo = helper.getEntity(refName);
			ParserHelper h2 = new ParserHelper(_ctx, oo);
			int idd = h2.getInt("id");
			log("xx " + idd);
			
			ReferenceDesc desc = new ReferenceDesc();
			desc.parser = this;
			desc.refName = refName;
			desc.refClass = clazz;
			desc.refId = idd;
			desc.target = target;
			this.refL.add(desc);
		}
		
		abstract protected Thing createObj();
		abstract protected void onParse(Thing t) throws Exception;
		
		Thing parse(String input) throws Exception
		{
			startParse(input);
			return parseFromJO(this.obj);
		}
		Thing parseFromJO(JSONObject jo) throws Exception
		{
			this.obj = jo;
			this.helper = new ParserHelper(_ctx, obj);
			Thing target = createObj();
			this.onParse(target);
			return target;
		}
		
		protected void parseId(Thing target)
		{
			target.id = helper.getInt("id");
		}
		
		protected void resolve(String refName, Thing refObj, Object targetParam) throws Exception
		{
		}
		
		//render
		public String render(Thing target) 
		{
			obj=new JSONObject();
			onRender(target);
			return obj.toJSONString();
		}
		
		protected abstract void onRender(Thing target);
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
		assertEquals("ss", output);
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
		parser.refL = new ArrayList<JsonTests.ReferenceDesc>();
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

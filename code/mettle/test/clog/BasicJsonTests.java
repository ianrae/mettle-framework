package clog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;
import org.mef.framework.sfx.SfxErrorTracker;

import persistence.ReferenceDesc;
import tools.BaseTest;
import clog.JsonTests.Airport;
import clog.JsonTests.BigAirport;
import clog.JsonTests.Gate;

public class BasicJsonTests extends BaseJsonTest
{
	
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
	
	
	//------- helpers---------
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

package clog;

import static org.junit.Assert.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;

import tools.BaseTest;

public class JsonTests extends BaseTest
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
	}
	//------- helpers---------
	private String fix(String s)
	{
		s = s.replace('\'', '"');
		return s;
	}
}

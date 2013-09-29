package mef;

import static org.junit.Assert.*;

import mef.entities.Phone_GEN;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.junit.Test;

import play.libs.Json;

public class JsonTests extends BaseTest
{
	public static class MyObject
	{
		private String name;
		public String getName() {
			return name;
		}
		public void setName(String name)
		{
			this.name = name;
		}
	}

	@Test
	public void test() throws Exception
	{
		String json = "{\"name\": \"Guillaume\"}";
		log(json);
		
		ObjectNode result = Json.newObject();
		MyObject foo = new ObjectMapper().readValue(json, MyObject.class);
		log(foo.name);
		assertEquals("Guillaume", foo.name);
	}

	@Test
	public void testPhone() throws Exception
	{
		String json = "{\"id\": \"45\", \"name\": \"Guillaume\"}";
		log(json);
		
		
		ObjectNode result = Json.newObject();
		Phone_GEN foo = new ObjectMapper().readValue(json, Phone_GEN.class);
		log(foo.name);
		assertEquals("Guillaume", foo.name);
	}
}

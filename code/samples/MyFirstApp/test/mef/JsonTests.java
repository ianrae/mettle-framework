package mef;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import mef.dals.IPhoneDAL;
import mef.entities.Phone;
import mef.entities.Phone_GEN;
import mef.mocks.MockPhoneDAL;

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
	
	@Test
	public void testFile() throws Exception
	{
		String path = this.getTestFile("json1.txt");
		String json = this.readFile(path);
		log(json);
		
		Phone_GEN foo = new ObjectMapper().readValue(json, Phone_GEN.class);
		log(foo.name);
		assertEquals("Mark", foo.name);
	}
	
	@Test
	public void testSeed() throws Exception
	{
		init();
		assertNotNull(_dal);
		String path = this.getTestFile("json1.txt");
		String json = this.readFile(path);
		log(json);
		
		Phone phone = new ObjectMapper().readValue(json, Phone.class);
		log(phone.name);
		assertEquals("Mark", phone.name);
		
		_dal.save(phone);
		assertEquals(1, _dal.size());
	}
	
	
	private MockPhoneDAL _dal;
	public void init()
	{
		super.init();
		_dal = getDAL();
	}
	
	private MockPhoneDAL getDAL()
	{
		MockPhoneDAL dal = (MockPhoneDAL) _ctx.getServiceLocator().getInstance(IPhoneDAL.class); 
		return dal;
	}	
	//------------ helper fns ------------
	private String readFile(String path) 
	{
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
	    try {
	    	br = new BufferedReader(new FileReader(path));
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append('\n');
	            line = br.readLine();
	        }
	    } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
	    	if (br != null)
	    	{
	    		try {
					br.close();
				} catch (IOException e) {
//					e.printStackTrace();
				}
	    	}
	    }		
	    String everything = sb.toString();
	    return everything;
	}
}

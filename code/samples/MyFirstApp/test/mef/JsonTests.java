package mef;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import mef.core.EntityLoader;
import mef.daos.IPhoneDAO;
import mef.daos.IUserDAO;
import mef.daos.mocks.MockPhoneDAO;
import mef.daos.mocks.MockUserDAO;
import mef.entities.Phone;
import mef.entities.User;
import mef.gen.Phone_GEN;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.junit.Test;
import org.mef.framework.utils.ResourceReader;

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
	
	@Test
	public void testSeed2() throws Exception
	{
		init();
		assertEquals(0, _dal.size());

		String path = this.getTestFile("json2.txt");
		String json = readFile(path);
		
		EntityLoader loader = new EntityLoader(_ctx);
		loader.loadPhone(json);
		assertEquals(2, _dal.size());
		assertEquals(2, _dal.size()); //again - not added twice
		
		//steps for init from json
		//mef.xml: define find_by_name
		//set seedWith
		//real DAL, implement find_by_name and initFromJson
	}

	@Test
	public void testUserSeed() throws Exception
	{
		init();
		assertEquals(0, _dal.size());
		MockUserDAO userDal = (MockUserDAO) _ctx.getServiceLocator().getInstance(IUserDAO.class); 
		
		String path = this.getTestFile("json-user1.txt");
		String json = readFile(path);
		EntityLoader loader = new EntityLoader(_ctx);
		loader.loadUser(json);
		assertEquals(1, userDal.size());
		long id = userDal.findById(1).phone.id;
		
		assertEquals(1, userDal.size()); //again - not added twice
		long id2 = userDal.findById(1).phone.id;
		assertEquals(id2, id);
		User u = userDal.find_by_name("user1");
		assertEquals("user1", u.name);
		assertEquals("Mark", u.phone.name);
		
		assertEquals(1, _dal.size());
		Phone ph = _dal.find_by_name("Mark");
		assertNotNull(ph);
		assertEquals(u.phone.id, ph.id);
	}

	@Test
	public void testMefConfigFiles() throws Exception
	{
		init();
		MockUserDAO userDal = (MockUserDAO) _ctx.getServiceLocator().getInstance(IUserDAO.class); 
		
		String json = ResourceReader.readSeedFile("json-user1.txt");
		EntityLoader loader = new EntityLoader(_ctx);
		loader.loadUser(json);
		assertEquals(1, userDal.size());
		long id = userDal.findById(1).phone.id;
		
		assertEquals(1, userDal.size()); //again - not added twice
		long id2 = userDal.findById(1).phone.id;
		assertEquals(id2, id);
		User u = userDal.find_by_name("user1");
		assertEquals("user1", u.name);
		assertEquals("Mark", u.phone.name);
		
		assertEquals(1, _dal.size());
		Phone ph = _dal.find_by_name("Mark");
		assertNotNull(ph);
		assertEquals(u.phone.id, ph.id);
	}
	
	private MockPhoneDAO _dal;
	public void init()
	{
		super.init();
		_dal = getDAL();
	}
	
	private MockPhoneDAO getDAL()
	{
		MockPhoneDAO dal = (MockPhoneDAO) _ctx.getServiceLocator().getInstance(IPhoneDAO.class); 
		return dal;
	}	
	//------------ helper fns ------------
	private String readFile(String path) 
	{
		return ResourceReader.readFile(path);
	}
}

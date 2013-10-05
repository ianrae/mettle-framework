package mef;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.List;

import mef.core.DaoJsonLoader;
import mef.core.EntityLoader;
import mef.daos.IPhoneDAO;
import mef.daos.IUserDAO;
import mef.daos.mocks.MockPhoneDAO;
import mef.daos.mocks.MockUserDAO;
import mef.entities.Phone;
import mef.entities.User;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.mef.framework.utils.ResourceReader;

public class JsonMoreTests extends BaseTest
{
	@Test
	public void test() throws Exception
	{
		this.init();

		String json = loadJson("json-user1.txt");
		EntityLoader loader = new EntityLoader(_ctx);
		loader.loadUser(json);
		
		assertEquals(3, getUserDAO().size());
	}
	
	@Test
	public void testTree() throws Exception
	{
		init();
		String json = loadJson("json-user2.txt");
    	ObjectMapper mapper = new ObjectMapper();
    	JsonNode rootNode = mapper.readTree(json);
    	
    	JsonNode msgNode = rootNode.path("User");
		Iterator<JsonNode> ite = msgNode.getElements();

		while (ite.hasNext()) {
			JsonNode temp = ite.next();
			JsonNode jj = temp.get("id");
			log(jj.getTextValue());

			 jj = temp.get("name");
			log(jj.getTextValue());
			
			jj = temp.get("phone");
			JsonNode jjj = jj.get("name");
			log(jjj.getTextValue());
		}    	
	}
	
	@Test
	public void testTree2() throws Exception
	{
		init();
		String json = loadJson("json-user3.txt");
    	ObjectMapper mapper = new ObjectMapper();
    	JsonNode rootNode = mapper.readTree(json);
    	
    	DaoJsonLoader loader = new DaoJsonLoader();
//    	List<Phone> phoneL = new ArrayList<Phone>(); //parsePhones(rootNode);
    	List<Phone> phoneL = parsePhones(loader, rootNode);
    	
    	JsonNode msgNode = rootNode.path("User");
		Iterator<JsonNode> ite = msgNode.getElements();

		String[] names = new String[]{ "user1", "user2", "user3" };
		Long[] phoneIds = new Long[]{ 20L, 30L, 40L };
		
		List<User> userL = loader.loadUsers(rootNode);
		int i = 0;
		
		for(User u : userL)
		{
			assertEquals(0L, u.id.longValue());
			assertEquals(names[i], u.name);
			assertEquals(phoneIds[i], u.phone.id);
			
			i++;
		}    	
		
		loader.resolveIds(userL, phoneL);
		
		for(User u : userL)
		{
			log(String.format("%s : %s", u.name, u.phone.name));
		}
	}
	

	private List<Phone> parsePhones(DaoJsonLoader loader, JsonNode rootNode) 
	{
		List<Phone> phoneL = loader.loadPhones(rootNode);
		
		String[] names = new String[]{ "phone1", "phone2", "phone3", "phone4" };
		Long[] phoneIds = new Long[]{ 20L, 30L, 40L, 50L };
		
		int i = 0;
		for(Phone ph : phoneL) 
		{
			assertEquals(phoneIds[i].longValue(), ph.id.longValue());
			assertEquals(names[i], ph.name);
			i++;
		}    	
		
		return phoneL;
	}

	
	@Test
	public void testEntityLoader() throws Exception
	{
		init();
		IPhoneDAO phoneDAO = getPhoneDAO();
		IUserDAO userDAO = getUserDAO();
		
		assertEquals(0, phoneDAO.size());
		String json = loadJson("json-user3.txt");
		
		EntityLoader loader = new EntityLoader(_ctx);
		loader.loadAll(json);

		assertEquals(4, phoneDAO.size());
		
		for(Phone ph : phoneDAO.all())
		{
			log(String.format("%d: %s", ph.id, ph.name));
		}
		log("users..");
		for(User u : userDAO.all())
		{
			log(String.format("%d: %s  %d: %s", u.id, u.name, u.phone.id, u.phone.name));
		}
		

		String[] names = new String[]{ "user1", "user2", "user3" };
		Long[] phoneIds = new Long[]{ 20L, 30L, 40L };
		
//		List<User> userL = loader.loadUsers(rootNode);
//		int i = 0;
//		
//		for(User u : userL)
//		{
//			assertEquals(0L, u.id.longValue());
//			assertEquals(names[i], u.name);
//			assertEquals(phoneIds[i], u.phone.id);
//			
//			i++;
//		}    	
//		
//		loader.resolveIds(userL, phoneL);
//		
//		for(User u : userL)
//		{
//			log(String.format("%s : %s", u.name, u.phone.name));
//		}
	}
	
	//------------ helpers ----------------
	private IUserDAO getUserDAO()
	{
		MockUserDAO dal = (MockUserDAO) _ctx.getServiceLocator().getInstance(IUserDAO.class); 
		return dal;
	}
	private IPhoneDAO getPhoneDAO()
	{
		MockPhoneDAO dal = (MockPhoneDAO) _ctx.getServiceLocator().getInstance(IPhoneDAO.class); 
		return dal;
	}
	
	private String loadJson(String filename)
	{
		String dir = this.getTestFile("seed");
		String json = ResourceReader.readSeedFile(filename, dir);
		return json;
		
	}
}


package mef;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mef.core.EntityLoader;
import mef.daos.IUserDAO;
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

		String json = ResourceReader.readSeedFile("json-user1.txt");
		EntityLoader loader = new EntityLoader(_ctx);
		loader.loadUser(json);
		
		assertEquals(3, getDAO().size());
	}
	
	@Test
	public void testTree() throws Exception
	{
		init();
		String json = ResourceReader.readSeedFile("json-user2.txt");
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
		String dir = this.getTestFile("seed");
		String json = ResourceReader.readSeedFile("json-user3.txt", dir);
    	ObjectMapper mapper = new ObjectMapper();
    	JsonNode rootNode = mapper.readTree(json);
    	
    	List<Phone> phoneL = parsePhones(rootNode);
    	
    	JsonNode msgNode = rootNode.path("User");
		Iterator<JsonNode> ite = msgNode.getElements();

		String[] names = new String[]{ "user1", "user2", "user3" };
		Long[] phoneIds = new Long[]{ 20L, 30L, 40L };
		
		int i = 0;
		
		while (ite.hasNext()) {
			JsonNode temp = ite.next();
			User u = this.readUser(temp);
			assertEquals(0L, u.id.longValue());
			assertEquals(names[i], u.name);
			assertEquals(phoneIds[i], u.phone.id);
			
			i++;
		}    	
	}
	
	private List<Phone> parsePhones(JsonNode rootNode) 
	{
		List<Phone> phoneL = new ArrayList<Phone>();
		
    	JsonNode msgNode = rootNode.path("Phone");
		Iterator<JsonNode> ite = msgNode.getElements();

		String[] names = new String[]{ "phone1", "phone2", "phone3", "phone4" };
		Long[] phoneIds = new Long[]{ 20L, 30L, 40L, 50L };
		
		int i = 0;
		
		while (ite.hasNext()) {
			JsonNode temp = ite.next();
			Phone ph = readPhone(temp);
			assertEquals(0L, ph.id.longValue());
			assertEquals(names[i], ph.name);
			
			phoneL.add(ph);
			i++;
		}    	
		
		return phoneL;
	}

	private Phone readPhone(JsonNode node)
	{
		Phone ph = new Phone();
		JsonNode jj = node.get("id");
		ph.id = jj.asLong();

		jj = node.get("name");
		ph.name = jj.getTextValue();
		
		return ph;
	}

	private User readUser(JsonNode node)
	{
		User u = new User();
		JsonNode jj = node.get("id");
		u.id = jj.asLong();

		jj = node.get("name");
		u.name = jj.getTextValue();
		
		jj = node.get("phone");
		jj = jj.get("id");
		u.phone = new Phone();
		u.phone.id = jj.asLong();

		return u;
	}
	
	
	//------------ helpers ----------------
	private IUserDAO getDAO()
	{
		MockUserDAO dal = (MockUserDAO) _ctx.getServiceLocator().getInstance(IUserDAO.class); 
		return dal;
	}
}


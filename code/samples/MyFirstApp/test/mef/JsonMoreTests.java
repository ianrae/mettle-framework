package mef;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;

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
		String json = ResourceReader.readSeedFile("json-user3.txt");
    	ObjectMapper mapper = new ObjectMapper();
    	JsonNode rootNode = mapper.readTree(json);
    	
    	JsonNode msgNode = rootNode.path("User");
		Iterator<JsonNode> ite = msgNode.getElements();

		String[] names = new String[]{ "user1", "user2", "user3" };
		Long[] phoneIds = new Long[]{ 20L, 30L, 40L };
		
		int i = 0;
		
		while (ite.hasNext()) {
			JsonNode temp = ite.next();
			User u = this.readFields(temp);
			assertEquals(0L, u.id.longValue());
			assertEquals(names[i], u.name);
			assertEquals(phoneIds[i], u.phone.id);
			
			i++;
		}    	
	}
	
	private User readFields(JsonNode node)
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


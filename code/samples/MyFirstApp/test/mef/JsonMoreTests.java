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
	public static class DaoJsonLoader
	{
		public Phone readPhone(JsonNode node)
		{
			Phone obj = new Phone();
			JsonNode jj = node.get("id");
			obj.id = jj.asLong();

			jj = node.get("name");
			obj.name = jj.getTextValue();
			
			return obj;
		}

		public User readUser(JsonNode node)
		{
			User obj = new User();
			JsonNode jj = node.get("id");
			obj.id = jj.asLong();

			jj = node.get("name");
			obj.name = jj.getTextValue();
			
			jj = node.get("phone");
			jj = jj.get("id");
			obj.phone = new Phone();
			obj.phone.id = jj.asLong();

			return obj;
		}

		private List<Phone> loadPhones(JsonNode rootNode) 
		{
			List<Phone> phoneL = new ArrayList<Phone>();
			
	    	JsonNode msgNode = rootNode.path("Phone");
			Iterator<JsonNode> ite = msgNode.getElements();

			int i = 0;
			while (ite.hasNext()) {
				JsonNode temp = ite.next();
				Phone ph = readPhone(temp);
				
				phoneL.add(ph);
				i++;
			}    	
			
			return phoneL;
		}
		
		private List<User> loadUsers(JsonNode rootNode) 
		{
			List<User> userL = new ArrayList<User>();
			
	    	JsonNode msgNode = rootNode.path("User");
			Iterator<JsonNode> ite = msgNode.getElements();

			int i = 0;
			while (ite.hasNext()) {
				JsonNode temp = ite.next();
				User ph = readUser(temp);
				
				userL.add(ph);
				i++;
			}    	
			
			return userL;
		}
		
		public void resolveIds(List<User> userL, List<Phone> phoneL) 
		{
			for(User u : userL)
			{
				Phone ph = findPhoneWithId(u.phone.id, phoneL);
				if (ph == null)
				{
					//err
				}
				else
				{
					u.phone = ph;
				}
			}
		}
		private Phone findPhoneWithId(long id, List<Phone> phoneL) 
		{
			for (Phone ph : phoneL)
			{
				if (ph.id == id)
				{
					return ph;
				}
			}
			return null;
		}
	}
	

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

	
	
	//------------ helpers ----------------
	private IUserDAO getDAO()
	{
		MockUserDAO dal = (MockUserDAO) _ctx.getServiceLocator().getInstance(IUserDAO.class); 
		return dal;
	}
}


package mef.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mef.entities.Phone;
import mef.entities.User;

import org.codehaus.jackson.JsonNode;

public class DaoJsonLoader
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

	public List<Phone> loadPhones(JsonNode rootNode) 
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
	
	public List<User> loadUsers(JsonNode rootNode) 
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
				//err!!
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
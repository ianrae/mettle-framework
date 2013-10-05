//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.gen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.codehaus.jackson.JsonNode;
import mef.entities.*;



public class DaoJsonLoader_GEN
{


	public Task readTask(JsonNode node)
	{
		Task obj = new Task();
		JsonNode jj = node.get("id");
		obj.id = jj.asLong();

				jj = node.get("label");
				obj.label = jj.getTextValue();

				jj = node.get("enabled");
				obj.enabled = jj.asBoolean();



		return obj;
	}
	public List<Task> loadTasks(JsonNode rootNode) 
	{
		List<Task> phoneL = new ArrayList<Task>();

    	JsonNode msgNode = rootNode.path("Task");
		Iterator<JsonNode> ite = msgNode.getElements();

		int i = 0;
		while (ite.hasNext()) {
			JsonNode temp = ite.next();
			Task ph = readTask(temp);

			phoneL.add(ph);
			i++;
		}    	

		return phoneL;
	}
	protected Task findTaskWithId(long id, List<Task> phoneL) 
	{
		for (Task ph : phoneL)
		{
			if (ph.id == id)
			{
				return ph;
			}
		}
		return null;
	}
	public User readUser(JsonNode node)
	{
		User obj = new User();
		JsonNode jj = node.get("id");
		obj.id = jj.asLong();

				jj = node.get("name");
				obj.name = jj.getTextValue();

				jj = node.get("phone");



		return obj;
	}
	public List<User> loadUsers(JsonNode rootNode) 
	{
		List<User> phoneL = new ArrayList<User>();

    	JsonNode msgNode = rootNode.path("User");
		Iterator<JsonNode> ite = msgNode.getElements();

		int i = 0;
		while (ite.hasNext()) {
			JsonNode temp = ite.next();
			User ph = readUser(temp);

			phoneL.add(ph);
			i++;
		}    	

		return phoneL;
	}
	protected User findUserWithId(long id, List<User> phoneL) 
	{
		for (User ph : phoneL)
		{
			if (ph.id == id)
			{
				return ph;
			}
		}
		return null;
	}
	public Phone readPhone(JsonNode node)
	{
		Phone obj = new Phone();
		JsonNode jj = node.get("id");
		obj.id = jj.asLong();

				jj = node.get("name");
				obj.name = jj.getTextValue();



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
	protected Phone findPhoneWithId(long id, List<Phone> phoneL) 
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

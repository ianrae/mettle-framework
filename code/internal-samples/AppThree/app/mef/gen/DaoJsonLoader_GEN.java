//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.gen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.codehaus.jackson.JsonNode;
import mef.entities.*;
import java.util.Date;
import org.mef.framework.loaders.BaseDaoJsonLoader;



public class DaoJsonLoader_GEN extends BaseDaoJsonLoader
{


	public User readUser(JsonNode node)
	{
		User obj = new User();
		JsonNode jj = node.get("id");
		obj.id = jj.asLong();

				jj = node.get("name");
				obj.name = jj.getTextValue();



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
	public Role readRole(JsonNode node)
	{
		Role obj = new Role();
		JsonNode jj = node.get("id");
		obj.id = jj.asLong();

				jj = node.get("name");
				obj.name = jj.getTextValue();



		return obj;
	}
	public List<Role> loadRoles(JsonNode rootNode) 
	{
		List<Role> phoneL = new ArrayList<Role>();

    	JsonNode msgNode = rootNode.path("Role");
		Iterator<JsonNode> ite = msgNode.getElements();

		int i = 0;
		while (ite.hasNext()) {
			JsonNode temp = ite.next();
			Role ph = readRole(temp);

			phoneL.add(ph);
			i++;
		}    	

		return phoneL;
	}
	protected Role findRoleWithId(long id, List<Role> phoneL) 
	{
		for (Role ph : phoneL)
		{
			if (ph.id == id)
			{
				return ph;
			}
		}
		return null;
	}
	public Ticket readTicket(JsonNode node)
	{
		Ticket obj = new Ticket();
		JsonNode jj = node.get("id");
		obj.id = jj.asLong();



		return obj;
	}
	public List<Ticket> loadTickets(JsonNode rootNode) 
	{
		List<Ticket> phoneL = new ArrayList<Ticket>();

    	JsonNode msgNode = rootNode.path("Ticket");
		Iterator<JsonNode> ite = msgNode.getElements();

		int i = 0;
		while (ite.hasNext()) {
			JsonNode temp = ite.next();
			Ticket ph = readTicket(temp);

			phoneL.add(ph);
			i++;
		}    	

		return phoneL;
	}
	protected Ticket findTicketWithId(long id, List<Ticket> phoneL) 
	{
		for (Ticket ph : phoneL)
		{
			if (ph.id == id)
			{
				return ph;
			}
		}
		return null;
	}
	public AuthRules readAuthRules(JsonNode node)
	{
		AuthRules obj = new AuthRules();
		JsonNode jj = node.get("id");
		obj.id = jj.asLong();

				jj = node.get("userId");

				jj = node.get("roleId");

				jj = node.get("ticketId");



		return obj;
	}
	public List<AuthRules> loadAuthRuless(JsonNode rootNode) 
	{
		List<AuthRules> phoneL = new ArrayList<AuthRules>();

    	JsonNode msgNode = rootNode.path("AuthRules");
		Iterator<JsonNode> ite = msgNode.getElements();

		int i = 0;
		while (ite.hasNext()) {
			JsonNode temp = ite.next();
			AuthRules ph = readAuthRules(temp);

			phoneL.add(ph);
			i++;
		}    	

		return phoneL;
	}
	protected AuthRules findAuthRulesWithId(long id, List<AuthRules> phoneL) 
	{
		for (AuthRules ph : phoneL)
		{
			if (ph.id == id)
			{
				return ph;
			}
		}
		return null;
	}
}

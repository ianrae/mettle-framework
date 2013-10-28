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
	public AuthRole readRole(JsonNode node)
	{
		AuthRole obj = new AuthRole();
		JsonNode jj = node.get("id");
		obj.id = jj.asLong();

				jj = node.get("name");
				obj.name = jj.getTextValue();



		return obj;
	}
	public List<AuthRole> loadRoles(JsonNode rootNode) 
	{
		List<AuthRole> phoneL = new ArrayList<AuthRole>();

    	JsonNode msgNode = rootNode.path("Role");
		Iterator<JsonNode> ite = msgNode.getElements();

		int i = 0;
		while (ite.hasNext()) {
			JsonNode temp = ite.next();
			AuthRole ph = readRole(temp);

			phoneL.add(ph);
			i++;
		}    	

		return phoneL;
	}
	protected AuthRole findRoleWithId(long id, List<AuthRole> phoneL) 
	{
		for (AuthRole ph : phoneL)
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
	public AuthRule readAuthRule(JsonNode node)
	{
		AuthRule obj = new AuthRule();
		JsonNode jj = node.get("id");
		obj.id = jj.asLong();

				jj = node.get("user");

				jj = node.get("role");

				jj = node.get("ticket");



		return obj;
	}
	public List<AuthRule> loadAuthRules(JsonNode rootNode) 
	{
		List<AuthRule> phoneL = new ArrayList<AuthRule>();

    	JsonNode msgNode = rootNode.path("AuthRule");
		Iterator<JsonNode> ite = msgNode.getElements();

		int i = 0;
		while (ite.hasNext()) {
			JsonNode temp = ite.next();
			AuthRule ph = readAuthRule(temp);

			phoneL.add(ph);
			i++;
		}    	

		return phoneL;
	}
	protected AuthRule findAuthRuleWithId(long id, List<AuthRule> phoneL) 
	{
		for (AuthRule ph : phoneL)
		{
			if (ph.id == id)
			{
				return ph;
			}
		}
		return null;
	}
}

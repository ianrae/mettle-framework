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


	public AuthUser readUser(JsonNode node)
	{
		AuthUser obj = new AuthUser();
		JsonNode jj = node.get("id");
		obj.id = jj.asLong();

				jj = node.get("name");
				obj.name = jj.getTextValue();



		return obj;
	}
	public List<AuthUser> loadUsers(JsonNode rootNode) 
	{
		List<AuthUser> phoneL = new ArrayList<AuthUser>();

    	JsonNode msgNode = rootNode.path("User");
		Iterator<JsonNode> ite = msgNode.getElements();

		int i = 0;
		while (ite.hasNext()) {
			JsonNode temp = ite.next();
			AuthUser ph = readUser(temp);

			phoneL.add(ph);
			i++;
		}    	

		return phoneL;
	}
	protected AuthUser findUserWithId(long id, List<AuthUser> phoneL) 
	{
		for (AuthUser ph : phoneL)
		{
			if (ph.id == id)
			{
				return ph;
			}
		}
		return null;
	}
	public AuthRole readAuthRole(JsonNode node)
	{
		AuthRole obj = new AuthRole();
		JsonNode jj = node.get("id");
		obj.id = jj.asLong();

				jj = node.get("name");
				obj.name = jj.getTextValue();



		return obj;
	}
	public List<AuthRole> loadAuthRoles(JsonNode rootNode) 
	{
		List<AuthRole> phoneL = new ArrayList<AuthRole>();

    	JsonNode msgNode = rootNode.path("AuthRole");
		Iterator<JsonNode> ite = msgNode.getElements();

		int i = 0;
		while (ite.hasNext()) {
			JsonNode temp = ite.next();
			AuthRole ph = readAuthRole(temp);

			phoneL.add(ph);
			i++;
		}    	

		return phoneL;
	}
	protected AuthRole findAuthRoleWithId(long id, List<AuthRole> phoneL) 
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
	public AuthTicket readAuthTicket(JsonNode node)
	{
		AuthTicket obj = new AuthTicket();
		JsonNode jj = node.get("id");
		obj.id = jj.asLong();



		return obj;
	}
	public List<AuthTicket> loadAuthTickets(JsonNode rootNode) 
	{
		List<AuthTicket> phoneL = new ArrayList<AuthTicket>();

    	JsonNode msgNode = rootNode.path("AuthTicket");
		Iterator<JsonNode> ite = msgNode.getElements();

		int i = 0;
		while (ite.hasNext()) {
			JsonNode temp = ite.next();
			AuthTicket ph = readAuthTicket(temp);

			phoneL.add(ph);
			i++;
		}    	

		return phoneL;
	}
	protected AuthTicket findAuthTicketWithId(long id, List<AuthTicket> phoneL) 
	{
		for (AuthTicket ph : phoneL)
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

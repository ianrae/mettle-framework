//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.gen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.codehaus.jackson.JsonNode;
import mef.entities.*;
import java.util.Date;

import org.mef.framework.auth.AuthRole;
import org.mef.framework.auth.AuthRule;
import org.mef.framework.auth.AuthSubject;
import org.mef.framework.auth.AuthTicket;
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
	public Blog readBlog(JsonNode node)
	{
		Blog obj = new Blog();
		JsonNode jj = node.get("id");
		obj.id = jj.asLong();

				jj = node.get("name");
				obj.name = jj.getTextValue();



		return obj;
	}
	public List<Blog> loadBlogs(JsonNode rootNode) 
	{
		List<Blog> phoneL = new ArrayList<Blog>();

    	JsonNode msgNode = rootNode.path("Blog");
		Iterator<JsonNode> ite = msgNode.getElements();

		int i = 0;
		while (ite.hasNext()) {
			JsonNode temp = ite.next();
			Blog ph = readBlog(temp);

			phoneL.add(ph);
			i++;
		}    	

		return phoneL;
	}
	protected Blog findBlogWithId(long id, List<Blog> phoneL) 
	{
		for (Blog ph : phoneL)
		{
			if (ph.id == id)
			{
				return ph;
			}
		}
		return null;
	}
	public AuthSubject readAuthSubject(JsonNode node)
	{
		AuthSubject obj = new AuthSubject();
		JsonNode jj = node.get("id");
		obj.id = jj.asLong();

				jj = node.get("name");
				obj.name = jj.getTextValue();



		return obj;
	}
	public List<AuthSubject> loadAuthSubjects(JsonNode rootNode) 
	{
		List<AuthSubject> phoneL = new ArrayList<AuthSubject>();

    	JsonNode msgNode = rootNode.path("AuthSubject");
		Iterator<JsonNode> ite = msgNode.getElements();

		int i = 0;
		while (ite.hasNext()) {
			JsonNode temp = ite.next();
			AuthSubject ph = readAuthSubject(temp);

			phoneL.add(ph);
			i++;
		}    	

		return phoneL;
	}
	protected AuthSubject findAuthSubjectWithId(long id, List<AuthSubject> phoneL) 
	{
		for (AuthSubject ph : phoneL)
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

				jj = node.get("subject");

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

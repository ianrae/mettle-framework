package mef.core;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import mef.daos.IAuthRoleDAO;
import mef.daos.IAuthRuleDAO;
import mef.daos.IAuthSubjectDAO;
import mef.daos.IAuthTicketDAO;
import mef.daos.IUserDAO;
import mef.entities.User;
import mef.gen.EntityLoaderSaver_GEN;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.mef.framework.auth.AuthRole;
import org.mef.framework.auth.AuthRule;
import org.mef.framework.auth.AuthSubject;
import org.mef.framework.auth.AuthTicket;
import org.mef.framework.entities.Entity;
import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;

public class EntityLoader extends SfxBaseObj
{
	private IUserDAO userDal; 
	private IAuthTicketDAO ticketDal;
	private IAuthRoleDAO roleDal;
	private IAuthRuleDAO ruleDal;
	private IAuthSubjectDAO subjectDal;
	
	public EntityLoader(SfxContext ctx)
	{
		super(ctx);
		userDal = DaoFinder.getUserDao();
		ticketDal = DaoFinder.getAuthTicketDao();
		roleDal = DaoFinder.getAuthRoleDao();
		ruleDal = DaoFinder.getAuthRuleDao();
		subjectDal = DaoFinder.getAuthSubjectDao();
	}
	
	public void loadAll(String json) throws Exception
	{
    	DaoJsonLoader loader = new DaoJsonLoader();
    	ObjectMapper mapper = new ObjectMapper();
    	JsonNode rootNode = mapper.readTree(json);

    	//entities can have logical ids in the json, so that one entity can refer to another
    	//When entities stored in DB they will get a real id
    	//Keep a map to track logical-id to real-id.
    	//Logical id formatted as classname+id, eg "Company.20"
    	HashMap<String, Long> map = new HashMap<String, Long>();
    	
    	List<User> phoneL = loader.loadUsers(rootNode);
    	saveUsers(phoneL, map);
    	
    	List<AuthTicket> ticketL = loader.loadAuthTickets(rootNode);
    	saveAuthTickets(ticketL, map);
    	
    	List<AuthRole> roleL = loader.loadAuthRoles(rootNode);
    	saveAuthRoles(roleL, map);

    	List<AuthSubject> subjectL = loader.loadAuthSubjects(rootNode);
    	saveAuthSubjects(subjectL, map);
    	
    	List<AuthRule> ruleL = loader.loadAuthRules(rootNode);
    	saveAuthRules(ruleL, map);
	}


	private void saveUsers(List<User> phoneL, HashMap<String, Long> map) 
	{
    	for(User ph : phoneL)
    	{
    		String key = makeKey(ph, ph.id);
    		User existing = userDal.find_by_name(ph.name); //use seedWith field
    		long id = EntityLoaderSaver_GEN.saveOrUpdate(ph, existing, userDal);
    		map.put(key, id);
    	}
	}
	private void saveAuthSubjects(List<AuthSubject> phoneL, HashMap<String, Long> map) 
	{
    	for(AuthSubject ph : phoneL)
    	{
    		String key = makeKey(ph, ph.id);
    		AuthSubject existing = subjectDal.find_by_name(ph.name); //use seedWith field
    		long id = EntityLoaderSaver_GEN.saveOrUpdate(ph, existing, subjectDal);
    		map.put(key, id);
    	}
	}
	private void saveAuthTickets(List<AuthTicket> phoneL, HashMap<String, Long> map) 
	{
    	for(AuthTicket ph : phoneL)
    	{
    		String key = makeKey(ph, ph.id);
    		AuthTicket existing = null; //no find by name userDal.find_by_name(ph.name); //use seedWith field
    		long id = EntityLoaderSaver_GEN.saveOrUpdate(ph, existing, ticketDal);
    		map.put(key, id);
    	}
	}
	private void saveAuthRoles(List<AuthRole> phoneL, HashMap<String, Long> map) 
	{
    	for(AuthRole ph : phoneL)
    	{
    		String key = makeKey(ph, ph.id);
    		AuthRole existing = roleDal.find_by_name(ph.name); //use seedWith field
    		long id = EntityLoaderSaver_GEN.saveOrUpdate(ph, existing, roleDal);
    		map.put(key, id);
    	}
	}
	private void saveAuthRules(List<AuthRule> phoneL, HashMap<String, Long> map) 
	{
		for(AuthRule rule : phoneL)
    	{
    		if (rule.role != null)
    		{
    			long existingId = findIdInMap(rule.role, rule.role.id, map);
				if (existingId != 0L)
				{
		    		AuthRole existing = roleDal.findById(existingId);
					rule.role = existing;
				}
    		}    		
    		
    		if (rule.subject != null)
    		{
    			long existingId = findIdInMap(rule.subject, rule.subject.id, map);
				if (existingId != 0L)
				{
		    		AuthSubject existing = subjectDal.findById(existingId);
					rule.subject = existing;
				}
    		}    		
    		
    		String key = makeKey(rule, rule.id);
    		AuthRule existing = null; //computerDal.find_by_name(computer.name); //use seedWith field
    		long id = EntityLoaderSaver_GEN.saveOrUpdate(rule, existing, ruleDal);
    		map.put(key, id);
    	}		
	}
	
	private String makeKey(Entity entity, Long id)
	{
		String s = entity.getClass().getSimpleName();
		s = String.format("%s.%d", s, id);
		return s;
	}

	private long findIdInMap(Entity entity, Long id, HashMap<String, Long> map) 
	{
		String key = makeKey(entity, id);
		Long physicalId = map.get(key);
		if (physicalId != null && physicalId.longValue() != 0L)
		{
			return physicalId.longValue();
		}
		else
		{
			log(String.format("ERR: can't find id: %s", key));
			return 0;
		}
	}	
}

package mef.core;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import mef.daos.IUserDAO;
import mef.entities.User;
import mef.gen.EntityLoaderSaver_GEN;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.mef.framework.entities.Entity;
import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;

public class EntityLoader extends SfxBaseObj
{
	private IUserDAO userDal; 
	
	public EntityLoader(SfxContext ctx)
	{
		super(ctx);
		userDal = (IUserDAO) Initializer.getDAO(IUserDAO.class); 
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

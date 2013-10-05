package mef.core;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import mef.daos.IPhoneDAO;
import mef.daos.IUserDAO;
import mef.entities.Phone;
import mef.entities.User;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.mef.framework.entities.Entity;
import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;

public class EntityLoader extends SfxBaseObj
{
	private IUserDAO userDal; 
	private IPhoneDAO phoneDal;
	
	public EntityLoader(SfxContext ctx)
	{
		super(ctx);
		userDal = (IUserDAO) _ctx.getServiceLocator().getInstance(IUserDAO.class); 
		phoneDal = (IPhoneDAO) _ctx.getServiceLocator().getInstance(IPhoneDAO.class); 
	}
	
    public void loadPhone(String json) throws Exception
    {
    	ObjectMapper mapper = new ObjectMapper();
    	Phone[] arPhone = mapper.readValue(json, Phone[].class);
    	for(int i = 0; i < arPhone.length; i++)
    	{
    		Phone entity = arPhone[i];
    		Phone existing = phoneDal.find_by_name(entity.name); //use seedWith field
    		if (existing != null)
    		{
    			entity.id = existing.id;
    		}
    		phoneDal.save(entity); //inserts or updates 
    	}
    }
    
    
    public void loadUser(String json) throws Exception
    {
    	ObjectMapper mapper = new ObjectMapper();
    	User[] arUser = mapper.readValue(json, User[].class);
    	for(int i = 0; i < arUser.length; i++)
    	{
    		User entity = arUser[i];
    		doPhone(entity.phone);
    		
    		User existing = userDal.find_by_name(entity.name); //use seedWith field
    		if (existing != null)
    		{
    			existing.name = entity.name; //copy all!!
    			userDal.update(existing);
    		}
    		else
    		{
    			userDal.save(entity); //inserts or updates
    		}
    	}
    }

	private void doPhone(Phone entity) 
	{
		Phone ph = phoneDal.find_by_name(entity.name); //use seedWith field
		
		if (ph != null)
		{
			ph.name = entity.name; //!!copy all over
			phoneDal.update(ph);
		}
		else
		{
			phoneDal.save(entity);
		}
		
	}

	public void loadAll(String json) throws Exception
	{
    	DaoJsonLoader loader = new DaoJsonLoader();
    	ObjectMapper mapper = new ObjectMapper();
    	JsonNode rootNode = mapper.readTree(json);

    	//entities can have logical ids in the json, so that one entity can refer to another
    	//When entities stored in DB they will get a real id
    	//Keep a map to track logical-id to real-id.
    	//Logical id formatted as classname+id, eg "Phone.20"
    	HashMap<String, Long> map = new HashMap<String, Long>();
    	
    	List<Phone> phoneL = loader.loadPhones(rootNode);
    	savePhones(phoneL, map);
    	
    	log("map:");
    	for(String key : map.keySet())
    	{
    		Long val = map.get(key);
    		log(String.format("%s -> %d", key, val));
    	}
    	
    	//use map during saving
    	List<User> userL = loader.loadUsers(rootNode);
    	saveUsers(userL, map);
    	
    	log("map2:");
    	for(String key : map.keySet())
    	{
    		Long val = map.get(key);
    		log(String.format("%s -> %d", key, val));
    	}
    	
	}

	private void saveUsers(List<User> userL, HashMap<String, Long> map) 
	{
    	for(User u : userL)
    	{
			String phKey = makeKey(u.phone, u.phone.id);
			Long phoneId = map.get(phKey);
			if (phoneId != 0L)
			{
	    		Phone existing = phoneDal.findById(phoneId);
				u.phone = existing;
			}
    		
    		User existing = userDal.find_by_name(u.name); //use seedWith field
    		if (existing != null)
    		{
    			u.id = existing.id;
    			userDal.save(u); //inserts or updates 
    		}
    		else
    		{
    			String s = makeKey(u, u.id);
    			u.id = 0L;
    			userDal.save(u); //inserts or updates 
    			map.put(s, u.id);
    		}
    	}
	}

	private void savePhones(List<Phone> phoneL, HashMap<String, Long> map) 
	{
    	for(Phone ph : phoneL)
    	{
    		Phone existing = phoneDal.find_by_name(ph.name); //use seedWith field
    		if (existing != null)
    		{
    			ph.id = existing.id;
    			phoneDal.save(ph); //inserts or updates 
    		}
    		else
    		{
    			String s = makeKey(ph, ph.id);
    			ph.id = 0L;
    			phoneDal.save(ph); //inserts or updates 
    			map.put(s, ph.id);
    		}
    	}
	}
	
	private String makeKey(Entity entity, Long id)
	{
		String s = entity.getClass().getName();
		s = String.format("%s.%d", s, id);
		return s;
	}

}

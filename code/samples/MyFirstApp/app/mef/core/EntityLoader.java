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

    	HashMap<String, Long> map = new HashMap<String, Long>();
    	
    	List<Phone> phoneL = loader.loadPhones(rootNode);

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
    			String s = String.format("%s%d", "Phone", ph.id);
    			ph.id = 0L;
    			phoneDal.save(ph); //inserts or updates 
    			map.put(s, ph.id);
    		}
    	}
    	
    	log("map:");
    	for(String key : map.keySet())
    	{
    		Long val = map.get(key);
    		log(String.format("%s -> %d", key, val));
    	}
    	
    	List<User> userL = loader.loadUsers(rootNode);
    	
    	for(User u : userL)
    	{
			String phKey = String.format("%s%d", "Phone", u.phone.id);
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
    			String s = String.format("%s%d", "User", u.id);
    			u.id = 0L;
    			userDal.save(u); //inserts or updates 
    			map.put(s, u.id);
    		}
    	}
    	
    	log("map2:");
    	for(String key : map.keySet())
    	{
    		Long val = map.get(key);
    		log(String.format("%s -> %d", key, val));
    	}
    	
	}

}

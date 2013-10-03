package mef.core;

import mef.dals.IPhoneDAO;
import mef.dals.IUserDAO;
import mef.entities.Phone;
import mef.entities.User;

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

}

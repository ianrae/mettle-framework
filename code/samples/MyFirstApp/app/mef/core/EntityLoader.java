package mef.core;

import mef.dals.IPhoneDAL;
import mef.dals.IUserDAL;
import mef.entities.Phone;
import mef.entities.User;

import org.codehaus.jackson.map.ObjectMapper;
import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;

public class EntityLoader extends SfxBaseObj
{
	private IUserDAL userDal; 
	private IPhoneDAL phoneDal;
	
	public EntityLoader(SfxContext ctx)
	{
		super(ctx);
		userDal = (IUserDAL) _ctx.getServiceLocator().getInstance(IUserDAL.class); 
		phoneDal = (IPhoneDAL) _ctx.getServiceLocator().getInstance(IPhoneDAL.class); 
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
    		doPhone(entity);
    		
    		User existing = userDal.find_by_name(entity.name); //use seedWith field
    		if (existing != null)
    		{
    			entity.id = existing.id;
    		}
    		userDal.save(entity); //inserts or updates 
    	}
    }

	private void doPhone(User entity) 
	{
		Phone ph = phoneDal.find_by_name(entity.phone.name); //use seedWith field
		
		if (ph != null)
		{
			entity.phone.id = ph.id;
		}
		phoneDal.save(entity.phone); //inserts or updates
		
	}

}

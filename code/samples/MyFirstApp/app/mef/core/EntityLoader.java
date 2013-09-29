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
	public EntityLoader(SfxContext ctx)
	{
		super(ctx);
	}
	
    public void loadPhoneFromJson(String json) throws Exception
    {
		IPhoneDAL dal = (IPhoneDAL) _ctx.getServiceLocator().getInstance(IPhoneDAL.class); 
    	
    	ObjectMapper mapper = new ObjectMapper();
    	Phone[] arPhone = mapper.readValue(json, Phone[].class);
    	for(int i = 0; i < arPhone.length; i++)
    	{
    		Phone entity = arPhone[i];
    		Phone existing = dal.find_by_name(entity.name);
    		if (existing != null)
    		{
    			entity.id = existing.id;
    		}
    		dal.save(entity); //inserts or updates 
    	}
    }
    
    
    public void loadUserFromJson(String json) throws Exception
    {
		IUserDAL userDal = (IUserDAL) _ctx.getServiceLocator().getInstance(IUserDAL.class); 
		IPhoneDAL phoneDal = (IPhoneDAL) _ctx.getServiceLocator().getInstance(IPhoneDAL.class); 
    	
    	ObjectMapper mapper = new ObjectMapper();
    	User[] arUser = mapper.readValue(json, User[].class);
    	for(int i = 0; i < arUser.length; i++)
    	{
    		User entity = arUser[i];
    		doPhone(entity, phoneDal);
    		
    		User existing = userDal.find_by_name(entity.name);
    		if (existing != null)
    		{
    			entity.id = existing.id;
    		}
    		userDal.save(entity); //inserts or updates 
    	}
    }

	private void doPhone(User entity, IPhoneDAL dal) 
	{
		Phone ph = dal.find_by_name(entity.phone.name);
		
		if (ph != null)
		{
			entity.phone.id = ph.id;
		}
		dal.save(entity.phone); //inserts or updates 
		
	}

}

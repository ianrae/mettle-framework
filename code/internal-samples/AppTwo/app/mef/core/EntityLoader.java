package mef.core;

import mef.daos.ICompanyDAO;
import mef.daos.IUserDAO;
import mef.entities.Company;

import org.codehaus.jackson.map.ObjectMapper;
import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;

public class EntityLoader extends SfxBaseObj
{
	private IUserDAO userDal; 
	private ICompanyDAO companyDal;
	
	public EntityLoader(SfxContext ctx)
	{
		super(ctx);
		userDal = (IUserDAO) _ctx.getServiceLocator().getInstance(IUserDAO.class); 
		companyDal = (ICompanyDAO) _ctx.getServiceLocator().getInstance(ICompanyDAO.class); 
	}
	
    public void loadCompany(String json) throws Exception
    {
    	ObjectMapper mapper = new ObjectMapper();
    	Company[] arPhone = mapper.readValue(json, Company[].class);
    	for(int i = 0; i < arPhone.length; i++)
    	{
    		Company entity = arPhone[i];
    		Company existing = companyDal.find_by_name(entity.name); //use seedWith field
    		if (existing != null)
    		{
    			entity.id = existing.id;
    		}
    		companyDal.save(entity); //inserts or updates 
    	}
    }
    
    

}

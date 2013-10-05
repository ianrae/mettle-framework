package mef.core;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import mef.daos.ICompanyDAO;
import mef.daos.IUserDAO;
import mef.entities.Company;
import mef.entities.User;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.mef.framework.entities.Entity;
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
    	Company[] arCompany = mapper.readValue(json, Company[].class);
    	for(int i = 0; i < arCompany.length; i++)
    	{
    		Company entity = arCompany[i];
    		Company existing = companyDal.find_by_name(entity.name); //use seedWith field
    		if (existing != null)
    		{
    			entity.id = existing.id;
    		}
    		companyDal.save(entity); //inserts or updates 
    	}
    }
    
    

	private void doCompany(Company entity) 
	{
		Company ph = companyDal.find_by_name(entity.name); //use seedWith field
		
		if (ph != null)
		{
			ph.name = entity.name; //!!copy all over
			companyDal.update(ph);
		}
		else
		{
			companyDal.save(entity);
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
    	//Logical id formatted as classname+id, eg "Company.20"
    	HashMap<String, Long> map = new HashMap<String, Long>();
    	
    	List<Company> phoneL = loader.loadCompanys(rootNode);
    	saveCompanys(phoneL, map);
    	
    	log("map:");
    	for(String key : map.keySet())
    	{
    		Long val = map.get(key);
    		log(String.format("%s -> %d", key, val));
    	}
 	}


	private void saveCompanys(List<Company> phoneL, HashMap<String, Long> map) 
	{
    	for(Company ph : phoneL)
    	{
    		Company existing = companyDal.find_by_name(ph.name); //use seedWith field
    		if (existing != null)
    		{
    			ph.id = existing.id;
    			companyDal.save(ph); //inserts or updates 
    		}
    		else
    		{
    			String s = makeKey(ph, ph.id);
    			ph.id = 0L;
    			companyDal.save(ph); //inserts or updates 
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

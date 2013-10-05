package boundaries.daos;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.mef.framework.binder.IFormBinder;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;

import play.Logger;

import boundaries.Boundary;

import models.CompanyModel;
import models.UserModel;

import mef.daos.ICompanyDAO;
import mef.entities.Company;
import mef.entities.User;

public class CompanyDAO implements ICompanyDAO 
{

	@Override
	public void save(Company entity) 
	{
		CompanyModel t = (CompanyModel)entity.cc; 
		if (t == null) //not yet known by db? (newly created)
		{
			System.out.println("save-auto-create");
			t = createModelFromEntity(entity); //create model, set entity, and call all setters
		}
		else //touch all (for ebean), except id
		{
			t.setName(entity.name);
		}
		t.save();
		entity.id = t.getId(); //in case created on
	}
	

	@Override
	public Company findById(long id) 
	{
		Logger.info("HERE GOES:");
//		CompanyModel t = CompanyModel.find.byId(id);
		
//		String oql = 
//		        "  find  user_model "
////		        +" fetch phone "
//		        +" where order.id = :id";
//		   
//		 Query<CompanyModel> query = Ebean.createQuery(CompanyModel.class, oql);
//		 query.setParameter("id", id);
//		CompanyModel t = query.findUnique();
		
		//http://www.avaje.org/static/javadoc/pub/com/avaje/ebean/Query.html
		CompanyModel t = Ebean.find(CompanyModel.class).fetch("phone").where().eq("id", id).findUnique();		
		
//		CompanyModel t = CompanyModel.find.fetch("phone").where(String.format("id=%d",id)).findUnique();
		if (t == null)
		{
			return null;
		}
				
		t.entity = createEntityFromModel(t); //create entity, set m.cc and t.entity, copy all fields from model to entity
		return t.entity;
	}

	@Override
	public List<Company> all() 
	{
		List<CompanyModel> L = CompanyModel.all();
		List<Company> entityL = createEntityFromModel(L);
		return entityL;
	}

	@Override
	public int size() 
	{
		return CompanyModel.all().size();
	}

	@Override
	public void delete(long id) 
	{
		CompanyModel t = CompanyModel.find.byId(id);
		t.delete();
	}

	//Company
	//create model, set entity, and call all setters
	public static CompanyModel createModelFromEntity(Company entity)
	{
		CompanyModel t = new CompanyModel();
		entity.cc = t;
		t.entity = entity;
		t.setId(entity.id);
		t.setName(entity.name);
		//email later!!
		return t;
	}
	//create entity, set m.cc and t.entity, copy all fields from model to entity
	public static Company createEntityFromModel(CompanyModel t)
	{
		if (t.entity != null && t.entity.cc != null)
		{
			return t.entity; //already exists
		}
		Company entity = new Company();
		entity.cc = t;
		t.entity = entity;
		entity.id = (t.getId() == null) ? 0 : t.getId();
		entity.name	= t.getName();
		//!email!!
		return entity;
	}
	public static List<Company> createEntityFromModel(List<CompanyModel> L)
	{
		ArrayList<Company> entityL = new ArrayList<Company>();
		for(CompanyModel t : L)
		{
			Company entity = createEntityFromModel(t);
			entityL.add(entity);
		}
		return entityL;
	}


	@Override
	public void updateFrom(IFormBinder binder) 
	{
		CompanyModel model = (CompanyModel) binder.getRawObject();
		model.update();
	}


	@Override
	public void update(Company entity) 
	{
		CompanyModel t = (CompanyModel)entity.cc; 
		if (t == null) //not yet known by db? (newly created)
		{
			t.entity = null; //throw exception
		}
		else //touch all (for ebean), except id
		{
			t.setName(entity.name); //copy all!!
		}
		t.update();
	}


	@Override
	public Company find_by_name(String val) 
	{
		CompanyModel t = Ebean.find(CompanyModel.class).where().eq("name", val).findUnique();
		if (t == null)
		{
			return null;
		}
		Company entity = createEntityFromModel(t);
		return entity;
	}
	
}

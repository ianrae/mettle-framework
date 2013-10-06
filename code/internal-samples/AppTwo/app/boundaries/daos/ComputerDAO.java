//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.
package boundaries.daos;


import java.util.ArrayList;
import java.util.List;

import mef.entities.Computer;
import mef.gen.ComputerDAO_GEN;
import mef.presenters.MyPage;
import models.ComputerModel;

import com.avaje.ebean.Page;
public class ComputerDAO extends ComputerDAO_GEN
{


	public Page<Computer> page(int page, int pageSize)
	{
		String filter = "";
		String sortBy = "name";
		String order = "asc";
		
		Page<ComputerModel> pagex = 
	            ComputerModel.find.where()
	                .ilike("name", "%" + filter + "%")
	                .orderBy(sortBy + " " + order)
	                
	                .fetch("company")
	                .findPagingList(pageSize)
	                .getPage(page - 1); //convert to 0-based
		
		List<Computer> L = createEntityFromModel(pagex.getList());
		Page<Computer> resultPage = new MyPage<Computer>(L, pageSize, page);
		return resultPage;
	}

}

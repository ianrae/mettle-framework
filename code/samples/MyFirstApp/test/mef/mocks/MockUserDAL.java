//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.mocks;

import java.util.List;
import java.util.ArrayList;
import mef.entities.*;
import mef.gen.MockUserDAL_GEN;
import mef.dals.*;
import org.mef.framework.binder.IFormBinder;
public class MockUserDAL extends MockUserDAL_GEN
{
	@Override
	public List<User> search_by_name(String name)
	{
		List<User> resultsL = new ArrayList<User>();
		
		for(User entity : resultsL)
		{
			if (entity.name.equals(name))
			{
				resultsL.add(entity);
			}
		}	
		return resultsL;
	}

}

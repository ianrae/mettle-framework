//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.dals.mocks;

import java.util.List;
import java.util.ArrayList;
import mef.entities.*;
import mef.gen.MockUserDAO_GEN;
import mef.dals.*;
import org.mef.framework.binder.IFormBinder;
import org.mef.framework.entitydb.EntityDB;
public class MockUserDAO extends MockUserDAO_GEN
{
	@Override
	public List<User> search_by_name(String name)
	{
		EntityDB<User> db = new EntityDB<User>();
		List<User> resultsL = db.findMatches(_L, "name", name);
		return resultsL;
	}

}

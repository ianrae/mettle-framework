//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.daos.mocks;

import java.util.List;

import org.mef.framework.entitydb.IValueMatcher;

import mef.entities.Computer;
import mef.gen.MockComputerDAO_GEN;
import mef.presenters.MyPage;

import com.avaje.ebean.Page;
public class MockComputerDAO extends MockComputerDAO_GEN
{

	//method
	@Override
	public Page<Computer> page(int page, int pageSize, String orderBy, String filter)
	{
		filter = (filter == null) ? "" : filter;
		List<Computer> list1 = _entityDB.findMatches(_L, "name", filter, IValueMatcher.ILIKE);
		MyPage<Computer> pg = new MyPage<Computer>(list1, pageSize, page); //page is 1-based
		return pg;
	}

}

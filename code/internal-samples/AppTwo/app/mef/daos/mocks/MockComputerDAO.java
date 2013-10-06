//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.daos.mocks;

import mef.entities.Computer;
import mef.gen.MockComputerDAO_GEN;
import mef.presenters.MyPage;

import com.avaje.ebean.Page;
public class MockComputerDAO extends MockComputerDAO_GEN
{

	//method
	public Page<Computer> page(int page, int pageSize)
	{
		MyPage<Computer> pg = new MyPage<Computer>(all(), pageSize, page); //page is 1-based
		return pg;
	}

}

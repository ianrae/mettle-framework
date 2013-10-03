package mef.daos.mocks;

import java.util.List;
import java.util.ArrayList;
import mef.entities.*;
import mef.gen.MockTaskDAO_GEN;

public class MockTaskDAO extends MockTaskDAO_GEN
{
	public boolean _dbDown;
	
	@Override
	public List<Task> all() 
	{
		if (_dbDown)
		{
			return null;
		}
		return super.all();
	}
}

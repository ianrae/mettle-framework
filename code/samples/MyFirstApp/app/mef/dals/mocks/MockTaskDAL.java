package mef.dals.mocks;

import java.util.List;
import java.util.ArrayList;
import mef.entities.*;
import mef.gen.MockTaskDAL_GEN;

public class MockTaskDAL extends MockTaskDAL_GEN
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

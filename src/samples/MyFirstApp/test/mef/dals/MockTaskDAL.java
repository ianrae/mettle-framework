package mef.dals;

import java.util.List;
import java.util.ArrayList;
import mef.entities.*;

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

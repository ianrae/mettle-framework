package mef.dals;

import java.util.ArrayList;
import java.util.List;

import mef.entities.Task;

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

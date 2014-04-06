package org.mef.framework.entities;

public class Entity 
{
	protected Object mRawModel;

	public Object getUnderlyingModel()
    {
    	return mRawModel;
    }
	
	//Derived classes can override this to do ad-hoc entity validation
	public String validate()
	{
		return null;
	}

}

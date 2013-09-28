package org.mef.framework.entities;

public class Entity 
{
	public Object cc;
	
	//Derived classes can override this to do ad-hoc entity validation
	public String validate()
	{
		return null;
	}

}

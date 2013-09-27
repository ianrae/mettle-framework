package mef.entities;

import org.mef.framework.entities.Entity;


public class User extends Entity
{
    public long id;

    public String name;
    public String email;

    public String validate() 
    {
    	if (name != null && name.equals("bob"))
    	{
    		return "no bobs allowed";
    	}
        return null;
    }
}



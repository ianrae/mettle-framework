//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.entities;

import org.mef.framework.entities.Entity;

public class User extends User_GEN
{
	public String validate() 
	{
		if (name != null && name.equals("bob"))
		{
			return "no bobs allowed";
		}
		return null;
	}
}

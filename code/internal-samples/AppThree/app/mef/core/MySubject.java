package mef.core;

import org.mef.framework.auth.AuthSubject;
import org.mef.framework.auth.IAuthSubject;

public class MySubject implements IAuthSubject
{
	AuthSubject entity;
	public MySubject(AuthSubject entity)
	{
		this.entity = entity;
	}

	@Override
	public boolean isLoggedIn() 
	{
		return true;
	}

	@Override
	public String getName() 
	{
		return entity.name;
	}

	@Override
	public Object getUserObject() 
	{
		return entity;
	}

	public static IAuthSubject createFrom(AuthSubject entity)
	{
		if (entity == null)
		{
			return null;
		}
		MySubject subj = new MySubject(entity);
		return subj;
		
	}
}

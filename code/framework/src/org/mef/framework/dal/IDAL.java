package org.mef.framework.dal;


import org.mef.framework.binder.IFormBinder;

public interface IDAL 
{
	int size();
	void delete(long id);
    void updateFrom(IFormBinder binder);        
}
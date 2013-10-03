package org.mef.framework.dao;


import org.mef.framework.binder.IFormBinder;

public interface IDAO 
{
	int size();
	void delete(long id);
    void updateFrom(IFormBinder binder);        
}

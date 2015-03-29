package org.mef.framework.dao;


import org.mef.framework.sfx.SfxContext;

public interface IDAO 
{
	void init(SfxContext ctx);
	int size();
	void delete(long id);
//    void updateFrom(IFormBinder binder);        //!!get rid of this
}

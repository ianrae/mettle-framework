package org.mef.framework.dao;

//models that have a Long Id can use this
public interface IHasLongId 
{
	Long getId();
	void setId(Long val);
}

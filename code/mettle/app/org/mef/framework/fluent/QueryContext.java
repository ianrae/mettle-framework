package org.mef.framework.fluent;
import java.util.List;

import org.mef.framework.dao.IDAO;
import org.mef.framework.sfx.SfxServiceLocator;



public class QueryContext<T>
{
	public List<QStep> queryL;
	public IQueryActionProcessor<T> proc;
	
	private SfxServiceLocator daoRegistry = new SfxServiceLocator();
	
	public QueryContext()
	{
	}
	
	public void registerDao(Class clazz, IDAO dao)
	{
		this.daoRegistry.registerSingleton(clazz, dao);
	}
	
	public IDAO findDao(Class clazz)
	{
		return (IDAO) this.daoRegistry.getInstance(clazz);
	}
}
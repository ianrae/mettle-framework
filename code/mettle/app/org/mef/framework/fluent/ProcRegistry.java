package org.mef.framework.fluent;

import org.mef.framework.sfx.SfxServiceLocator;

public class ProcRegistry 
{
	private SfxServiceLocator registry = new SfxServiceLocator();
	
	public ProcRegistry()
	{
	}
	
	public void registerDao(Class clazz, IQueryActionProcessor proc)
	{
		this.registry.registerSingleton(clazz, proc);
	}
	
	public IQueryActionProcessor findProc(Class clazz)
	{
		return (IQueryActionProcessor) this.registry.getInstance(clazz);
	}
}

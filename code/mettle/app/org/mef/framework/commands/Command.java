package org.mef.framework.commands;

import java.util.Map;

import org.mef.framework.binder.IFormBinder;

public class Command 
{
	private Map<String, String> map;
	private IFormBinder binder;

	public Command()
	{}
	
	public void setParameters(Map<String, String> map)
	{
		this.map = map;
	}
	public String getParameter(String name)
	{
		return this.map.get(name);
	}
	
	public IFormBinder getFormBinder()
	{
		return binder;
	}
	public void setFormBinder(IFormBinder binder)
	{
		this.binder = binder;
	}
}

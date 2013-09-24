package org.mef.framework.commands;

import java.util.Map;

public class Command 
{
	private Map<String, String> map;

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
}

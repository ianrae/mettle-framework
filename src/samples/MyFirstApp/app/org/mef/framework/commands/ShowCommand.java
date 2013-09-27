package org.mef.framework.commands;

public class ShowCommand extends Command
{
	public long id; //entity id to be be deleted
	
	public ShowCommand(Long id)
	{
		this.id = id;
	}
	
}

package org.mef.framework.commands;

public class DeleteCommand extends Command
{
	public long id; //entity id to be be deleted
	
	public DeleteCommand(Long id)
	{
		this.id = id;
	}
	
}

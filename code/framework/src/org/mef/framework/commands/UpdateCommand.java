package org.mef.framework.commands;

public class UpdateCommand extends Command 
{
	public long id; //entity id to be be deleted
	
	public UpdateCommand(Long id)
	{
		this.id = id;
	}
	
}

package org.mef.framework.commands;

public class EditCommand extends Command
{
	public long id; //entity id to be be deleted
	
	public EditCommand(Long id)
	{
		this.id = id;
	}
	
}

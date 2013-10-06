package mef.presenters.commands;

import org.mef.framework.commands.IndexCommand;

public class IndexComputerCommand extends IndexCommand 
{
	public int pageSize;
	public int pageNum; //1-based
	
	public IndexComputerCommand()
	{
		pageSize = 4;
		pageNum = 1;
	}

}

package mef.presenters.commands;

import org.mef.framework.commands.IndexCommand;

public class IndexBlogCommand extends IndexCommand 
{
	public String identityId; //some sort of string that SecureSocial sets. null means not authenticated
	
	public IndexBlogCommand()
	{
	}

	public IndexBlogCommand(String identityId)
	{
		this.identityId = identityId;
		
	}
}

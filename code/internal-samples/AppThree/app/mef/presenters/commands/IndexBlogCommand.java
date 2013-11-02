package mef.presenters.commands;

import mef.core.MySubject;
import mef.entities.AuthSubject;

import org.mef.framework.commands.IndexCommand;

public class IndexBlogCommand extends IndexCommand 
{
	public IndexBlogCommand()
	{
	}

	public IndexBlogCommand(AuthSubject entity)
	{
		this.authSubject = MySubject.createFrom(entity);
	}
}

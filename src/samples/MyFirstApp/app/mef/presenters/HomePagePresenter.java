package mef.presenters;

import org.mef.framework.Logger;
import org.mef.framework.binder.IFormBinder;
import org.mef.framework.commands.CreateCommand;
import org.mef.framework.commands.DeleteCommand;
import org.mef.framework.commands.IndexCommand;
import org.mef.framework.commands.Command;
import org.mef.framework.presenters.Presenter;
import org.mef.framework.replies.Reply;
import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;

import mef.dals.ITaskDAL;
import mef.entities.Task;

public class HomePagePresenter extends Presenter
{
	private ITaskDAL _dal;

	public HomePagePresenter(SfxContext ctx)
	{
		super(ctx); 
		_dal = (ITaskDAL) getInstance(ITaskDAL.class);
	}
	
	public HomePageReply onIndexCommand(IndexCommand cmd)
	{
		HomePageReply resp = new HomePageReply();
		return fillPage(resp);
	}

	public HomePageReply onCreateCommand(CreateCommand cmd)
	{
		HomePageReply reply = new HomePageReply();
		
		IFormBinder binder = cmd.getFormBinder();
		if (! binder.bind())
		{
			reply.setFlash("binding failed!");
		}
		else
		{
			Task entity = binder.getObject();
			if (entity == null)
			{
				reply.setFailed(true);
			}
			else
			{
				_dal.save(entity);
				Logger.info("saved new");
				reply.setForward("index");
			}
		}

		return fillPage(reply);
	}
	public HomePageReply onDeleteCommand(DeleteCommand cmd)
	{
		HomePageReply reply = new HomePageReply();
		
		Task t = _dal.findById(cmd.id);
		if (t == null)
		{
			reply.setForward("somewhere");
			reply.setFlash("could not find task");
		}
		
		_dal.delete(cmd.id);

		return fillPage(reply);
	}
	
	private HomePageReply fillPage(HomePageReply reply)
	{
		Logger.info("hey in fill!!");
		reply._allL = _dal.all();
		if (reply._allL == null)
		{
			reply.setFailed(true);
		}
		return reply;
	}

}

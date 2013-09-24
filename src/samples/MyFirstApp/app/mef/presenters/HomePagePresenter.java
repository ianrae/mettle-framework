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
	private HomePageReply _reply;
	
	public HomePagePresenter(SfxContext ctx)
	{
		super(ctx); 
		_dal = (ITaskDAL) getInstance(ITaskDAL.class);
	}
	
	@Override
	protected Reply createReply()
	{
		_reply = new HomePageReply();
		return _reply;
	}
	public HomePageReply onIndexCommand(IndexCommand cmd)
	{
		return fillPage();
	}

	public HomePageReply onCreateCommand(CreateCommand cmd)
	{
		IFormBinder binder = cmd.getFormBinder();
		if (! binder.bind())
		{
			_reply.setFlash("binding failed!");
		}
		else
		{
			Task entity = (Task) binder.getObject();
			if (entity == null)
			{
				_reply.setFailed(true);
			}
			else
			{
				_dal.save(entity);
				Logger.info("saved new, ID: " + entity.id);
				_reply.setForward("index");
				return _reply; //no need to fillpage 'cause redirecting
			}
		}

		return fillPage();
	}
	public HomePageReply onDeleteCommand(DeleteCommand cmd)
	{
		Task t = _dal.findById(cmd.id);
		if (t == null)
		{
			_reply.setForward("somewhere");
			_reply.setFlash("could not find task");
		}
		else
		{
			_dal.delete(cmd.id);
		}

		return fillPage();
	}
	
	private HomePageReply fillPage()
	{
		Logger.info("hey in fill!!");
		_reply._allL = _dal.all();
		if (_reply._allL == null)
		{
			_reply.setFailed(true);
		}
		return _reply;
	}

}

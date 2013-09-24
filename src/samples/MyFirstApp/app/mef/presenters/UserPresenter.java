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

import mef.dals.IUserDAL;
import mef.entities.User;

public class UserPresenter extends Presenter
{
	private IUserDAL _dal;

	public UserPresenter(SfxContext ctx)
	{
		super(ctx); 
		_dal = (IUserDAL) getInstance(IUserDAL.class);
	}
	
	public UserReply onIndexCommand(IndexCommand cmd)
	{
		UserReply resp = new UserReply();
		return fillPage(resp);
	}

	public UserReply onCreateCommand(CreateCommand cmd)
	{
		UserReply reply = new UserReply();
		
		IFormBinder binder = cmd.getFormBinder();
		if (! binder.bind())
		{
			reply.setFlash("binding failed!");
		}
		else
		{
			User entity = (User) binder.getObject();
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
	public UserReply onDeleteCommand(DeleteCommand cmd)
	{
		UserReply reply = new UserReply();
		
		User t = _dal.findById(cmd.id);
		if (t == null)
		{
			reply.setForward("somewhere");
			reply.setFlash("could not find task");
		}
		else
		{
			_dal.delete(cmd.id);
		}

		return fillPage(reply);
	}
	
	private UserReply fillPage(UserReply reply)
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

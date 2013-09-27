package mef.presenters;

import java.util.List;

import org.mef.framework.Logger;
import org.mef.framework.binder.IFormBinder;
import org.mef.framework.commands.CreateCommand;
import org.mef.framework.commands.DeleteCommand;
import org.mef.framework.commands.EditCommand;
import org.mef.framework.commands.IndexCommand;
import org.mef.framework.commands.Command;
import org.mef.framework.commands.NewCommand;
import org.mef.framework.commands.ShowCommand;
import org.mef.framework.commands.UpdateCommand;
import org.mef.framework.presenters.Presenter;
import org.mef.framework.replies.Reply;
import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;

import mef.dals.IUserDAL;
import mef.entities.User;
import mef.presenters.replies.UserReply;

public class UserPresenter extends Presenter
{
	private IUserDAL _dal;
	private UserReply _reply;

	public UserPresenter(SfxContext ctx)
	{
		super(ctx); 
		_dal = (IUserDAL) getInstance(IUserDAL.class);
	}
	@Override
	protected UserReply createReply()
	{
		_reply = new UserReply();
		return _reply;
	}
	
	public UserReply onIndexCommand(IndexCommand cmd)
	{
		UserReply reply = createReply(); //
		reply.setDestination(Reply.VIEW_INDEX);
		return fillPage(reply);
	}

	public UserReply onNewCommand(NewCommand cmd)
	{
		UserReply reply = createReply();//Reply.VIEW_NEW);
		reply.setDestination(Reply.VIEW_NEW);
		reply._entity = new User();
		//default vals
		reply._entity.name = "defaultname";
		return reply; //don't add list
	}

	public UserReply onCreateCommand(CreateCommand cmd)
	{
		UserReply reply = new UserReply();
		reply.setDestination(Reply.VIEW_NEW);
		
		IFormBinder binder = cmd.getFormBinder();
		if (! binder.bind())
		{
			reply.setFlashFail("binding failed!");
			Logger.info("BINDING failed");
			reply._entity = (User) binder.getObject();
			return reply;
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
				reply.setFlashSuccess("created user " + entity.name);
				reply.setDestination(Reply.FORWARD_INDEX);
			}
			return fillPage(reply); //for now!!
		}
	}
	
	public UserReply onEditCommand(EditCommand cmd)
	{
		UserReply reply = new UserReply();
		reply.setDestination(Reply.VIEW_EDIT);
		
		User user = _dal.findById(cmd.id);
		if (user == null)
		{
			reply.setDestination(Reply.FORWARD_NOT_FOUND);
			return reply;
		}
		else
		{
			reply._entity = user;
			return reply;
		}
	}
	public UserReply onUpdateCommand(UpdateCommand cmd)
	{
		UserReply reply = new UserReply();
		reply.setDestination(Reply.VIEW_EDIT);
		
		IFormBinder binder = cmd.getFormBinder();
		if (! binder.bind())
		{
			reply.setFlashFail("binding failed!");
			reply._entity = (User) binder.getObject();
			if (reply._entity == null)
			{
				Logger.info("failbinding null entity!");
				reply._entity = _dal.findById(cmd.id); //fix better later!!
			}
			return reply;
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
				User user = _dal.findById(cmd.id);
				if (user == null)
				{
					reply.setDestination(Reply.FORWARD_NOT_FOUND);
					return reply;
				}
				else
				{
					user.name = entity.name;
					Logger.info("userID: " + user.id);
					_dal.save(user);
					Logger.info("xsaved update: " + user.name);
					reply.setDestination(Reply.FORWARD_INDEX);
				}
			}
			return fillPage(reply);
		}
	}
	
	
	public UserReply onDeleteCommand(DeleteCommand cmd)
	{
		UserReply reply = new UserReply();
		reply.setDestination(Reply.FORWARD_INDEX);
		
		User t = _dal.findById(cmd.id);
		if (t == null)
		{
			reply.setDestination(Reply.FORWARD_NOT_FOUND);
			reply.setFlashFail("could not find task");
		}
		else
		{
			_dal.delete(cmd.id);
		}

		return fillPage(reply);
	}
	
	public UserReply onShowCommand(ShowCommand cmd)
	{
		UserReply reply = new UserReply();
		reply.setDestination(Reply.VIEW_SHOW);
		
		User user = _dal.findById(cmd.id);
		if (user == null)
		{
			reply.setDestination(Reply.FORWARD_NOT_FOUND);
			return reply;
		}
		else
		{
			reply._entity = user;
			return reply;
		}
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

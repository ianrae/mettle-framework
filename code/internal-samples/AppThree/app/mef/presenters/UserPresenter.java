//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.presenters;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.mef.framework.Logger;
import org.mef.framework.auth.AuthSubject;
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

import mef.daos.IAuthSubjectDAO;
import mef.presenters.replies.UserReply;
public class UserPresenter extends Presenter
{
	private IAuthSubjectDAO _dao;
	private UserReply _reply;

	public UserPresenter(SfxContext ctx)
	{
		super(ctx); 
		_dao = (IAuthSubjectDAO) getInstance(IAuthSubjectDAO.class);
	}
	@Override
	protected UserReply createReply()
	{
		_reply = new UserReply();
		return _reply;
	}

	public UserReply onIndexCommand(IndexCommand cmd)
	{
		UserReply reply = createReply(); 
		reply.setDestination(Reply.VIEW_INDEX);
		reply._allL = _dao.all();
		return reply;
	}

	public UserReply onNewCommand(NewCommand cmd)
	{
		UserReply reply = createReply();
		reply.setDestination(Reply.VIEW_NEW);
		reply._entity = new AuthSubject();
		return reply; 
	}

	public UserReply onCreateCommand(CreateCommand cmd)
	{
		UserReply reply = createReply();
		reply.setDestination(Reply.VIEW_NEW);

		IFormBinder binder = cmd.getFormBinder();
		if (! binder.bind())
		{
			reply.setFlashFail("binding failed!");
			reply._entity = (AuthSubject) binder.getObject();
			return reply;
		}
		else
		{
			AuthSubject entity = (AuthSubject) binder.getObject();
			if (entity == null)
			{
				reply.setFailed(true);
			}
			else
			{
				_dao.save(entity);
				Logger.info("saved new");
				reply.setFlashSuccess("created entity");
				reply.setDestination(Reply.FORWARD_INDEX);
			}
			return reply;
		}
	}

	public UserReply onEditCommand(EditCommand cmd)
	{
		UserReply reply = createReply();
		reply.setDestination(Reply.VIEW_EDIT);

		AuthSubject entity = _dao.findById(cmd.id);
		if (entity == null)
		{
			reply.setDestination(Reply.FORWARD_NOT_FOUND);
			return reply;
		}
		else
		{
			reply._entity = entity;
			return reply;
		}
	}
	public UserReply onUpdateCommand(UpdateCommand cmd)
	{
		UserReply reply = createReply();
		reply.setDestination(Reply.VIEW_EDIT);
		IFormBinder binder = cmd.getFormBinder();
		if (! binder.bind())
		{
			reply.setFlashFail("binding failed!");
			reply._entity = (AuthSubject) binder.getObject();
			if (reply._entity == null)
			{
				Logger.info("failbinding null entity!");
				reply._entity = _dao.findById(cmd.id); //fix better later!!
			}
			return reply;
		}
		else
		{
			//ensure id is a valid id
			AuthSubject entity = _dao.findById(cmd.id);
			if (entity == null)
			{
				reply.setDestination(Reply.FORWARD_NOT_FOUND);
				return reply;
			}
			_dao.updateFrom(binder);
			Logger.info("saved update ");
			reply.setDestination(Reply.FORWARD_INDEX);
			return reply;
		}
	}


	public UserReply onDeleteCommand(DeleteCommand cmd)
	{
		UserReply reply = createReply();
		reply.setDestination(Reply.FORWARD_INDEX);
		AuthSubject entity = _dao.findById(cmd.id);
		if (entity == null)
		{
			reply.setDestination(Reply.FORWARD_NOT_FOUND);
			reply.setFlashFail("could not find entity");
		}
		else
		{
			_dao.delete(cmd.id);
		}
		return reply;
	}

	public UserReply onShowCommand(ShowCommand cmd)
	{
		UserReply reply = createReply();
		reply.setDestination(Reply.VIEW_SHOW);
		AuthSubject entity = _dao.findById(cmd.id);
		if (entity == null)
		{
			reply.setDestination(Reply.FORWARD_NOT_FOUND);
			reply.setFlashFail("could not find entity");
		}
		else
		{
			reply._entity = entity;
		}
		return reply;
	}





}

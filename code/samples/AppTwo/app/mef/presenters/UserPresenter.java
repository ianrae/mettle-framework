//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.presenters;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

import mef.daos.IUserDAO;
import mef.entities.User;
import mef.presenters.replies.UserReply;
public class UserPresenter extends Presenter
{
	private IUserDAO _dao;
	private UserReply _reply;

	public UserPresenter(SfxContext ctx)
	{
		super(ctx); 
		_dao = (IUserDAO) getInstance(IUserDAO.class);
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
		return reply;
	}

	public UserReply onNewCommand(NewCommand cmd)
	{
		UserReply reply = createReply();
		reply.setDestination(Reply.VIEW_NEW);
		return reply; 
	}

	public UserReply onCreateCommand(CreateCommand cmd)
	{
		UserReply reply = createReply();
		reply.setDestination(Reply.VIEW_NEW);
		return reply;
	}

	public UserReply onEditCommand(EditCommand cmd)
	{
		UserReply reply = createReply();
		reply.setDestination(Reply.VIEW_EDIT);
		return reply;
	}
	public UserReply onUpdateCommand(UpdateCommand cmd)
	{
		UserReply reply = createReply();
		reply.setDestination(Reply.VIEW_EDIT);
		return reply;
	}


	public UserReply onDeleteCommand(DeleteCommand cmd)
	{
		UserReply reply = createReply();
		reply.setDestination(Reply.FORWARD_INDEX);
		return reply;
	}

	public UserReply onShowCommand(ShowCommand cmd)
	{
		UserReply reply = createReply();
		reply.setDestination(Reply.VIEW_SHOW);
		return reply;
	}





}

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

import mef.daos.IRoleDAO;
import mef.entities.Role;
import mef.presenters.replies.RoleReply;
public class RolePresenter extends Presenter
{
	private IRoleDAO _dao;
	private RoleReply _reply;

	public RolePresenter(SfxContext ctx)
	{
		super(ctx); 
		_dao = (IRoleDAO) getInstance(IRoleDAO.class);
	}
	@Override
	protected RoleReply createReply()
	{
		_reply = new RoleReply();
		return _reply;
	}

	public RoleReply onIndexCommand(IndexCommand cmd)
	{
		RoleReply reply = createReply(); 
		reply.setDestination(Reply.VIEW_INDEX);
		reply._allL = _dao.all();
		return reply;
	}

	public RoleReply onNewCommand(NewCommand cmd)
	{
		RoleReply reply = createReply();
		reply.setDestination(Reply.VIEW_NEW);
		reply._entity = new Role();
		return reply; 
	}

	public RoleReply onCreateCommand(CreateCommand cmd)
	{
		RoleReply reply = createReply();
		reply.setDestination(Reply.VIEW_NEW);

		IFormBinder binder = cmd.getFormBinder();
		if (! binder.bind())
		{
			reply.setFlashFail("binding failed!");
			reply._entity = (Role) binder.getObject();
			return reply;
		}
		else
		{
			Role entity = (Role) binder.getObject();
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

	public RoleReply onEditCommand(EditCommand cmd)
	{
		RoleReply reply = createReply();
		reply.setDestination(Reply.VIEW_EDIT);

		Role entity = _dao.findById(cmd.id);
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
	public RoleReply onUpdateCommand(UpdateCommand cmd)
	{
		RoleReply reply = createReply();
		reply.setDestination(Reply.VIEW_EDIT);
		IFormBinder binder = cmd.getFormBinder();
		if (! binder.bind())
		{
			reply.setFlashFail("binding failed!");
			reply._entity = (Role) binder.getObject();
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
			Role entity = _dao.findById(cmd.id);
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


	public RoleReply onDeleteCommand(DeleteCommand cmd)
	{
		RoleReply reply = createReply();
		reply.setDestination(Reply.FORWARD_INDEX);
		Role entity = _dao.findById(cmd.id);
		if (entity == null)
		{
			reply.setDestination(Reply.FORWARD_NOT_FOUND);
			reply.setFlashFail("could not find Role");
		}
		else
		{
			_dao.delete(cmd.id);
		}
		return reply;
	}

	public RoleReply onShowCommand(ShowCommand cmd)
	{
		RoleReply reply = createReply();
		reply.setDestination(Reply.VIEW_SHOW);
		Role entity = _dao.findById(cmd.id);
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

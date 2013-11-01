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

import mef.core.DaoFinder;
import mef.core.Initializer;
import mef.daos.IAuthRoleDAO;
import mef.daos.IAuthSubjectDAO;
import mef.daos.IAuthTicketDAO;
import mef.daos.IBlogDAO;
import mef.entities.Blog;
import mef.presenters.commands.IndexBlogCommand;
import mef.presenters.replies.BlogReply;
public class BlogPresenter extends Presenter
{
	private IBlogDAO _dao;
	private BlogReply _reply;

	public BlogPresenter(SfxContext ctx)
	{
		super(ctx); 
		_dao = (IBlogDAO) getInstance(IBlogDAO.class);
	}
	@Override
	protected BlogReply createReply()
	{
		_reply = new BlogReply();
		return _reply;
	}

	public BlogReply onIndexBlogCommand(IndexBlogCommand cmd)
	{
		BlogReply reply = createReply();
		if (! userHasRole(cmd, "Full"))
		{
			return reply;
		}
		
		reply.setDestination(Reply.VIEW_INDEX);
		reply._allL = _dao.all();
		return reply;
	}
	
	private boolean userHasRole(IndexBlogCommand cmd, String roleName)
	{
		if (cmd.identityId == null || cmd.identityId.isEmpty())
		{
			_reply.setDestination(Reply.FOWARD_NOT_AUTHENTICATED);
			return false;
		}
		
		if (! hasRole(cmd, roleName))
		{
			_reply.setDestination(Reply.FOWARD_NOT_AUTHORIZED);
			return false;
		}
		return true;
	}

	public BlogReply onNewCommand(NewCommand cmd)
	{
		BlogReply reply = createReply();
		reply.setDestination(Reply.VIEW_NEW);
		reply._entity = new Blog();
		return reply; 
	}

	public BlogReply onCreateCommand(CreateCommand cmd)
	{
		BlogReply reply = createReply();
		reply.setDestination(Reply.VIEW_NEW);

		IFormBinder binder = cmd.getFormBinder();
		if (! binder.bind())
		{
			reply.setFlashFail("binding failed!");
			reply._entity = (Blog) binder.getObject();
			return reply;
		}
		else
		{
			Blog entity = (Blog) binder.getObject();
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

	public BlogReply onEditCommand(EditCommand cmd)
	{
		BlogReply reply = createReply();
		reply.setDestination(Reply.VIEW_EDIT);

		Blog entity = _dao.findById(cmd.id);
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
	public BlogReply onUpdateCommand(UpdateCommand cmd)
	{
		BlogReply reply = createReply();
		reply.setDestination(Reply.VIEW_EDIT);
		IFormBinder binder = cmd.getFormBinder();
		if (! binder.bind())
		{
			reply.setFlashFail("binding failed!");
			reply._entity = (Blog) binder.getObject();
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
			Blog entity = _dao.findById(cmd.id);
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


	public BlogReply onDeleteCommand(DeleteCommand cmd)
	{
		BlogReply reply = createReply();
		reply.setDestination(Reply.FORWARD_INDEX);
		Blog entity = _dao.findById(cmd.id);
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

	public BlogReply onShowCommand(ShowCommand cmd)
	{
		BlogReply reply = createReply();
		reply.setDestination(Reply.VIEW_SHOW);
		Blog entity = _dao.findById(cmd.id);
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

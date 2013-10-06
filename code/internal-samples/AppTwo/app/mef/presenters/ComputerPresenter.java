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

import mef.daos.IComputerDAO;
import mef.entities.Computer;
import mef.presenters.commands.IndexComputerCommand;
import mef.presenters.replies.ComputerReply;
public class ComputerPresenter extends Presenter
{
	private IComputerDAO _dao;
	private ComputerReply _reply;

	public ComputerPresenter(SfxContext ctx)
	{
		super(ctx); 
		_dao = (IComputerDAO) getInstance(IComputerDAO.class);
	}
	@Override
	protected ComputerReply createReply()
	{
		_reply = new ComputerReply();
		return _reply;
	}

	public ComputerReply onIndexComputerCommand(IndexComputerCommand cmd)
	{
		ComputerReply reply = createReply(); 
		reply.setDestination(Reply.VIEW_INDEX);
		
		MyPage<Computer> pg = new MyPage<Computer>(_dao.all(), cmd.pageSize, cmd.pageNum);
		
		reply._page = pg;
		return reply;
	}

	public ComputerReply onNewCommand(NewCommand cmd)
	{
		ComputerReply reply = createReply();
		reply.setDestination(Reply.VIEW_NEW);
		return reply; 
	}

	public ComputerReply onCreateCommand(CreateCommand cmd)
	{
		ComputerReply reply = createReply();
		reply.setDestination(Reply.VIEW_NEW);
		return reply;
	}

	public ComputerReply onEditCommand(EditCommand cmd)
	{
		ComputerReply reply = createReply();
		reply.setDestination(Reply.VIEW_EDIT);
		return reply;
	}
	public ComputerReply onUpdateCommand(UpdateCommand cmd)
	{
		ComputerReply reply = createReply();
		reply.setDestination(Reply.VIEW_EDIT);
		return reply;
	}


	public ComputerReply onDeleteCommand(DeleteCommand cmd)
	{
		ComputerReply reply = createReply();
		reply.setDestination(Reply.FORWARD_INDEX);
		return reply;
	}

	public ComputerReply onShowCommand(ShowCommand cmd)
	{
		ComputerReply reply = createReply();
		reply.setDestination(Reply.VIEW_SHOW);
		return reply;
	}











}

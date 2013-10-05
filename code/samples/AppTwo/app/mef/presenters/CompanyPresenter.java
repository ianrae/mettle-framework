//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.presenters;

import java.util.ArrayList;
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

import mef.daos.ICompanyDAO;
import mef.entities.Company;
import mef.presenters.replies.CompanyReply;
public class CompanyPresenter extends Presenter
{
	private ICompanyDAO _dao;
	private CompanyReply _reply;

	public CompanyPresenter(SfxContext ctx)
	{
		super(ctx); 
		_dao = (ICompanyDAO) getInstance(ICompanyDAO.class);
	}
	@Override
	protected CompanyReply createReply()
	{
		_reply = new CompanyReply();
		return _reply;
	}

	public CompanyReply onIndexCommand(IndexCommand cmd)
	{
		CompanyReply reply = createReply(); 
		reply.setDestination(Reply.VIEW_INDEX);
		reply._allL = new ArrayList<Company>();
		return reply;
	}

	public CompanyReply onNewCommand(NewCommand cmd)
	{
		CompanyReply reply = createReply();
		reply.setDestination(Reply.VIEW_NEW);
		return reply; 
	}

	public CompanyReply onCreateCommand(CreateCommand cmd)
	{
		CompanyReply reply = createReply();
		reply.setDestination(Reply.VIEW_NEW);
		return reply;
	}

	public CompanyReply onEditCommand(EditCommand cmd)
	{
		CompanyReply reply = createReply();
		reply.setDestination(Reply.VIEW_EDIT);
		return reply;
	}
	public CompanyReply onUpdateCommand(UpdateCommand cmd)
	{
		CompanyReply reply = createReply();
		reply.setDestination(Reply.VIEW_EDIT);
		return reply;
	}


	public CompanyReply onDeleteCommand(DeleteCommand cmd)
	{
		CompanyReply reply = createReply();
		reply.setDestination(Reply.FORWARD_INDEX);
		return reply;
	}

	public CompanyReply onShowCommand(ShowCommand cmd)
	{
		CompanyReply reply = createReply();
		reply.setDestination(Reply.VIEW_SHOW);
		return reply;
	}





}

package mef;

import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;
import org.mef.framework.commands.Command;
import org.mef.framework.commands.CreateCommand;
import org.mef.framework.commands.DeleteCommand;
import org.mef.framework.commands.EditCommand;
import org.mef.framework.commands.IndexCommand;
import org.mef.framework.commands.NewCommand;
import org.mef.framework.commands.ShowCommand;
import org.mef.framework.commands.UpdateCommand;
import org.mef.framework.presenters.Presenter;
import org.mef.framework.replies.Reply;
import org.mef.framework.sfx.SfxContext;

import mef.core.Initializer;
import mef.daos.ICompanyDAO;
import mef.daos.mocks.MockCompanyDAO;
import mef.entities.Company;
import mef.presenters.CompanyPresenter;
import mef.presenters.replies.CompanyReply;

public class CompanyPresenterTests extends BasePresenterTest
{

	//HTTP  URL             ACTION  FRM    VIEW/REDIR     
	//GET 	users/    		index          INDEX
	//GET 	users/new 		new            NEW
	//POST  users/new   	create  form   r:index, NEW(if validation fails)
	//GET   users/:id/edit  edit           EDIT, NOTFOUND(if invalid id)
	//POST  users/:id/edit  update  form   r:index, NOTFOUND(if invalid id), EDIT(if validation fails)
	//POST 	users/:id/delete delete		   r:index, NOTFOUND(if invalid id)
	//GET   users/:id       show           SHOW,NOTFOUND

	//--- index ---
	@Test
	public void indexTest() 
	{
		CompanyReply reply = (CompanyReply) _presenter.process(new IndexCommand());
		
		chkReplySucessful(reply, Reply.VIEW_INDEX, null);
		chkDalSize(0);
		chkReplyWithoutEntity(reply, true, 0);
	}

	@Test
	public void indexTestOne() 
	{
		Initializer.loadSeedData(_ctx);
		CompanyReply reply = (CompanyReply) _presenter.process(new IndexCommand());
		
		chkReplySucessful(reply, Reply.VIEW_INDEX, null);
		assertEquals("Apple Inc.", _dao.all().get(0).name);
		chkReplyWithoutEntity(reply, true, 42);
	}
	
	
	//--------- helper fns--------------
	protected void chkDalSize(int expected)
	{
		assertEquals(expected, _dao.size());
	}
	private void chkReplyWithEntity(CompanyReply reply, boolean listExists, int expected)
	{
		assertNotNull(reply._entity);
		if (listExists)
		{
			assertNotNull(reply._allL);
			assertEquals(expected, reply._allL.size());
		}
		else
		{
			assertNull(reply._allL);
		}
	}
	private void chkReplyWithoutEntity(CompanyReply reply, boolean listExists, int expected)
	{
		assertEquals(null, reply._entity);
		if (listExists)
		{
			assertNotNull(reply._allL);
			assertEquals(expected, reply._allL.size());
		}
		else
		{
			assertNull(reply._allL);
		}
	}
	
	private MockCompanyDAO _dao;
	private CompanyPresenter _presenter;
	@Before
	public void init()
	{
		super.init();
		_dao = getDAO();
		this._presenter = new CompanyPresenter(_ctx);
	}
	
	private MockCompanyDAO getDAO()
	{
		MockCompanyDAO dal = (MockCompanyDAO) Initializer.getDAO(ICompanyDAO.class); 
		return dal;
	}
	
	private Company initCompany()
	{
		Company t = new Company();
		t.id = 0L; //dal will assign id
		t.name = "task1";
		assertEquals(0, _dao.size());
		return t;
	}
	
	private Company initAndSaveCompany()
	{
		Company t = initCompany();
		_dao.save(t);
		assertEquals(1, _dao.size());
		return t;
	}
	private Company createCompany(String name)
	{
		Company u = new Company();
		u.name = name;
		return u;
	}
	
	
}

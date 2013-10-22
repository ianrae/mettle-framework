//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.
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
import mef.daos.IRoleDAO;
import mef.daos.mocks.MockRoleDAO;
import mef.entities.Role;
import mef.presenters.RolePresenter;
import mef.presenters.replies.RoleReply;

public class RolePresenterTests extends BasePresenterTest
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
		RoleReply reply = (RoleReply) _presenter.process(new IndexCommand());

		chkReplySucessful(reply, Reply.VIEW_INDEX, null);
		chkDalSize(0);
		chkReplyWithoutEntity(reply, true, 0);
	}

	@Test
	public void indexTestOne() 
	{
		Role u = createRole("bob");
		_dao.save(u);
		assertEquals("bob", _dao.all().get(0).name);
		RoleReply reply = (RoleReply) _presenter.process(new IndexCommand());

		chkReplySucessful(reply, Reply.VIEW_INDEX, null);
		chkDalSize(1);
		chkReplyWithoutEntity(reply, true, 1);
	}

	//--- new ---
	@Test
	public void testNewRole() 
	{
		RoleReply reply = (RoleReply) _presenter.process( new NewCommand());

		chkReplySucessful(reply, Reply.VIEW_NEW, null);
		assertEquals("defaultname", reply._entity.name);
		chkDalSize(0);
		chkReplyWithEntity(reply);
	}

	//--- create ---
	@Test
	public void testCreateRole() 
	{
		Role t = initRole();
		chkDalSize(0);
		Command cmd = createWithBinder(new CreateCommand(), t, true);

		RoleReply reply = (RoleReply) _presenter.process(cmd);

		chkReplySucessful(reply, Reply.FORWARD_INDEX, "created entity");
		chkDalSize(1);
		chkReplyForwardOnly(reply);
		t = _dao.findById(1);
		assertEquals(new Long(1L), t.id);
	}

	@Test
	public void testCreateRole_ValFail() 
	{
		Role t = initRole();
		Command cmd = createWithBinder(new CreateCommand(), t, false);

		RoleReply reply = (RoleReply) _presenter.process(cmd);

		chkReplySucessful(reply, Reply.VIEW_NEW, "binding failed!");
		chkDalSize(0);
		chkReplyWithEntity(reply);
	}

	//--- edit ---
	@Test
	public void testEditRole() 
	{
		Role t = initAndSaveRole();
		RoleReply reply = (RoleReply) _presenter.process(new EditCommand(t.id));

		chkReplySucessful(reply, Reply.VIEW_EDIT, null);
		chkDalSize(1);
		chkReplyWithEntity(reply);
	}
	@Test
	public void testEditRole_NotFound() 
	{
		Role t = initAndSaveRole();
		RoleReply reply = (RoleReply) _presenter.process(new EditCommand(99L));

		chkReplySucessful(reply, Reply.FORWARD_NOT_FOUND, null);
		chkDalSize(1);
		chkReplyForwardOnly(reply);
	}

	//--- update ---
	@Test
	public void testUpdateRole() 
	{
		Role t = initAndSaveRole();
		chkDalSize(1);
		t.name = "user2"; //simulate user edit
		Command cmd = createWithBinder(new UpdateCommand(t.id), t, true);

		RoleReply reply = (RoleReply) _presenter.process(cmd);

		chkReplySucessful(reply, Reply.FORWARD_INDEX, null);
		chkDalSize(1);
		chkReplyForwardOnly(reply);

		Role t2 = _dao.findById(t.id);
		assertEquals("user2", t2.name);
	}
	@Test
	public void testUpdateRole_ValFail() 
	{
		Role t = initAndSaveRole();
		t.name = "user2"; //simulate user edit
		Command cmd = createWithBinder(new UpdateCommand(t.id), t, false);

		RoleReply reply = (RoleReply) _presenter.process(cmd);

		chkReplySucessful(reply, Reply.VIEW_EDIT, "binding failed!");
		chkDalSize(1);
		chkReplyWithEntity(reply);

		Role t2 = _dao.findById(t.id);
		assertEquals("bob", t2.name); //unchanged (but mock dal kinda broken)
	}
	@Test
	public void testUpdateRole_NotFound() 
	{
		Role t = initAndSaveRole();
		Command cmd = createWithBinder(new UpdateCommand(99L), t, true);
		RoleReply reply = (RoleReply) _presenter.process(cmd);

		chkReplySucessful(reply, Reply.FORWARD_NOT_FOUND, null);
		chkDalSize(1);
		chkReplyForwardOnly(reply);
	}


	//--- delete ---
	@Test
	public void testDeleteRole() 
	{
		Role t = initAndSaveRole();
		RoleReply reply = (RoleReply) _presenter.process( new DeleteCommand(t.id));

		chkReplySucessful(reply, Reply.FORWARD_INDEX, null);
		chkDalSize(0);
		chkReplyForwardOnly(reply);
	}

	@Test
	public void testBadDeleteRole() 
	{
		Role t = initAndSaveRole();
		RoleReply reply = (RoleReply) _presenter.process(new DeleteCommand(99L)); //not exist

		chkReplySucessful(reply, Reply.FORWARD_NOT_FOUND, "could not find entity");
		chkDalSize(1);
		chkReplyForwardOnly(reply);
	}

	//--- show ---
	@Test
	public void testShowRole() 
	{
		Role t = initAndSaveRole();
		RoleReply reply = (RoleReply) _presenter.process(new ShowCommand(t.id));

		chkReplySucessful(reply, Reply.VIEW_SHOW, null);
		chkDalSize(1);
		chkReplyWithEntity(reply);
	}
	@Test
	public void testShowRole_NotFound() 
	{
		Role t = initAndSaveRole();
		RoleReply reply = (RoleReply) _presenter.process(new ShowCommand(99L));

		chkReplySucessful(reply, Reply.FORWARD_NOT_FOUND, "could not find entity");
		chkDalSize(1);
		chkReplyForwardOnly(reply);
	}


	//--------- helper fns--------------
	protected void chkDalSize(int expected)
	{
		assertEquals(expected, _dao.size());
	}
	private void chkReplyWithEntity(RoleReply reply)
	{
		chkReplyWithEntity(reply, false, 0);
	}
	private void chkReplyWithEntity(RoleReply reply, boolean listExists, int expected)
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
	private void chkReplyWithoutEntity(RoleReply reply, boolean listExists, int expected)
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
	private void chkReplyForwardOnly(RoleReply reply)
	{
		assertEquals(null, reply._entity);
		assertNull(reply._allL);
	}

	private MockRoleDAO _dao;
	private RolePresenter _presenter;
	@Before
	public void init()
	{
		super.init();
		_dao = getDAO();
		this._presenter = new RolePresenter(_ctx);
	}

	private MockRoleDAO getDAO()
	{
		MockRoleDAO dal = (MockRoleDAO) Initializer.getDAO(IRoleDAO.class); 
		return dal;
	}

	private Role initRole()
	{
		return initRole("bob");
	}
	private Role initRole(String name)
	{
		Role t = new Role();
		t.id = 0L; //dal will assign id
		t.name = name;
		assertEquals(0, _dao.size());
		return t;
	}

	private Role initAndSaveRole()
	{
		return initAndSaveRole("bob");
	}

	private Role initAndSaveRole(String name)
	{
		Role t = initRole(name);
		_dao.save(t);
		assertEquals(1, _dao.size());
		return _dao.findById(t.id);
	}
	private Role createRole(String name)
	{
		Role u = new Role();
		u.name = name;
		return u;
	}





}

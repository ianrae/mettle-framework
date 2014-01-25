package mef.presenter;

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

import mef.core.MettleInitializer;
import mef.daos.IUserDAO;
import mef.daos.mocks.MockUserDAO;
import mef.entities.User;
import mef.presenters.UserPresenter;
import mef.presenters.replies.UserReply;
import org.mef.framework.test.helpers.MockFormBinder;

public class UserPresenterTests extends BasePresenterTest
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
		UserReply reply = (UserReply) _presenter.process(new IndexCommand());

		chkReplySucessful(reply, Reply.VIEW_INDEX, null);
		chkDalSize(0);
		chkReplyWithoutEntity(reply, true, 0);
	}

	@Test
	public void indexTestOne() 
	{
		User u = createUser("bob");
		_dao.save(u);
		assertEquals("bob", _dao.all().get(0).name);
		UserReply reply = (UserReply) _presenter.process(new IndexCommand());

		chkReplySucessful(reply, Reply.VIEW_INDEX, null);
		chkDalSize(1);
		chkReplyWithoutEntity(reply, true, 1);
	}

	//--- new ---
	@Test
	public void testNewUser() 
	{
		UserReply reply = (UserReply) _presenter.process( new NewCommand());

		chkReplySucessful(reply, Reply.VIEW_NEW, null);
		chkDalSize(0);
		chkReplyWithEntity(reply);
	}

	//--- create ---
	@Test
	public void testCreateUser() 
	{
		User t = initUser();
		chkDalSize(0);
		Command cmd = createWithBinder(new CreateCommand(), t, true);

		UserReply reply = (UserReply) _presenter.process(cmd);

		chkReplySucessful(reply, Reply.FORWARD_INDEX, "created entity");
		chkDalSize(1);
		chkReplyForwardOnly(reply);
		t = _dao.findById(1);
		assertEquals(new Long(1L), t.id);
	}

	@Test
	public void testCreateUser_ValFail() 
	{
		User t = initUser();
		Command cmd = createWithBinder(new CreateCommand(), t, false);

		UserReply reply = (UserReply) _presenter.process(cmd);

		chkReplySucessful(reply, Reply.VIEW_NEW, "binding failed!");
		chkDalSize(0);
		chkReplyWithEntity(reply);
	}

	//--- edit ---
	@Test
	public void testEditUser() 
	{
		User t = initAndSaveUser();
		UserReply reply = (UserReply) _presenter.process(new EditCommand(t.id));

		chkReplySucessful(reply, Reply.VIEW_EDIT, null);
		chkDalSize(1);
		chkReplyWithEntity(reply);
	}
	@Test
	public void testEditUser_NotFound() 
	{
		User t = initAndSaveUser();
		UserReply reply = (UserReply) _presenter.process(new EditCommand(99L));

		chkReplySucessful(reply, Reply.FORWARD_NOT_FOUND, null);
		chkDalSize(1);
		chkReplyForwardOnly(reply);
	}

	//--- update ---
	@Test
	public void testUpdateUser() 
	{
		User t = initAndSaveUser();
		chkDalSize(1);
		t.name = "user2"; //simulate user edit
		Command cmd = createWithBinder(new UpdateCommand(t.id), t, true);

		UserReply reply = (UserReply) _presenter.process(cmd);

		chkReplySucessful(reply, Reply.FORWARD_INDEX, null);
		chkDalSize(1);
		chkReplyForwardOnly(reply);

		User t2 = _dao.findById(t.id);
		assertEquals("user2", t2.name);
	}
	@Test
	public void testUpdateUser_ValFail() 
	{
		User t = initAndSaveUser();
		t.name = "user2"; //simulate user edit
		Command cmd = createWithBinder(new UpdateCommand(t.id), t, false);

		UserReply reply = (UserReply) _presenter.process(cmd);

		chkReplySucessful(reply, Reply.VIEW_EDIT, "binding failed!");
		chkDalSize(1);
		chkReplyWithEntity(reply);

		User t2 = _dao.findById(t.id);
		assertEquals("bob", t2.name); //unchanged (but mock dal kinda broken)
	}
	@Test
	public void testUpdateUser_NotFound() 
	{
		User t = initAndSaveUser();
		Command cmd = createWithBinder(new UpdateCommand(99L), t, true);
		UserReply reply = (UserReply) _presenter.process(cmd);

		chkReplySucessful(reply, Reply.FORWARD_NOT_FOUND, null);
		chkDalSize(1);
		chkReplyForwardOnly(reply);
	}


	//--- delete ---
	@Test
	public void testDeleteUser() 
	{
		User t = initAndSaveUser();
		UserReply reply = (UserReply) _presenter.process( new DeleteCommand(t.id));

		chkReplySucessful(reply, Reply.FORWARD_INDEX, null);
		chkDalSize(0);
		chkReplyForwardOnly(reply);
	}

	@Test
	public void testBadDeleteUser() 
	{
		User t = initAndSaveUser();
		UserReply reply = (UserReply) _presenter.process(new DeleteCommand(99L)); //not exist

		chkReplySucessful(reply, Reply.FORWARD_NOT_FOUND, "could not find entity");
		chkDalSize(1);
		chkReplyForwardOnly(reply);
	}

	//--- show ---
	@Test
	public void testShowUser() 
	{
		User t = initAndSaveUser();
		UserReply reply = (UserReply) _presenter.process(new ShowCommand(t.id));

		chkReplySucessful(reply, Reply.VIEW_SHOW, null);
		chkDalSize(1);
		chkReplyWithEntity(reply);
	}
	@Test
	public void testShowUser_NotFound() 
	{
		User t = initAndSaveUser();
		UserReply reply = (UserReply) _presenter.process(new ShowCommand(99L));

		chkReplySucessful(reply, Reply.FORWARD_NOT_FOUND, "could not find entity");
		chkDalSize(1);
		chkReplyForwardOnly(reply);
	}


	//--------- helper fns--------------
	protected void chkDalSize(int expected)
	{
		assertEquals(expected, _dao.size());
	}
	private void chkReplyWithEntity(UserReply reply)
	{
		chkReplyWithEntity(reply, false, 0);
	}
	private void chkReplyWithEntity(UserReply reply, boolean listExists, int expected)
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
	private void chkReplyWithoutEntity(UserReply reply, boolean listExists, int expected)
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
	private void chkReplyForwardOnly(UserReply reply)
	{
		assertEquals(null, reply._entity);
		assertNull(reply._allL);
	}

	private MockUserDAO _dao;
	private UserPresenter _presenter;
	@Before
	public void init()
	{
		super.init();
		_dao = getDAO();
		this._presenter = new UserPresenter(_ctx);
	}

	private MockUserDAO getDAO()
	{
		MockUserDAO dal = (MockUserDAO) MettleInitializer.getDAO(IUserDAO.class); 
		return dal;
	}

	private User initUser()
	{
		return initUser("bob");
	}
	private User initUser(String name)
	{
		User t = new User();
		t.id = 0L; //dal will assign id
		t.name = name;
		assertEquals(0, _dao.size());
		return t;
	}

	private User initAndSaveUser()
	{
		return initAndSaveUser("bob");
	}

	private User initAndSaveUser(String name)
	{
		User t = initUser(name);
		_dao.save(t);
		assertEquals(1, _dao.size());
		return _dao.findById(t.id);
	}
	private User createUser(String name)
	{
		User u = new User();
		u.name = name;
		return u;
	}

	protected Command createWithBinder(Command cmd, User t, boolean bindingIsValid)
	{
		MockFormBinder binder = new MockFormBinder(t);
		cmd.setFormBinder(binder);
		binder.isValid = bindingIsValid;
		return cmd;
	}





}

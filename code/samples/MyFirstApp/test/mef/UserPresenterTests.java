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
import mef.daos.IUserDAO;
import mef.daos.mocks.MockTaskDAO;
import mef.daos.mocks.MockUserDAO;
import mef.entities.User;
import mef.presenters.UserPresenter;
import mef.presenters.replies.UserReply;

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
		UserReply reply = (UserReply) _presenter.process(new IndexCommand());
		
		chkReplySucessful(reply, Reply.VIEW_INDEX, null);
		chkDalSize(1);
		assertEquals("bob", _dao.all().get(0).name);
		chkReplyWithoutEntity(reply, true, 1);
	}
	
	//--- new ---
	@Test
	public void testNewUser() 
	{
		UserReply reply = (UserReply) _presenter.process( new NewCommand());
		
		chkReplySucessful(reply, Reply.VIEW_NEW, null);
		assertEquals("defaultname", reply._entity.name);
		chkDalSize(0);
		chkReplyWithEntity(reply, false, 0);
		assertNotNull(reply._options);
	}
	
	//--- create ---
	@Test
	public void testCreateUser() 
	{
		User t = initUser();
		Command cmd = createWithBinder(new CreateCommand(), t, true);
		
		UserReply reply = (UserReply) _presenter.process(cmd);
		
		chkReplySucessful(reply, Reply.FORWARD_INDEX, "created user task1");
		chkDalSize(1);
		chkReplyWithoutEntity(reply, true, 1);
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
		chkReplyWithEntity(reply, false, 0);
		assertNotNull(reply._options);
	}
	
	//--- edit ---
	@Test
	public void testEditUser() 
	{
		User t = initAndSaveUser();
		UserReply reply = (UserReply) _presenter.process(new EditCommand(t.id));
		
		chkReplySucessful(reply, Reply.VIEW_EDIT, null);
		chkDalSize(1);
		chkReplyWithEntity(reply, false, 0);
		assertNotNull(reply._options);
	}
	@Test
	public void testEditUser_NotFound() 
	{
		User t = initAndSaveUser();
		UserReply reply = (UserReply) _presenter.process(new EditCommand(99L));
		
		chkReplySucessful(reply, Reply.FORWARD_NOT_FOUND, null);
		chkDalSize(1);
		chkReplyWithoutEntity(reply, false, 0);
	}
	
	//--- update ---
	@Test
	public void testUpdateUser() 
	{
		User t = initAndSaveUser();
		t.name = "task2"; //simulate user edit
		Command cmd = createWithBinder(new UpdateCommand(t.id), t, true);
		
		UserReply reply = (UserReply) _presenter.process(cmd);
		
		chkReplySucessful(reply, Reply.FORWARD_INDEX, null);
		chkDalSize(1);
		chkReplyWithoutEntity(reply, true, 1);
		
		User t2 = _dao.findById(t.id);
		assertEquals("task2", t2.name);
	}
	@Test
	public void testUpdateUser_ValFail() 
	{
		User t = initAndSaveUser();
		t.name = "task2"; //simulate user edit
		Command cmd = createWithBinder(new UpdateCommand(t.id), t, false);
		
		UserReply reply = (UserReply) _presenter.process(cmd);
		
		chkReplySucessful(reply, Reply.VIEW_EDIT, "binding failed!");
		chkDalSize(1);
		chkReplyWithEntity(reply, false, 0);
		
		User t2 = _dao.findById(t.id);
		assertEquals("task2", t2.name); //unchanged (but mock dal kinda broken)
		assertNotNull(reply._options);
	}
	@Test
	public void testUpdateUser_NotFound() 
	{
		User t = initAndSaveUser();
		Command cmd = createWithBinder(new UpdateCommand(99L), t, true);
		UserReply reply = (UserReply) _presenter.process(cmd);
		
		chkReplySucessful(reply, Reply.FORWARD_NOT_FOUND, null);
		chkDalSize(1);
		chkReplyWithoutEntity(reply, false, 0);
	}
	
	
	//--- delete ---
	@Test
	public void testDeleteUser() 
	{
		User t = initAndSaveUser();
		UserReply reply = (UserReply) _presenter.process( new DeleteCommand(t.id));
		
		chkReplySucessful(reply, Reply.FORWARD_INDEX, null);
		chkDalSize(0);
		chkReplyWithoutEntity(reply, true, 0);
	}
	
	@Test
	public void testBadDeleteUser() 
	{
		User t = initAndSaveUser();
		UserReply reply = (UserReply) _presenter.process(new DeleteCommand(99L)); //not exist
		
		chkReplySucessful(reply, Reply.FORWARD_NOT_FOUND, "could not find task");
		chkDalSize(1);
		chkReplyWithoutEntity(reply, true, 1);
	}
	
	//--- show ---
		@Test
		public void testShowUser() 
		{
			User t = initAndSaveUser();
			UserReply reply = (UserReply) _presenter.process(new ShowCommand(t.id));
			
			chkReplySucessful(reply, Reply.VIEW_SHOW, null);
			chkDalSize(1);
			chkReplyWithEntity(reply, false, 0);
		}
//		@Test
//		public void testEditUser_NotFound() 
//		{
//			User t = initAndSaveUser();
//			UserReply reply = (UserReply) _presenter.process(new EditCommand(99L));
//			
//			chkReplySucessful(reply, Reply.FORWARD_NOT_FOUND, null);
//			chkDalSize(1);
//			chkReplyWithoutEntity(reply, false, 0);
//		}
	
	
	//--------- helper fns--------------
	protected void chkDalSize(int expected)
	{
		assertEquals(expected, _dao.size());
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
		MockUserDAO dal = (MockUserDAO) _ctx.getServiceLocator().getInstance(IUserDAO.class); 
		return dal;
	}
	
	private User initUser()
	{
		User t = new User();
		t.id = 0L; //dal will assign id
		t.name = "task1";
		assertEquals(0, _dao.size());
		return t;
	}
	
	private User initAndSaveUser()
	{
		User t = initUser();
		_dao.save(t);
		assertEquals(1, _dao.size());
		return t;
	}
	private User createUser(String name)
	{
		User u = new User();
		u.name = name;
		return u;
	}
	
	
}

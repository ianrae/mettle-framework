package mef;

import static org.junit.Assert.*;


import org.junit.Test;
import org.mef.framework.commands.CreateCommand;
import org.mef.framework.commands.DeleteCommand;
import org.mef.framework.commands.EditCommand;
import org.mef.framework.commands.IndexCommand;
import org.mef.framework.commands.NewCommand;
import org.mef.framework.commands.UpdateCommand;
import org.mef.framework.presenters.Presenter;
import org.mef.framework.replies.Reply;
import org.mef.framework.sfx.SfxContext;

import mef.core.Initializer;
import mef.dals.IUserDAL;
import mef.entities.User;
import mef.mocks.MockFormBinder;
import mef.mocks.MockTaskDAL;
import mef.mocks.MockUserDAL;
import mef.presenters.UserPresenter;
import mef.presenters.UserReply;

public class UserPresenterTests 
{

	//GET 	users/    		index          VINDEX
	//GET 	users/new 		new            VNEW
	//POST  users/new   	create form    f index
	//GET   users/:id/edit  edit           VEDIT
	//POST  users/:id/edit  update form    f index
	//POST 	users/:id/delete delete		   f index
	
	
	@Test
	public void indexTest() 
	{
		init();
		UserPresenter presenter = new UserPresenter(_ctx);
		UserReply reply = (UserReply) presenter.process(new IndexCommand());
		
		chkReplySucessful(reply, Reply.VIEW_INDEX, null);
		chkDalSize(0);
		chkReplyWithoutEntity(reply, true, 0);
	}

	
	@Test
	public void testNewUser() 
	{
		init();
		UserPresenter presenter = new UserPresenter(_ctx);
		NewCommand cmd = new NewCommand();
		
		UserReply reply = (UserReply) presenter.process(cmd);
		
		chkReplySucessful(reply, Reply.VIEW_NEW, null);
		assertEquals("defaultname", reply._entity.name);
		chkDalSize(0);
		chkReplyWithEntity(reply, false, 0);
	}
	
	@Test
	public void testCreateUser() 
	{
		init();
		User t = new User();
		t.id = 46L;
		t.name = "task1";
		assertEquals(0, _dal.size());
		
		UserPresenter presenter = new UserPresenter(_ctx);
		CreateCommand cmd = new CreateCommand();
		MockFormBinder binder = new MockFormBinder(t);
		cmd.setFormBinder(binder);
		
		UserReply reply = (UserReply) presenter.process(cmd);
		
		chkReplySucessful(reply, Reply.FORWARD_INDEX, null);
		chkDalSize(1);
		chkReplyWithoutEntity(reply, true, 1);
	}
	
	@Test
	public void testEditUser() 
	{
		init();
		User t = new User();
		t.id = 46L;
		t.name = "task1";
		_dal.save(t);
		assertEquals(1, _dal.size());
		
		UserPresenter presenter = new UserPresenter(_ctx);
		EditCommand cmd = new EditCommand(t.id);
		UserReply reply = (UserReply) presenter.process(cmd);
		
		chkReplySucessful(reply, Reply.VIEW_EDIT, null);
		chkDalSize(1);
		chkReplyWithEntity(reply, false, 0);
	}
	@Test
	public void testBadEditUser() 
	{
		init();
		User t = new User();
		t.id = 46L;
		t.name = "task1";
		_dal.save(t);
		assertEquals(1, _dal.size());
		
		UserPresenter presenter = new UserPresenter(_ctx);
		EditCommand cmd = new EditCommand(99L);
		UserReply reply = (UserReply) presenter.process(cmd);
		
		chkReplySucessful(reply, Reply.FORWARD_NOT_FOUND, null);
		chkDalSize(1);
		chkReplyWithoutEntity(reply, false, 0);
	}
	
	@Test
	public void testUpdateUser() 
	{
		init();
		User t = new User();
		t.id = 46L;
		t.name = "task1";
		assertEquals(0, _dal.size());
		
		UserPresenter presenter = new UserPresenter(_ctx);
		UpdateCommand cmd = new UpdateCommand(t.id);
		t.name = "task2";
		MockFormBinder binder = new MockFormBinder(t);
		cmd.setFormBinder(binder);
		
		UserReply reply = (UserReply) presenter.process(cmd);
		
		chkReplySucessful(reply, Reply.FORWARD_INDEX, null);
		chkDalSize(1);
		chkReplyWithoutEntity(reply, true, 1);
		
		User t2 = _dal.findById(t.id);
		assertEquals("task2", t2.name);
	}
	
	
	@Test
	public void testDeleteUser() 
	{
		init();
		User t = new User();
		t.id = 46L;
		t.name = "task1";
		_dal.save(t);
		assertEquals(1, _dal.size());
		
		UserPresenter presenter = new UserPresenter(_ctx);
		DeleteCommand cmd = new DeleteCommand(t.id);
		
		UserReply reply = (UserReply) presenter.process(cmd);
		
		chkReplySucessful(reply, Reply.FORWARD_INDEX, null);
		chkDalSize(0);
		chkReplyWithoutEntity(reply, true, 0);
	}
	
	@Test
	public void testBadDeleteUser() 
	{
		init();
		User t = new User();
		t.id = 46L;
		t.name = "task1";
		_dal.save(t);
		assertEquals(1, _dal.size());
		
		UserPresenter presenter = new UserPresenter(_ctx);
		DeleteCommand cmd = new DeleteCommand(99L); //not exist
		
		UserReply reply = (UserReply) presenter.process(cmd);
		
		chkReplySucessful(reply, Reply.FORWARD_NOT_FOUND, "could not find task");
		chkDalSize(1);
		chkReplyWithoutEntity(reply, true, 1);
	}
	
	
	//--------- helper fns--------------
	private void chkReplySucessful(Reply reply, int view, String flash)
	{
		assertNotNull(reply);
		assertEquals(false, reply.failed()); //should go to error page. something bad happened
		assertEquals(view, reply.getDestination());
		assertEquals(flash, reply.getFlash());
	}
	private void chkDalSize(int expected)
	{
		assertEquals(expected, _dal.size());
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
	
	
	
	protected SfxContext _ctx;
	private MockUserDAL _dal;
	private void init()
	{
		_ctx = Initializer.createContext(new MockTaskDAL(), new MockUserDAL());
		_dal = getDAL();
	}
	
	private MockUserDAL getDAL()
	{
		MockUserDAL dal = (MockUserDAL) _ctx.getServiceLocator().getInstance(IUserDAL.class); 
		return dal;
	}
}

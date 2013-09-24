package mef;

import static org.junit.Assert.*;


import org.junit.Test;
import org.mef.framework.commands.CreateCommand;
import org.mef.framework.commands.DeleteCommand;
import org.mef.framework.commands.IndexCommand;
import org.mef.framework.presenters.Presenter;
import org.mef.framework.replies.Reply;
import org.mef.framework.sfx.SfxContext;

import mef.core.Initializer;
import mef.dals.IUserDAL;
import mef.dals.MockTaskDAL;
import mef.dals.MockUserDAL;
import mef.entities.User;
import mef.presenters.UserPresenter;
import mef.presenters.UserReply;

public class UserPresenterTests 
{

	@Test
	public void test() 
	{
		init();
		UserPresenter presenter = new UserPresenter(_ctx);
		UserReply reply = (UserReply) presenter.process(new IndexCommand());
		
		assertNotNull(reply);
		assertEquals(0, reply._allL.size());
		assertEquals(null, reply.getFlash());
		
		assertEquals(reply.getViewName(), Reply.VIEW_DEFAULT);
	}

	
	@Test
	public void testDeleteUser() 
	{
		init();
		MockUserDAL dal = getDAL();
		User t = new User();
		t.id = 46L;
		t.name = "task1";
		dal.save(t);
		assertEquals(1, dal.size());
		
		UserPresenter presenter = new UserPresenter(_ctx);
		DeleteCommand cmd = new DeleteCommand(t.id);
		
		UserReply reply = (UserReply) presenter.process(cmd);
		
		assertNotNull(reply);
		assertEquals(false, reply.failed()); //should go to error page. something bad happened
		assertEquals(null, reply.getFlash());
		assertEquals(0, reply._allL.size());
		assertEquals(0, dal.size());
		assertEquals(reply.getViewName(), Reply.VIEW_DEFAULT);
		// resp._allL may be null
	}
	
	@Test
	public void testBadDeleteUser() 
	{
		init();
		MockUserDAL dal = getDAL();
		User t = new User();
		t.id = 46L;
		t.name = "task1";
		dal.save(t);
		assertEquals(1, dal.size());
		
		UserPresenter presenter = new UserPresenter(_ctx);
		DeleteCommand cmd = new DeleteCommand(99L); //not exist
		
		UserReply reply = (UserReply) presenter.process(cmd);
		
		assertNotNull(reply);
		assertEquals(false, reply.failed()); 
		assertEquals("somewhere", reply.getForward()); //can go back to same page, but with msg
		assertEquals("could not find task", reply.getFlash());
		assertEquals(1, reply._allL.size());
		assertEquals(1, dal.size());
		assertEquals(reply.getViewName(), Reply.VIEW_DEFAULT);
		// resp._allL may be null
	}
	
	@Test
	public void testCreateUser() 
	{
		init();
		MockUserDAL dal = getDAL();
		User t = new User();
		t.id = 46L;
		t.name = "task1";
		assertEquals(0, dal.size());
		
		UserPresenter presenter = new UserPresenter(_ctx);
		CreateCommand cmd = new CreateCommand();
		MockFormBinder binder = new MockFormBinder(t);
		cmd.setFormBinder(binder);
		
		UserReply reply = (UserReply) presenter.process(cmd);
		
		assertNotNull(reply);
		assertEquals(false, reply.failed()); //should go to error page. something bad happened
		assertEquals(null, reply.getFlash());
		assertEquals(1, reply._allL.size());
		assertEquals(1, dal.size());
		assertEquals(reply.getViewName(), Reply.VIEW_DEFAULT);
		// resp._allL may be null
	}
	
	//--------- helper fns--------------
	protected SfxContext _ctx;
	private void init()
	{
		_ctx = Initializer.createContext(new MockTaskDAL(), new MockUserDAL());
	}
	
	private MockUserDAL getDAL()
	{
		MockUserDAL dal = (MockUserDAL) _ctx.getServiceLocator().getInstance(IUserDAL.class); 
		return dal;
	}
}

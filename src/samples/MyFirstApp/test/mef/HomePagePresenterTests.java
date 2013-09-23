package mef;

import static org.junit.Assert.*;


import org.junit.Test;
import org.mef.framework.commands.DeleteCommand;
import org.mef.framework.commands.IndexCommand;
import org.mef.framework.presenters.Presenter;
import org.mef.framework.replies.Reply;
import org.mef.framework.sfx.SfxContext;

import mef.core.Initializer;
import mef.dals.ITaskDAL;
import mef.dals.MockTaskDAL;
import mef.entities.TaskEO;
import mef.presenters.HomePagePresenter;
import mef.presenters.HomePageReply;

public class HomePagePresenterTests 
{

	@Test
	public void test() 
	{
		init();
		HomePagePresenter presenter = new HomePagePresenter(_ctx);
		HomePageReply reply = (HomePageReply) presenter.process(new IndexCommand());
		
		assertNotNull(reply);
		assertEquals(0, reply._allL.size());
		assertEquals(null, reply.getFlash());
		
		assertEquals(reply.getViewName(), Reply.VIEW_DEFAULT);
	}

	@Test
	public void testDBDown() 
	{
		init();
		MockTaskDAL dal = getDAL(); 
		dal._dbDown = true;
		
		HomePagePresenter presenter = new HomePagePresenter(_ctx);
		HomePageReply reply = (HomePageReply) presenter.process(new IndexCommand());
		
		assertNotNull(reply);
		assertEquals(true, reply.failed()); //should go to error page. something bad happened
		
		// resp._allL may be null
	}
	
	@Test
	public void testDeleteTask() 
	{
		init();
		MockTaskDAL dal = getDAL();
		TaskEO t = new TaskEO();
		t.id = 46L;
		t.label = "task1";
		dal.save(t);
		assertEquals(1, dal.size());
		
		HomePagePresenter presenter = new HomePagePresenter(_ctx);
		DeleteCommand cmd = new DeleteCommand(t.id);
		
		HomePageReply reply = (HomePageReply) presenter.process(cmd);
		
		assertNotNull(reply);
		assertEquals(false, reply.failed()); //should go to error page. something bad happened
		assertEquals(null, reply.getFlash());
		assertEquals(0, reply._allL.size());
		assertEquals(0, dal.size());
		assertEquals(reply.getViewName(), Reply.VIEW_DEFAULT);
		// resp._allL may be null
	}
	
	@Test
	public void testBadDeleteTask() 
	{
		init();
		MockTaskDAL dal = getDAL();
		TaskEO t = new TaskEO();
		t.id = 46L;
		t.label = "task1";
		dal.save(t);
		assertEquals(1, dal.size());
		
		HomePagePresenter presenter = new HomePagePresenter(_ctx);
		DeleteCommand cmd = new DeleteCommand(99L); //not exist
		
		HomePageReply reply = (HomePageReply) presenter.process(cmd);
		
		assertNotNull(reply);
		assertEquals(false, reply.failed()); 
		assertEquals("somewhere", reply.getForward()); //can go back to same page, but with msg
		assertEquals("could not find task", reply.getFlash());
		assertEquals(1, reply._allL.size());
		assertEquals(1, dal.size());
		assertEquals(reply.getViewName(), Reply.VIEW_DEFAULT);
		// resp._allL may be null
	}
	
	//--------- helper fns--------------
	protected SfxContext _ctx;
	private void init()
	{
		_ctx = Initializer.createContext(new MockTaskDAL());
	}
	
	private MockTaskDAL getDAL()
	{
		MockTaskDAL dal = (MockTaskDAL) _ctx.getServiceLocator().getInstance(ITaskDAL.class); 
		return dal;
	}
}

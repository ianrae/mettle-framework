package mef.presenter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import mef.BaseMettleTest;

import org.mef.framework.commands.Command;
import org.mef.framework.replies.Reply;

public class BasePresenterTest extends BaseMettleTest
{
	protected void chkReplySucessful(Reply reply, int view, String flash)
	{
		assertNotNull(reply);
		assertEquals(false, reply.failed()); //should go to error page. something bad happened
		assertEquals(view, reply.getDestination());
		assertEquals(flash, reply.getFlash());
	}
	protected void chkUserName(Reply reply, String expected) 
	{
		assertEquals(expected, reply.authUser.getUsername());
	}


	
}

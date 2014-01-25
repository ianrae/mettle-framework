package mef;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.mef.framework.replies.Reply;

public class BasePresenterTest extends BaseTest
{
	protected void chkReplySucessful(Reply reply, int view, String flash)
	{
		assertNotNull(reply);
		assertEquals(false, reply.failed()); //should go to error page. something bad happened
		assertEquals(view, reply.getDestination());
		assertEquals(flash, reply.getFlash());
	}

	
}

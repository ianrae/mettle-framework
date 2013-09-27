package mef;

import static org.junit.Assert.*;

import mef.core.Initializer;
import mef.entities.User;
import mef.mocks.MockFormBinder;
import mef.mocks.MockTaskDAL;
import mef.mocks.MockUserDAL;
import mef.presenters.UserPresenter;

import org.junit.Before;
import org.junit.Test;
import org.mef.framework.commands.Command;
import org.mef.framework.replies.Reply;
import org.mef.framework.sfx.SfxContext;

public class BasePresenterTest 
{
	protected SfxContext _ctx;
	
	public void init()
	{
		_ctx = Initializer.createContext(new MockTaskDAL(), new MockUserDAL());
	}

	protected void chkReplySucessful(Reply reply, int view, String flash)
	{
		assertNotNull(reply);
		assertEquals(false, reply.failed()); //should go to error page. something bad happened
		assertEquals(view, reply.getDestination());
		assertEquals(flash, reply.getFlash());
	}

	protected Command createWithBinder(Command cmd, User t, boolean bindingIsValid)
	{
		MockFormBinder binder = new MockFormBinder(t);
		cmd.setFormBinder(binder);
		binder.isValid = bindingIsValid;
		return cmd;
	}
	
}

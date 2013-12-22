package clog;

import static org.junit.Assert.assertEquals;
import org.mef.framework.sfx.SfxErrorTracker;
import tools.BaseTest;

public class BaseJsonTest extends BaseTest
{

	protected String fix(String s)
	{
		s = s.replace('\'', '"');
		return s;
	}
	
	protected void init()
	{
		this.createContext();
		SfxErrorTracker tracker = new SfxErrorTracker(_ctx);
		_ctx.getServiceLocator().registerSingleton(SfxErrorTracker.class, tracker);
	}

	protected void chkErrors(int i) 
	{
		SfxErrorTracker tracker = (SfxErrorTracker) _ctx.getServiceLocator().getInstance(SfxErrorTracker.class);
		assertEquals(i, tracker.getErrorCount());
	}

	
}

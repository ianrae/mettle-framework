package mef;

import mef.core.MettleInitializer;


public class BaseMettleTest extends BaseTest
{
	@Override
	public void init()
	{
		MettleInitializer initializer = new MettleInitializer();
		initializer.appPath = this.getCurrentDir("");
		initPart1(initializer);
		initPart2(initializer);
	}
	public void initPart1(MettleInitializer initializer)
	{
		_ctx = initializer.createContext();
		_ctx.setVar("UNITTEST", "true");
	}
	public void initPart2(MettleInitializer initializer)
	{
		//now register any mocks...
		
		initializer.onStart();
	}

}

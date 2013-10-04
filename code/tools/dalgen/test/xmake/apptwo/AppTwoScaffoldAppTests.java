package xmake.apptwo;

import org.junit.Test;
import org.mef.dalgen.codegen.AppScaffoldCodeGenerator;
import unittests.BaseTest;

//********************** CAREFUL!!! ****************************

public class AppTwoScaffoldAppTests extends BaseTest
{
	@Test
	public void testEntity() throws Exception
	{
		createContext();
		AppScaffoldCodeGenerator gen = new AppScaffoldCodeGenerator(_ctx);
		
		gen.disableFileIO = true;  //***** WATCH OUT!11 ****
		
		String appDir = this.getCurrentDir("..\\..\\samples\\AppTwo");
		String stDir = this.getCurrentDir("src\\org\\mef\\dalgen\\resources\\app");
log(appDir);
log(stDir);
		
		gen.init(appDir, stDir);

		boolean b = false;
		b = gen.generate();
		if (b)
		{}
	}

}

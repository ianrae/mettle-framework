package xmake.apptwo;



import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mef.dalgen.codegen.PresenterScaffoldCodeGenerator;

import unittests.BaseTest;

//********************** CAREFUL!!! ****************************

public class AppTwoScaffoldPresenterTests extends BaseTest
{
	@Test
	public void testEntity() throws Exception
	{
		createContext();
		PresenterScaffoldCodeGenerator gen = new PresenterScaffoldCodeGenerator(_ctx);
		
		gen.disableFileIO = false;  //***** WATCH OUT!11 ****
		
		String appDir = this.getCurrentDir("..\\..\\internal-samples\\AppTwo");
		String stDir = this.getCurrentDir("src\\org\\mef\\dalgen\\resources\\presenter");
log(appDir);
log(stDir);
		
		int n = gen.init(appDir, stDir);
		assertEquals(3, n);

		boolean b = false;
//		boolean b = gen.generate(0);
		b = gen.generate("User");
		b = gen.generate("Company");
		b = gen.generate("Computer");
		if (b)
		{}
	}

}

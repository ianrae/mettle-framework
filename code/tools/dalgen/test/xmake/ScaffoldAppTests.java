package xmake;



import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mef.dalgen.codegen.AppScaffoldCodeGenerator;
import org.mef.dalgen.codegen.PresenterScaffoldCodeGenerator;

import unittests.BaseTest;

//********************** CAREFUL!!! ****************************

public class ScaffoldAppTests extends BaseTest
{
	@Test
	public void testEntity() throws Exception
	{
		log("--testEntity--");
		createContext();
		AppScaffoldCodeGenerator gen = new AppScaffoldCodeGenerator(_ctx);
		
		gen.disableFileIO = false;  //***** WATCH OUT!11 ****
		
		String appDir = this.getCurrentDir("..\\..\\samples\\AppTwo");
		String stDir = this.getCurrentDir("src\\org\\mef\\dalgen\\resources\\app");
log(appDir);
log(stDir);
		
		gen.init(appDir, stDir);

		boolean b = false;
		b = gen.generate();
//		boolean b = gen.generate(0);
//		b = gen.generate("User");
//		b = gen.generate("Phone");
//		b = gen.generate("Zoo");
		if (b)
		{}
	}

}

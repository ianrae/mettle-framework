package xmake;



import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mef.dalgen.codegen.CreateCodeGenerator;

import unittests.BaseTest;

//********************** CAREFUL!!! ****************************

public class SampleAppCreateTests extends BaseTest
{
	@Test
	public void testEntity() throws Exception
	{
		log("--testEntity--");
		createContext();
		CreateCodeGenerator gen = new CreateCodeGenerator(_ctx);
		gen.disableFileIO = true;
		
		String appDir = this.getCurrentDir("..\\..\\samples\\MyFirstApp");
		String stDir = this.getCurrentDir("src\\org\\mef\\dalgen\\resources");
log(appDir);
log(stDir);
		
		int n = gen.init(appDir, stDir);
		assertEquals(4, n);

		boolean b = false;
//		boolean b = gen.generate(0);
		b = gen.generate("User");
//		b = gen.generate("Phone");
//		b = gen.generate("Zoo");
		if (b)
		{}
	}

}

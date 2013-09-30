package xmake;



import static org.junit.Assert.*;

import org.junit.Test;
import org.mef.dalgen.codegen.DalCodeGenerator;

import unittests.BaseTest;

//********************** DAATA!!! ****************************

public class SampleAppCodeGenTests extends BaseTest
{
	@Test
	public void testEntity() throws Exception
	{
		boolean genFiles = true;
		if (! genFiles)
		{
			return;
		}
		
		log("--testEntity--");
		createContext();
		DalCodeGenerator gen = new DalCodeGenerator(_ctx);
		String appDir = this.getCurrentDir("..\\..\\samples\\MyFirstApp");
		String stDir = this.getCurrentDir("src\\org\\mef\\dalgen\\resources");
log(appDir);
log(stDir);
		
		int n = gen.init(appDir, stDir);
		assertEquals(4, n);

		boolean b = false;
//		boolean b = gen.generate(0);
		b = gen.generate("User");
		b = gen.generate("Phone");
//		b = gen.generate("Zoo");
		if (b)
		{}
	}

}

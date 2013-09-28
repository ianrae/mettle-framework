package xmake;



import static org.junit.Assert.*;

import org.junit.Test;
import org.mef.dalgen.codegen.DalCodeGenerator;

import unittests.BaseTest;

//********************** CAREFUL!!! ****************************

public class SampleAppCodeGenTests extends BaseTest
{
	@Test
	public void testEntity() throws Exception
	{
		boolean genFiles = false;
		if (! genFiles)
		{
			return;
		}
		
		log("--testEntity--");
		createContext();
		DalCodeGenerator gen = new DalCodeGenerator(_ctx);
		String appDir = this.getCurrentDir("src\\samples\\MyFirstApp");
		String stDir = this.getUnitTestDir("testfiles");
		
		int n = gen.init(appDir, stDir);
		assertEquals(2, n);
		
//		boolean b = gen.generate(0);
		boolean b = gen.generate(1);
		
	}

}

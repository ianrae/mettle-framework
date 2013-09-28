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
		boolean genFiles = true;
		if (! genFiles)
		{
			return;
		}
		
		log("--testEntity--");
		createContext();
		DalCodeGenerator gen = new DalCodeGenerator(_ctx);
		String appDir = this.getCurrentDir("..\\..\\samples\\MyFirstApp");
		String stDir = this.getUnitTestDir("testfiles");
log(appDir);
log(stDir);
		
		int n = gen.init(appDir, stDir);
		assertEquals(3, n);

		boolean b = false;
//		boolean b = gen.generate(0);
		b = gen.generate(1); //User
		b = gen.generate(2); //Phone
		
		if (b)
		{}
	}

}

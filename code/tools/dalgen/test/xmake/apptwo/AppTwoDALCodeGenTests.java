package xmake.apptwo;



import static org.junit.Assert.*;

import org.junit.Test;
import org.mef.dalgen.codegen.DalCodeGenerator;

import unittests.BaseTest;

//********************** DAATA!!! ****************************

public class AppTwoDALCodeGenTests extends BaseTest
{
	@Test
	public void testEntity() throws Exception
	{
		boolean genFiles = true;
		if (! genFiles)
		{
			return;
		}
		
		createContext();
		DalCodeGenerator gen = new DalCodeGenerator(_ctx);
		String appDir = this.getCurrentDir("..\\..\\samples\\AppTwo");
		String stDir = this.getCurrentDir("src\\org\\mef\\dalgen\\resources\\dal\\");
log(appDir);
log(stDir);

		gen.genRealDAO = true;
		
		int n = gen.init(appDir, stDir);
		assertEquals(2, n);

		boolean b = false;
		b = gen.generateOnce(); //allKnownDAOs
		b = gen.generate("User");
//		b = gen.generate("Company");
		if (b)
		{}
	}

}

package xmake;



import static org.junit.Assert.*;

import org.junit.Test;
import org.mef.dalgen.codegen.DalCodeGenerator;

import unittests.BaseTest;

//********************** DAATA!!! ****************************

public class DALCodeGenTests extends BaseTest
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
		String stDir = this.getCurrentDir("src\\org\\mef\\dalgen\\resources\\dal\\");
log(appDir);
log(stDir);
		
		int n = gen.init(appDir, stDir);
		assertEquals(4, n);

		boolean b = false;
		b = gen.generateOnce(); //allKnownDAOs
		
		gen.genRealDAO = true;
		b = gen.generate("Task");
		b = gen.generate("Phone");
		gen.genRealDAO = false; //until get has-a assoc working
		b = gen.generate("User");
	}

}

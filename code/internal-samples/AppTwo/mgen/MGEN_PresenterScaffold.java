


import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mef.tools.mgen.codegen.PresenterScaffoldCodeGenerator;


//********************** CAREFUL!!! ****************************

public class MGEN_PresenterScaffold extends BaseTest
{
	@Test
	public void testEntity() throws Exception
	{
		createContext();
		PresenterScaffoldCodeGenerator gen = new PresenterScaffoldCodeGenerator(_ctx);
		gen.disableFileIO = false;  //***** WATCH OUT!11 ****
		
		String appDir = this.getCurrentDir("");
		log(appDir);
		
		int n = gen.init(appDir);
		assertEquals(4, n);

		boolean b = gen.generateAll();
		
//		b = gen.generate("User");
//		b = gen.generate("Company");
//		b = gen.generate("Computer");
//		b = gen.generate("Role");
		assertTrue(b);
	}

}

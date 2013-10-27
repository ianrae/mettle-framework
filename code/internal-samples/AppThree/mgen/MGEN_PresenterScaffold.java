


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
		String appDir = this.getCurrentDir("");
		log(appDir);
		
		int n = gen.init(appDir);
		assertEquals(1, n);

		boolean b = gen.generateAll();
		assertTrue(b);
	}

}

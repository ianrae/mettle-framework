

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;

import org.junit.Test;
import org.mef.tools.mgen.codegen.AppScaffoldCodeGenerator;

//********************** CAREFUL!!! ****************************

public class MGEN_AppScaffold extends BaseTest
{
	@Test
	public void testEntity() throws Exception
	{
		createContext();
		AppScaffoldCodeGenerator gen = new AppScaffoldCodeGenerator(_ctx);
		
		gen.disableFileIO = false;  //***** WATCH OUT!11 ****
		
		String appDir = this.getCurrentDir("");
		log(appDir);
		
		gen.init(appDir);

		boolean b = false;
		b = gen.generateAll();
		assertTrue(b);
	}

}

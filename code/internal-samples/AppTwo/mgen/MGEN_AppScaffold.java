

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
		String stDir = this.getCurrentDir("..\\..\\mettle\\conf\\mgen\\resources\\app");
log(appDir);
log(stDir);
		
		InputStream stream = gen.getClass().getResourceAsStream("/mgen/resources/dal/entity.stg");
		assertNotNull(stream);

		gen.init(appDir, stDir);

		boolean b = false;
		b = gen.generate();
		if (b)
		{}
	}

}

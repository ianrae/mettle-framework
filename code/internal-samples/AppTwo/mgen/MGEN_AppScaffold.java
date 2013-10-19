

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
		
		gen.disableFileIO = true;  //***** WATCH OUT!11 ****
		
		String appDir = this.getCurrentDir("");
		String stDir = this.getCurrentDir("..\\..\\mettle\\app\\org\\mef\\tools\\mgen\\resources\\app");
log(appDir);
log(stDir);
		
		gen.init(appDir, stDir);

		boolean b = false;
		b = gen.generate();
		if (b)
		{}
	}

}

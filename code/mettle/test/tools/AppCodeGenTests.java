package tools;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.Test;
import org.mef.tools.mgen.codegen.AppScaffoldCodeGenerator;

public class AppCodeGenTests extends BaseTest
{

	@Test
	public void test() throws Exception
	{
		this.createContext();
		AppScaffoldCodeGenerator gen = new AppScaffoldCodeGenerator(_ctx);
		
		gen.disableFileIO = false;  //***** WATCH OUT!11 ****
		
		String appDir = "c:\\tmp\\cc";
		String stDir = this.getCurrentDir("..\\..\\mettle\\conf\\mgen\\resources\\app");
log(appDir);
log(stDir);
		
//		InputStream stream = gen.getClass().getResourceAsStream("/mgen/resources/dal/entity.stg");
//		assertNotNull(stream);

		gen.init(appDir, stDir);

		boolean b = false;
		b = gen.generate();
	}

}

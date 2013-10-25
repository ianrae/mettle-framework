//import static org.junit.Assert.*;
//
//import org.junit.Test;
//import org.mef.tools.mgen.codegen.AppScaffoldCodeGenerator;
//
//
//public class AppCodeGenTest extends BaseTest
//{
//	@Test
//	public void testEntity() throws Exception
//	{
//		createContext();
//		AppScaffoldCodeGenerator gen = new AppScaffoldCodeGenerator(_ctx);
//		
//		gen.disableFileIO = false;  //***** WATCH OUT!11 ****
//		
//		String appDir = this.getCurrentDir("");
//		log(appDir);
//		
//		gen.init(appDir);
//
//		boolean b = false;
//		b = gen.generateAll();
//		assertTrue(b);
//	}
//}

package tools;

import static org.junit.Assert.*;

import java.io.File;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.mef.tools.mgen.codegen.AppScaffoldCodeGenerator;
import org.mef.tools.mgen.codegen.PresenterScaffoldCodeGenerator;

public class AppCodeGenTests extends BaseTest
{

	@Test
	public void test() throws Exception
	{
		this.createContext();
		AppScaffoldCodeGenerator gen = new AppScaffoldCodeGenerator(_ctx);
		
		gen.disableFileIO = false;  //***** WATCH OUT!11 ****
		
		String appDir = "c:\\tmp\\cc";
		log(appDir);
		gen.init(appDir);

		boolean b = false;
		b = gen.generate();
		assertTrue(b);
	}
	
	@Test
	public void testEntity() throws Exception
	{
		createContext();
		PresenterScaffoldCodeGenerator gen = new PresenterScaffoldCodeGenerator(_ctx);
		
		gen.disableFileIO = false;  //***** WATCH OUT!11 ****
		
		String appDir = "c:\\tmp\\cc";
		log(appDir);
		
		String path = this.getTestFile("mef.xml");
		FileUtils.copyFileToDirectory(new File(path), new File(appDir));
		
		int n = gen.init(appDir);
		assertEquals(2, n);

		boolean b = false;
//		boolean b = gen.generate(0);
		b = gen.generate("Company");
//		b = gen.generate("Computer");
//		b = gen.generate("Role");
//		if (b)
//		{}
	}

}

package tools;

import static org.junit.Assert.*;

import java.io.File;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.mef.tools.mgen.codegen.AppScaffoldCodeGenerator;
import org.mef.tools.mgen.codegen.DalCodeGenerator;
import org.mef.tools.mgen.codegen.PresenterScaffoldCodeGenerator;

public class AppCodeGenTests extends BaseTest
{

	@Test
	public void test() throws Exception
	{
		AppScaffoldCodeGenerator gen = new AppScaffoldCodeGenerator();
		
		gen.disableFileIO = false;  //***** WATCH OUT!11 ****
		String appDir = "c:\\tmp\\cc";
		log(appDir);
		gen.init(appDir);
		gen.createDir("mgen");
		gen.createDir("app\\controllers");
		gen.createDir("app\\views");

		boolean b = false;
		b = gen.generateAll();
		assertTrue(b);
	}
	
	@Test
	public void testPresenter() throws Exception
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
		b = gen.generate("Computer");
//		b = gen.generate("Role");
//		if (b)
//		{}
	}
	
	
	@Test
	public void testDAL() throws Exception
	{
		this.createContext();
		DalCodeGenerator gen = new DalCodeGenerator(_ctx);
		String appDir = "c:\\tmp\\cc";
		log(appDir);
		
		String path = this.getTestFile("mef.xml");
		FileUtils.copyFileToDirectory(new File(path), new File(appDir));
		
		int n = gen.init(appDir);
		assertEquals(2, n);

		//need some more dirs
		gen.createDir("app\\models");
		
		gen.genRealDAO = true;
		

		boolean b = false;
		gen.genDaoLoader = true;
		b = gen.generateOnce(); //allKnownDAOs
//		b = gen.generate("User");
		b = gen.generate("Company");
		b = gen.generate("Computer");
//		b = gen.generate("Role");
		if (b)
		{}
	}

}

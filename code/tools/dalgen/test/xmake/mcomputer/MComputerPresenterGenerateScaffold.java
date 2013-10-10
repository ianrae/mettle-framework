package xmake.mcomputer;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mef.dalgen.codegen.PresenterScaffoldCodeGenerator;

import unittests.BaseTest;

public class MComputerPresenterGenerateScaffold extends BaseTest
	{
		@Test
		public void testEntity() throws Exception
		{
			createContext();
			PresenterScaffoldCodeGenerator gen = new PresenterScaffoldCodeGenerator(_ctx);
			
			gen.disableFileIO = false;  //***** WATCH OUT!11 ****
			
			String appDir = "C:\\Users\\ian\\Documents\\GitHub\\mcomputer";
			String stDir = this.getCurrentDir("src\\org\\mef\\dalgen\\resources\\presenter");
	log(appDir);
	log(stDir);
			
			int n = gen.init(appDir, stDir);
			assertEquals(1, n);

			boolean b = false;
			b = gen.generate("Company");
			if (b)
			{}
		}

	}

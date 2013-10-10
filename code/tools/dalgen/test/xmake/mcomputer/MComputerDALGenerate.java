package xmake.mcomputer;



import static org.junit.Assert.*;

import org.junit.Test;
import org.mef.dalgen.codegen.DalCodeGenerator;

import sfx.SfxErrorTracker;
import unittests.BaseTest;

//********************** DAATA!!! ****************************

public class MComputerDALGenerate extends BaseTest
{
	@Test
	public void testEntity() throws Exception
	{
		boolean genFiles = true;
		if (! genFiles)
		{
			return;
		}
		
		init();
		DalCodeGenerator gen = new DalCodeGenerator(_ctx);
		String appDir = "C:\\Users\\ian\\Documents\\GitHub\\mcomputer";
		String stDir = this.getCurrentDir("src\\org\\mef\\dalgen\\resources\\dal\\");
log(appDir);
log(stDir);

		gen.genRealDAO = true;
		
		int n = gen.init(appDir, stDir);
		assertEquals(2, n);

		boolean b = false;
		gen.genDaoLoader = true;
		b = gen.generateOnce(); //allKnownDAOs
		b = gen.generate("Company");
		b = gen.generate("Computer");
		if (b)
		{}
	}
	
	private void init()
	{
		this.createContext();
		_ctx.getServiceLocator().registerSingleton(SfxErrorTracker.class, new SfxErrorTracker(_ctx));
	}

}

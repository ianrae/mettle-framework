


import static org.junit.Assert.*;

import org.junit.Test;
import org.mef.framework.sfx.SfxErrorTracker;
import org.mef.tools.mgen.codegen.DalCodeGenerator;


//********************** DAATA!!! ****************************

public class MGEN_Daos extends BaseTest
{
	@Test
	public void testEntity() throws Exception
	{
		init();
		String appDir = this.getCurrentDir("");
		log(appDir);

		DalCodeGenerator gen = new DalCodeGenerator(_ctx);
		gen.genRealDAO = true;
		
		int n = gen.init(appDir);
//		assertEquals(1, n);

		boolean b = false;
		gen.genDaoLoader = true;
		b =gen.generateAll();
		assertTrue(b);
	}
	
	private void init()
	{
		this.createContext();
		_ctx.getServiceLocator().registerSingleton(SfxErrorTracker.class, new SfxErrorTracker(_ctx));
	}

}

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;
import org.mef.framework.sfx.SfxContext;
import org.mef.tools.mgen.codegen.AppScaffoldCodeGenerator;


public class PresenterCodeGen 
{

	@Test
	public void testEntity() throws Exception
	{
		SfxContext ctx = new SfxContext();
		AppScaffoldCodeGenerator gen = new AppScaffoldCodeGenerator(ctx);
		
		String appDir = new File(".").getAbsolutePath();
		gen.init(appDir);

		boolean b = false;
		b = gen.generateAll();
		assertTrue(b);
	}


}

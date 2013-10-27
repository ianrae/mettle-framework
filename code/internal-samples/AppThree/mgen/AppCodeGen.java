import static org.junit.Assert.*;

import org.junit.Test;
import org.mef.tools.mgen.codegen.AppScaffoldCodeGenerator;


public class AppCodeGen {

	@Test
	public void codeGen() throws Exception
	{
		AppScaffoldCodeGenerator gen = new AppScaffoldCodeGenerator();
		boolean b = gen.generateAll();
		assertTrue(b);
	}

}

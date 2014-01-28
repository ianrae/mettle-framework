package tools;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mef.tools.mgen.parser.DalGenXmlParser;
import org.mef.tools.mgen.parser.EntityDef;

public class BaseCodeGenTest extends BaseTest
{
	// ----------- helper fns ---------------
	protected EntityDef readEntityDef(String mefFile) throws Exception
	{
		String path = this.getTestFile(mefFile);
		DalGenXmlParser parser = new DalGenXmlParser(_ctx);
		boolean b = parser.parse(path);

		assertEquals(2, parser._entityL.size());
		return parser._entityL.get(0);
	}


	protected EntityDef def;
	protected String mefFilename = "dalgen.xml";
	@Before
	public void init() throws Exception
	{
		createContext();
		def = readEntityDef(mefFilename);
	}
}

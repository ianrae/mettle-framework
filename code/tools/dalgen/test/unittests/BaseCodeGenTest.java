package unittests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mef.dalgen.parser.DalGenXmlParser;
import org.mef.dalgen.parser.EntityDef;

public class BaseCodeGenTest extends BaseTest
{
	// ----------- helper fns ---------------
	protected EntityDef readEntityDef() throws Exception
	{
		String path = this.getTestFile("dalgen.xml");
		DalGenXmlParser parser = new DalGenXmlParser(_ctx);
		boolean b = parser.parse(path);

		assertEquals(2, parser._entityL.size());
		return parser._entityL.get(0);
	}


	protected EntityDef def;
	@Before
	public void init() throws Exception
	{
		createContext();
		def = readEntityDef();
	}
}

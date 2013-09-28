package unittests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mef.dalgen.codegen.EntityCodeGen;
import org.mef.dalgen.codegen.PresenterCodeGen;
import org.mef.dalgen.parser.DalGenXmlParser;
import org.mef.dalgen.parser.EntityDef;

public class CreateCodeGenTests extends BaseTest
{

	@Test
	public void testEntity() throws Exception
	{
		log("--testEntity--");
		createContext();
		EntityDef def = readEntityDef();
		
		for(EntityDef tmp : def.allEntityTypes)
		{
			log(tmp.name);
		}
		
		String path = this.getTemplateFile("presenter.stg");
		String packageName = "mef.presenters";
		PresenterCodeGen gen = new PresenterCodeGen(_ctx, path, packageName);
		String code = gen.generate(def);	
		log(code);
		assertEquals(true, 10 < code.length());
		
	}
	
	// ----------- helper fns ---------------
	private EntityDef readEntityDef() throws Exception
	{
		String path = this.getTestFile("dalgen.xml");
		DalGenXmlParser parser = new DalGenXmlParser(_ctx);
		boolean b = parser.parse(path);

		assertEquals(2, parser._entityL.size());
		return parser._entityL.get(0);
	}


}

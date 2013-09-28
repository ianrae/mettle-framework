package unittests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mef.dalgen.codegen.generators.EntityCodeGen;
import org.mef.dalgen.codegen.generators.PresenterCodeGen;
import org.mef.dalgen.codegen.generators.ReplyCodeGen;
import org.mef.dalgen.parser.DalGenXmlParser;
import org.mef.dalgen.parser.EntityDef;

public class CreateCodeGenTests extends BaseTest
{

	@Test
	public void testPresenter() throws Exception
	{
		log("--testPresenter--");
		createContext();
		EntityDef def = readEntityDef();
		
		String path = this.getTemplateFile("presenter.stg");
		String packageName = "mef.presenters";
		PresenterCodeGen gen = new PresenterCodeGen(_ctx, path, packageName);
		String code = gen.generate(def);	
		log(code);
		assertEquals(true, 10 < code.length());
		
	}
	
	@Test
	public void testReply() throws Exception
	{
		log("--testReply--");
		createContext();
		EntityDef def = readEntityDef();
		
		String path = this.getTemplateFile("reply.stg");
		String packageName = "mef.presenters.replies";
		ReplyCodeGen gen = new ReplyCodeGen(_ctx, path, packageName);
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

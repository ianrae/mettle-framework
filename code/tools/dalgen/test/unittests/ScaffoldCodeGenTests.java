package unittests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mef.dalgen.codegen.generators.EntityCodeGen;
import org.mef.dalgen.codegen.generators.PresenterCodeGen;
import org.mef.dalgen.codegen.generators.ReplyCodeGen;
import org.mef.dalgen.parser.DalGenXmlParser;
import org.mef.dalgen.parser.EntityDef;

public class ScaffoldCodeGenTests extends BaseCodeGenTest
{

	@Test
	public void testPresenter() throws Exception
	{
		log("--testPresenter--");
		String path = this.getPresenterTemplateFile("presenter.stg");
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
		String path = this.getPresenterTemplateFile("reply.stg");
		String packageName = "mef.presenters.replies";
		ReplyCodeGen gen = new ReplyCodeGen(_ctx, path, packageName);
		String code = gen.generate(def);	
		log(code);
		assertEquals(true, 10 < code.length());
		
	}
	
}

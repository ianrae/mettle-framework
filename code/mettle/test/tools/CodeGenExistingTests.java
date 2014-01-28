package tools;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mef.framework.sfx.SfxContext;
import org.mef.tools.mgen.codegen.generators.BoundaryCodeGen;
import org.mef.tools.mgen.codegen.generators.CodeGenBase;
import org.mef.tools.mgen.codegen.generators.ControllerCodeGen;
import org.mef.tools.mgen.codegen.generators.DAOIntefaceCodeGen;
import org.mef.tools.mgen.codegen.generators.DaoEntityLoaderCodeGen;
import org.mef.tools.mgen.codegen.generators.DaoFinderCodeGen;
import org.mef.tools.mgen.codegen.generators.EntityCodeGen;
import org.mef.tools.mgen.codegen.generators.EntityLoaderSaverCodeGen;
import org.mef.tools.mgen.codegen.generators.FormBinderCodeGen;
import org.mef.tools.mgen.codegen.generators.KnownDAOsCodeGen;
import org.mef.tools.mgen.codegen.generators.MockDAOCodeGen;
import org.mef.tools.mgen.codegen.generators.ModelCodeGen;
import org.mef.tools.mgen.codegen.generators.PresenterCodeGen;
import org.mef.tools.mgen.codegen.generators.PresenterUnitTestCodeGen;
import org.mef.tools.mgen.codegen.generators.RealDAOCodeGen;
import org.mef.tools.mgen.codegen.generators.ReplyCodeGen;
import org.mef.tools.mgen.codegen.generators.ViewCodeGen;
import org.mef.tools.mgen.parser.DalGenXmlParser;
import org.mef.tools.mgen.parser.EntityDef;



public class CodeGenExistingTests extends BaseCodeGenTest
{
	
	@Test
	public void testEntity() throws Exception
	{
		log("--testEntity--");
		def = readEntityDef("dalgenExisting.xml");
		assertEquals("com.abc", def.useExistingPackage);
		assertEquals(false, def.shouldGenerate(EntityDef.ENTITY));
		
		for(EntityDef tmp : def.allEntityTypes)
		{
			log(tmp.name);
		}
		
		EntityCodeGen gen = new EntityCodeGen(_ctx);
		genAndLog(gen, "entity.stg", "org.mef.dalgen.unittests.gen");
	}
	
	
	//--- helper fns ---
	private void genAndLog(CodeGenBase gen, String templateFile, String packageName)
	{
		String path = this.getTemplateFile(templateFile);
		gen.init(path, packageName);
		String code = gen.generate(def);	
		log(code);
		assertEquals(true, 10 < code.length());
	}
	private void genPresenterAndLog(CodeGenBase gen, String templateFile, String packageName)
	{
		String path = this.getPresenterTemplateFile(templateFile);
		gen.init(path, packageName);
		String code = gen.generate(def);	
		log(code);
		assertEquals(true, 10 < code.length());
		
	}
	
}

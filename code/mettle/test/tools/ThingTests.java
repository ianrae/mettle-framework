package tools;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mef.tools.mgen.codegen.generators.CodeGenBase;
import org.mef.tools.mgen.codegen.generators.ThingCodeGen;
import org.mef.tools.mgen.parser.DalGenXmlParser;
import org.mef.tools.mgen.parser.EntityDef;



public class ThingTests extends BaseCodeGenTest
{
	public ThingTests()
	{
		mefFilename = "mefThing.xml";
	}

	@Override
	protected EntityDef readEntityDef(String mefFile) throws Exception
	{
		String path = this.getTestFile(mefFile);
		DalGenXmlParser parser = new DalGenXmlParser(_ctx, true);
		boolean b = parser.parse(path);

		assertEquals(1, parser._entityL.size());
		return parser._entityL.get(0);
	}
	
	@Test
	public void testEntityParent() throws Exception
	{
		log("--testEntityParent--");
		for(EntityDef tmp : def.allEntityTypes)
		{
			log(tmp.name);
		}
		assertEquals("Note", def.allEntityTypes.get(0).name);
		
		ThingCodeGen gen = new ThingCodeGen(_ctx);
		gen.setExtended(true);
		gen.setIsParentOfExtended(true);
		genAndLog(gen, "thing.stg", "mef.things");
	}
	
	@Test
	public void testEntity() throws Exception
	{
		log("--testEntity--");
		for(EntityDef tmp : def.allEntityTypes)
		{
			log(tmp.name);
		}
		
		ThingCodeGen gen = new ThingCodeGen(_ctx);
		gen.setExtended(true);
		gen.setIsParentOfExtended(false);
		genAndLog(gen, "thing.stg", "mef.gen");
	}
	
	//--- helper fns ---
	private void genAndLog(CodeGenBase gen, String templateFile, String packageName)
	{
		String path = this.getThingTemplateFile(templateFile);
		gen.init(path, packageName);
		String code = gen.generate(def);	
		log(code);
		assertEquals(true, 10 < code.length());
	}
	
}

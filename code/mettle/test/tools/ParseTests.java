package tools;



import static org.junit.Assert.*;


import org.junit.Test;
import org.mef.framework.sfx.SfxContext;
import org.mef.tools.mgen.parser.DalGenXmlParser;
import org.mef.tools.mgen.parser.EntityDef;
import org.mef.tools.mgen.parser.FieldDef;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;
import org.stringtemplate.v4.compiler.STParser.namedArg_return;


public class ParseTests extends BaseTest
{
	@Test
	public void test() throws Exception
	{
		log("--test--");
		createContext();
		String path = this.getTestFile("dalgen.xml");
		DalGenXmlParser parser = new DalGenXmlParser(_ctx);
		boolean b = parser.parse(path);
		
		assertEquals(2, parser._entityL.size());
		
		EntityDef def = parser._entityL.get(0);
		assertEquals("Task", def.name);
		
		assertEquals(4, def.fieldL.size());
		
		FieldDef fdef = def.fieldL.get(0);
		assertEquals("id", fdef.name);
		assertEquals("long", fdef.typeName);
		assertEquals(0, fdef.annotationL.size());
		
		fdef = def.fieldL.get(1);
		assertEquals("label", fdef.name);
		assertEquals("String", fdef.typeName);
		assertEquals(1, fdef.annotationL.size());
		assertEquals(0, parser.getErrorCount());

		fdef = def.fieldL.get(3);
		assertEquals("note", fdef.name);
		assertEquals("Note", fdef.typeName);
		assertEquals(0, fdef.annotationL.size());
		
		assertEquals(1, def.queryL.size());
		assertEquals("find_by_label", def.queryL.get(0));
		assertEquals(true, def.shouldExtend(EntityDef.ENTITY)); 
		assertEquals(true, def.shouldExtend(EntityDef.DAO)); 
		assertEquals(false, def.shouldExtend(EntityDef.PRESENTER)); 
		assertEquals(false, def.shouldExtend(EntityDef.CONTROLLER)); 

		assertEquals(true, def.shouldGenerate(EntityDef.ENTITY)); 
		assertEquals(true, def.shouldGenerate(EntityDef.DAO)); 
		assertEquals(false, def.shouldGenerate(EntityDef.PRESENTER)); 
		assertEquals(true, def.shouldGenerate(EntityDef.CONTROLLER)); 
	}
	

	@Test
	public void testBad() throws Exception
	{
		log("--testBad--");
		createContext();
		String path = this.getTestFile("dalgen-bad.xml");
		DalGenXmlParser parser = new DalGenXmlParser(_ctx);
		boolean b = parser.parse(path);
		
		assertEquals(1, parser._entityL.size());
		assertEquals(3, parser.getErrorCount());
	}
	@Test
	
	public void testMethod() throws Exception
	{
		log("--testMethod--");
		createContext();
		String path = this.getTestFile("dalgen.xml");
		DalGenXmlParser parser = new DalGenXmlParser(_ctx);
		boolean b = parser.parse(path);
		
		assertEquals(2, parser._entityL.size());
		assertEquals(0, parser.getErrorCount());
		
		EntityDef def = parser._entityL.get(0);
		assertEquals("Task", def.name);
		
		assertEquals(1, def.methodL.size());
		assertEquals("List<Task> search_by_name(String name)", def.methodL.get(0));
	}
	
	
	@Test
	public void testTwo() throws Exception
	{
		log("--testTwo--");
		SfxContext ctx = new SfxContext();
		String path = this.getTestFile("dalgen2.xml");
		DalGenXmlParser parser = new DalGenXmlParser(ctx);
		boolean b = parser.parse(path);
		
		assertEquals(2, parser._entityL.size());
		assertEquals(0, parser.getErrorCount());
		
		EntityDef def = parser._entityL.get(1);
		assertEquals("User", def.name);
	}
}

package org.mef.dalgen.unittests;

import static org.junit.Assert.*;


import org.junit.Test;
import org.mef.dalgen.parser.DalGenXmlParser;
import org.mef.dalgen.parser.EntityDef;
import org.mef.dalgen.parser.FieldDef;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;
import org.stringtemplate.v4.compiler.STParser.namedArg_return;

import sfx.SfxContext;

public class ParseTests extends BaseTest
{
	@Test
	public void test() throws Exception
	{
		log("--test--");
		SfxContext ctx = new SfxContext();
		String path = this.getTestFile("dalgen.xml");
		DalGenXmlParser parser = new DalGenXmlParser(ctx);
		boolean b = parser.parse(path);
		
		assertEquals(1, parser._entityL.size());
		
		EntityDef def = parser._entityL.get(0);
		assertEquals("Task", def.name);
		
		assertEquals(3, def.fieldL.size());
		
		FieldDef fdef = def.fieldL.get(0);
		assertEquals("id", fdef.name);
		assertEquals("long", fdef.typeName);
		assertEquals(0, fdef.annotationL.size());
		
		fdef = def.fieldL.get(1);
		assertEquals("label", fdef.name);
		assertEquals("String", fdef.typeName);
		assertEquals(1, fdef.annotationL.size());
		assertEquals(0, parser.getErrorCount());
		
		assertEquals(1, def.queryL.size());
		assertEquals("find_by_label", def.queryL.get(0));
		assertEquals(true, def.extendInterface);
		assertEquals(true, def.extendMock);
		assertEquals(true, def.extendReal);
	}
	

	@Test
	public void testBad() throws Exception
	{
		log("--testBad--");
		SfxContext ctx = new SfxContext();
		String path = this.getTestFile("dalgen-bad.xml");
		DalGenXmlParser parser = new DalGenXmlParser(ctx);
		boolean b = parser.parse(path);
		
		assertEquals(1, parser._entityL.size());
		assertEquals(3, parser.getErrorCount());
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

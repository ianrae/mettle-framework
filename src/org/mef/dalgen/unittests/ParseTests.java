package org.mef.dalgen.unittests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import sfx.SfxXmlParser;
import sfx.SfxXmlReader;

public class ParseTests extends BaseTest
{

	@Test
	public void test() throws Exception
	{
		log("--test--");
		String path = this.getTestFile("dalgen.xml");
		SfxXmlReader r = new SfxXmlReader();
		Document doc = r.readFromFile(path);
		
		SfxXmlParser p = new SfxXmlParser(doc);
		ArrayList<Element> list = p.getAllByXPath(doc, "//entity");
		assertEquals(1, list.size());
		
		Element entityEl = list.get(0);
		Element el = p.getIthByName(entityEl, "field", 0);
		assertEquals("id:Long", el.getTextContent());
		
		//		ArrayList<Element> list = p.getAllByXPath(doc, "//field");
//		assertEquals(2, list.size());
	}

}

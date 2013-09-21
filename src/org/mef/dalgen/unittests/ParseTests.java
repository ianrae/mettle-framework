package org.mef.dalgen.unittests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

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
	public static class FieldDef
	{
		public String name;
	}
	
	public static class EntityDef
	{
		public String name;
		public List<FieldDef> fieldL = new ArrayList<FieldDef>();
	}

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
		
		EntityDef def = new EntityDef();
		def.name = getEl(entityEl, "name");
		
		assertEquals("Task", def.name);
		
		list = p.getAllByXPath(doc, "//field");
		assertEquals(2, list.size());
		
		for(int i = 0; i < 10; i++)
		{
			Element tmp = p.getIthByName(entityEl, "field", i);
			if (tmp != null)
			{
				FieldDef fdef = new FieldDef();
				fdef.name = getBody(tmp);
				def.fieldL.add(fdef);
			}
		}
		
		assertEquals(2, def.fieldL.size());
		assertEquals("id:Long", def.fieldL.get(0).name);
	}
	
	private String getEl(Element el, String name)
	{
		String s = el.getAttribute(name);
		return s.trim();
	}
	private String getBody(Element el)
	{
		String s = el.getTextContent();
		return s.trim();
	}

}

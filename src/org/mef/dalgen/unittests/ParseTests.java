package org.mef.dalgen.unittests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;
import org.stringtemplate.v4.compiler.STParser.namedArg_return;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import sfx.SfxBaseObj;
import sfx.SfxContext;
import sfx.SfxErrorTracker;
import sfx.SfxXmlParser;
import sfx.SfxXmlReader;

public class ParseTests extends BaseTest
{
	public static class FieldDef
	{
		public String name;
		public String typeName;
		public List<String> annotationL = new ArrayList<String>();
	}
	
	public static class EntityDef
	{
		public String name;
		public List<FieldDef> fieldL = new ArrayList<FieldDef>();
		public List<String> queryL = new ArrayList<String>();
		public boolean extendInterface;
		public boolean extendMock;
		public boolean extendReal;
	}
	
	public static class DalGenXmlParser extends SfxBaseObj
	{
		public List<EntityDef> _entityL = new ArrayList<EntityDef>();
		SfxErrorTracker _tracker;
		
		public DalGenXmlParser(SfxContext ctx)
		{
			super(ctx);
			_tracker = new SfxErrorTracker(ctx);

		}
		
		public boolean parse(String path) throws Exception
		{
			SfxXmlReader r = new SfxXmlReader();
			Document doc = r.readFromFile(path);
			
			SfxXmlParser p = new SfxXmlParser(doc);
			ArrayList<Element> list = p.getAllByXPath(doc, "//entity");
			
			for(Element entityEl : list)
			{
				parseEntity(p, entityEl);
			}
			return (this.getErrorCount() == 0);
		}
		
		public void parseEntity(SfxXmlParser p, Element entityEl) throws Exception
		{
			EntityDef def = new EntityDef();
			this._entityL.add(def);
			def.name = getEl(entityEl, "name");
			this._currentEntityName = def.name;
			
			if (def.name.isEmpty())
			{
				_tracker.errorOccurred("Entity name must not be empty");
			}
			
			//fields
			for(int i = 0; i < 1000; i++)
			{
				Element tmp = p.getIthByName(entityEl, "field", i);
				if (tmp == null)
				{
					break;
				}
				else
				{
					parseField(def, tmp);
				}
			}
			//queries
			for(int i = 0; i < 1000; i++)
			{
				Element tmp = p.getIthByName(entityEl, "query", i);
				if (tmp == null)
				{
					break;
				}
				else
				{
					parseQuery(def, tmp);
				}
			}
			
			//more
			def.extendInterface = getExtend(p, entityEl, "interface");
			def.extendMock = getExtend(p, entityEl,  "mock");
			def.extendReal = getExtend(p, entityEl, "real");
		}
		
		private boolean getExtend(SfxXmlParser p, Element entityEl, String name)
		{
			Element tmp = p.getIthByName(entityEl, name, 0);
			if (tmp == null)
			{
				return false;
			}
			return getBool(tmp, "extend");
		}

		private boolean getBool(Element tmp, String name) 
		{
			String s = getEl(tmp, name);
			s = s.toLowerCase();
			return (s.equals("true"));
		}

		private void parseField(EntityDef def, Element tmp)
		{
			FieldDef fdef = new FieldDef();
			String s = getBody(tmp);
			String[] ar = s.split(" ");
			if (ar.length == 0 || (ar.length == 1 && ar[0].isEmpty()))
			{
				errorOccuredInEntity("field must not be empty");
			}
			else if (ar.length == 1)
			{
				String name = ar[0].trim();
				if (name.equals("id"))
				{
					fdef.name = name;
					fdef.typeName = "long";
				}
				else
				{
					errorOccuredInEntity(String.format("field '%s' needs a type", name));
				}
			}
			if (ar.length >= 2)
			{
				int n = ar.length;
				fdef.typeName = ar[n - 2].trim();
				fdef.name = ar[n - 1].trim();
				parseAnnotations(fdef, ar);
			}
			def.fieldL.add(fdef);
		}
		
		private void parseQuery(EntityDef def, Element tmp)
		{
			String s = getBody(tmp);
			def.queryL.add(s);
		}
		
		String _currentEntityName;
		private void errorOccuredInEntity(String msg)
		{
			String s = String.format("ENTITY %s: %s", _currentEntityName, msg);
			_tracker.errorOccurred(s);
			//log(s);
		}
		
		private void parseAnnotations(FieldDef fdef, String[] ar)
		{
			int n = ar.length;
			for(int i = 0; i < (n - 2); i++)
			{
				String s = ar[i].trim();
				if (! s.startsWith("@"))
				{
					errorOccuredInEntity(String.format("field '%s' has invalid annotation: '%s'. Did you forget the '@'?", fdef.name, s));
				}
				fdef.annotationL.add(s);
			}
			
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

		public int getErrorCount() 
		{
			return _tracker.getErrorCount();
		}
	}

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
		
		assertEquals(2, def.fieldL.size());
		assertEquals(2, def.fieldL.size());
		
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

package org.mef.tools.mgen.parser;

import java.util.ArrayList;
import java.util.List;

import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;
import org.mef.framework.sfx.SfxErrorTracker;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import sfx.SfxXmlParser;
import sfx.SfxXmlReader;

public class DalGenXmlParser extends SfxBaseObj
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
		
		
		//build list of all entities
		List<EntityDef> entityTypes = new ArrayList<EntityDef>();
		for(EntityDef def : _entityL)
		{
			entityTypes.add(def);
		}

		//give each entity list of all entities
		for(EntityDef def : _entityL)
		{
			List<EntityDef> copy = new ArrayList<EntityDef>();
			copy.addAll(entityTypes);
			def.allEntityTypes = copy;
		}
		
		
		return (this.getErrorCount() == 0);
	}
	
	public void parseEntity(SfxXmlParser p, Element entityEl) throws Exception
	{
		EntityDef def = new EntityDef();
		this._entityL.add(def);
		def.name = getEl(entityEl, "name");
		def.enabled = this.getBool(entityEl, "enabled", true);
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
		//method
		for(int i = 0; i < 1000; i++)
		{
			Element tmp = p.getIthByName(entityEl, "method", i);
			if (tmp == null)
			{
				break;
			}
			else
			{
				parseMethod(def, tmp);
			}
		}
		
		//more
		def.extendEntity = getExtend(p, entityEl, "entity");
		def.extendModel = getExtend(p, entityEl, "model");
		def.extendInterface = getExtend(p, entityEl, "dal_interface");
		def.extendMock = getExtend(p, entityEl,  "dal_mock");
		def.extendReal = getExtend(p, entityEl, "dal_real");
	}
	
	private boolean getExtend(SfxXmlParser p, Element entityEl, String name)
	{
		for(int i = 0; i < 100; i++)
		{
			Element tmp = p.getIthByName(entityEl, "codegen", i);
			if (tmp == null)
			{
				break;
			}
			
			String s = tmp.getAttribute("what");
			if (s != null && s.equals(name))
			{
				return getBool(tmp, "extend", false);
			}
		}
		
		return false;
	}

	private boolean getBool(Element tmp, String name, boolean defaultVal) 
	{
		String s = getEl(tmp, name);
		if (s == null || s.isEmpty())
		{
			return defaultVal;
		}
		s = s.toLowerCase();
		return (s.equals("true"));
	}

	private void parseField(EntityDef def, Element tmp)
	{
		FieldDef fdef = new FieldDef();
		fdef.isSeedField = this.getBool(tmp, "seedWith", false);
		
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
	private void parseMethod(EntityDef def, Element tmp)
	{
		String s = getBody(tmp);
		def.methodL.add(s);
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
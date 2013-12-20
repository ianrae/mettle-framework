package persistence;

import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;
import org.mef.framework.sfx.SfxErrorTracker;

import clog.JsonTests;
import clog.JsonTests.AirportParser;
import clog.JsonTests.BigAirportParser;
import clog.JsonTests.GateParser;

public class WorldParser extends SfxBaseObj implements IIdGenerator
	{
		private JSONParser parser=new JSONParser();
		protected JSONObject obj;
		protected ReferenceList refL = new ReferenceList();
		protected ParserHelper helper;
		private HashMap<String,ParserDesc> parserMap = new HashMap<String, ParserDesc>();
		
		//render
		protected int nextId = 1;
		
		public WorldParser(SfxContext ctx)
		{
			super(ctx);
		}
		
		protected JSONObject startParse(String input) throws Exception
		{
			this.obj = (JSONObject) parser.parse(input);
			this.helper = new ParserHelper(_ctx, obj);
			return obj;
		}
		
		public Object parse(String input) throws Exception
		{
			Object obj = doParse(input);
			
			if (obj != null)
			{
				JSONArray ooo = helper.getArray("refs");
				if (ooo != null)
				{
					for(int i = 0; i < ooo.size(); i++)
					{
						JSONObject val = (JSONObject) ooo.get(i);
						if (val != null)
						{
							String type = (String)val.get("type");
							JSONArray ggg = (JSONArray) val.get("things");
							loadThings(type, ggg);
						}
					}
				}
				
				resolveRefs();
			}
			return obj;
		}

		private void loadThings(String type, JSONArray ggg) throws Exception
		{
			if (ggg == null)
			{
				return;
			}
			ParserDesc desc = this.parserMap.get(type);
			
			for(int i = 0; i < ggg.size(); i++)
			{
				JSONObject val = (JSONObject) ggg.get(i);
				if (val != null)
				{
					BaseParser gp = (BaseParser) desc.parser; //re-using parser, careful!!
					Thing target = gp.parseFromJO(val);
					desc.thingL.add(target);
				}
			}
		}
		
		
		
		private Thing doParse(String input) throws Exception
		{
			startParse(input);
			String s = helper.getString("rootType");	
			log(s);
			
			JSONObject val = helper.getEntity("root");
			ParserDesc desc = parserMap.get(s);
			
			if (desc != null)
			{
				BaseParser aparser = desc.parser;
				aparser.refL = refL;
				Thing target = aparser.parseFromJO(val);
				return target;
			}
			else
			{
				helper.errorOccured("unknown rootType: " + s);
				return null;
			}
		}
		
		
		private void resolveRefs() throws Exception
		{
			log(String.format("resolveRefs: %d", refL.refL.size()));
			for(ReferenceDesc desc : refL.refL)
			{
				Thing thing = findByRefId(desc);
				desc.parser.resolve(desc.refName, thing, desc.target);
			}
		}

		private ParserDesc findParserFor(ReferenceDesc desc) 
		{
			String name = desc.refClass.getSimpleName();
//			log(name);
			ParserDesc d2 = parserMap.get(name);
			return d2;
		}
		private Thing findByRefId(ReferenceDesc desc) 
		{
			ParserDesc d2 = findParserFor(desc);
			if (d2 == null)
			{
				return null;
			}
			
			for(Thing thing : d2.thingL)
			{
				if (thing.id == desc.refId)
				{
					return thing;
				}
			}
			return null;
		}

		//--render--
		public String render(Thing thing) 
		{
			nextId = 1; //reset
			
			String name = thing.getClass().getSimpleName();
			ParserDesc desc = parserMap.get(name);
			if (desc == null)
			{
				errorOccured("render: unknown type: " + name);
				return null;
			}
			
//			assignId(thing);
			desc.parser.refL = refL;
			String rootStr = desc.parser.render(thing, this);
			
			String refString = resolveRenderRefs();
			
			String output = String.format("{\"rootType\":\"%s\",\"root\": %s, \"refs\": [ %s ] }", name, rootStr, refString);
//			s = s.replace("RRR", "{'id':1,'flag':true,'name':'bob','size':56,'gate':{'id':2} }");
//			s = s.replace("EEE", "[ GGG ]");
//			s = s.replace("GGG", "{ 'type': 'Gate', 'things': [{'id':2, 'name':'gate1'}] }");
			
			return output;
		}

		private String resolveRenderRefs() 
		{
			String output = "";
			log(String.format("resolveRenderRefs: %d", refL.refL.size()));
			
			List<String> typeL = refL.getAllTypes();		
			for(String className : typeL)
			{
				output += resolveRenderRefsFor(className);
			}
			return output;
		}
		private String resolveRenderRefsFor(String className) 
		{
			String output = String.format("{\"type\": \"%s\"", className);
			List<ReferenceDesc> L = refL.getListFor(className);
			log(String.format("resolveRenderRefs: %s %d", className, L.size()));
			
			output += ", \"things\": [ ";
			for(ReferenceDesc desc : L)
			{
				ParserDesc d2 = findParserFor(desc);
				if (d2 == null)
				{
					return null;
				}
				output += d2.parser.render(desc.target, this);
			}
			output += " ] }";
			return output;
		}

		@Override
		public void assignId(Thing thing) 
		{
			if (thing.id != 0)
			{
				return; //already assigned
			}
			thing.id = nextId++;
		}
		
		
		public void errorOccured(String errMsg)
		{
			SfxErrorTracker tracker = (SfxErrorTracker) _ctx.getServiceLocator().getInstance(SfxErrorTracker.class);
			tracker.addError(errMsg);
		}

		public void addParser(String name, BaseParser parser) 
		{
			parserMap.put(name, new ParserDesc(parser));
		}
		
	}
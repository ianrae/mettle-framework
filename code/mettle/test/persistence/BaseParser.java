package persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;

import clog.JsonTests;

public abstract class BaseParser extends SfxBaseObj
{
	private JSONParser parser=new JSONParser();
	protected JSONObject obj;
	public ReferenceList refL;
	protected ParserHelper helper;
	
	public BaseParser(SfxContext ctx)
	{
		super(ctx);
	}
	
	protected JSONObject startParse(String input) throws Exception
	{
		this.obj = (JSONObject) parser.parse(input);
		this.helper = new ParserHelper(_ctx, obj);
		return obj;
	}
	
	protected void addRef(Thing target, String refName, Class clazz)
	{
		JSONObject oo = helper.getEntity(refName);
		addRefObj(oo, target, refName, clazz);
	}
	protected void addRefObj(JSONObject oo, Thing target, String refName, Class clazz)
	{
		ParserHelper h2 = new ParserHelper(_ctx, oo);
		int idd = h2.getInt("id");
		log("xx " + idd);
		
		ReferenceDesc desc = new ReferenceDesc();
		desc.parser = this;
		desc.refName = refName;
		desc.refClass = clazz;
		desc.refId = idd;
		desc.target = target;
		this.refL.refL.add(desc);
	}
	protected void addListRef(Thing target, String refName, Class clazz)
	{
		JSONArray ar = helper.getArray(refName);
		
		for(int i = 0; i < ar.size(); i++)
		{
			String s = String.format("%s.%d", refName, i);
			JSONObject el = (JSONObject) ar.get(i);
			addRefObj(el, target, s, clazz);
		}
	}
	
	
	abstract protected Thing createObj();
	abstract protected void onParse(Thing t) throws Exception;
	
	public Thing parse(String input) throws Exception
	{
		startParse(input);
		return parseFromJO(this.obj);
	}
	Thing parseFromJO(JSONObject jo) throws Exception
	{
		this.obj = jo;
		this.helper = new ParserHelper(_ctx, obj);
		Thing target = createObj();
		this.onParse(target);
		return target;
	}
	
	protected void parseId(Thing target)
	{
		target.id = helper.getInt("id");
	}
	
	protected void resolve(String refName, Thing refObj, Object targetParam) throws Exception
	{
	}
	
	//render
	IIdGenerator currentGenerator;
	
	public String render(Thing target, IIdGenerator generator) 
	{
		obj=new JSONObject();
		generator.assignId(target);
		currentGenerator = generator;
		
		onRender(target);
		return obj.toJSONString();
	}
	
	protected abstract void onRender(Thing target);
	
	@SuppressWarnings("unchecked")
	protected void renderRef(String refName, Thing thing)
	{
		currentGenerator.assignId(thing);
		
		HashMap <String,Object> map = new HashMap<String, Object>();
		map.put("id", thing.id);
		obj.put(refName, map);
		
		ReferenceDesc desc = new ReferenceDesc();
		desc.parser = this;
		desc.refName = refName;
		desc.refClass = thing.getClass();
		desc.refId = thing.id;
		desc.target = thing;
		this.refL.refL.add(desc);
	}

	protected void renderListRef(String refName, List<? extends Thing> L)
	{
		int i = 0;
		ArrayList<HashMap> outputL = new ArrayList<HashMap>();
		
		for (Thing thing : L)
		{
			currentGenerator.assignId(thing);
			
			HashMap <String,Object> map = new HashMap<String, Object>();
			map.put("id", thing.id);
			outputL.add(map);
//			obj.putAll(map);
//			obj.put(refName, map);
			
			ReferenceDesc desc = new ReferenceDesc();
			desc.parser = this;
			desc.refName = String.format("%s.%d", refName, i);
			desc.refClass = thing.getClass();
			desc.refId = thing.id;
			desc.target = thing;
			this.refL.refL.add(desc);
			
			i++;
		}
		
		obj.put(refName, outputL);
	}
}
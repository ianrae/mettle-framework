package persistence;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;
import org.mef.framework.sfx.SfxErrorTracker;

public class ParserHelper extends SfxBaseObj
{
	protected JSONObject obj;
	SfxErrorTracker tracker;
	
	public ParserHelper(SfxContext ctx, JSONObject obj)
	{
		super(ctx);
		this.obj = obj;
		
		tracker = (SfxErrorTracker) _ctx.getServiceLocator().getInstance(SfxErrorTracker.class);
	}
	
	public void errorOccured(String errMsg)
	{
		tracker.addError(errMsg);
	}
	
	public boolean getBool(String name)
	{
		Boolean b = (Boolean) obj.get(name);
		if (b == null)
		{
			//err
			return false;
		}
		return b;
	}
	public String getString(String name)
	{
		String s = (String) obj.get(name);
		return s; //can be null
	}
	public int getInt(String name)
	{
		Long val = (Long) obj.get(name);
		if (val == null)
		{
			//err
			return 0;
		}
		int n = val.intValue();
		return n;
	}
	
	public JSONObject getEntity(String name)
	{
		JSONObject val = (JSONObject) obj.get(name);
		return val; //can be null
	}
	public JSONArray getArray(String name)
	{
		JSONArray val = (JSONArray) obj.get(name);
		return val; //can be null
	}
}
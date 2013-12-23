package persistence;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
	public Date getDate(String name)
	{
		String s = getString(name);
		if (s == null)
		{
			//err
			return null;
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy H:m:s z"); //22 Dec 2013 20:56:05 GMT
		Date dt = null;
		try {
			dt = sdf.parse(s);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dt;
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
	
	public static String dateToString(Date dt)
	{
		if (dt == null)
		{
			return "";
		}
		return dt.toGMTString(); //!!fix later
	}
	public static Date dateFromString(String s) throws Exception
	{
		SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy H:m:s z"); //22 Dec 2013 20:56:05 GMT
		Date dt = (Date) sdf.parse(s);
		return dt;
	}
}
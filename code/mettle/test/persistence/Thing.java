package persistence;

import java.util.Date;
import java.text.SimpleDateFormat;

public class Thing
{
	public long id;
	
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
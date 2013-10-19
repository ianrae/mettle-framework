package org.mef.framework.loaders;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import org.codehaus.jackson.JsonNode;

import play.data.format.Formats;
import play.data.format.Formats.DateFormatter;

public class BaseDaoJsonLoader 
{
	protected Date readDate(JsonNode jj, String pattern)
	{
		if (jj == null)
		{
			return null;
		}
		
		Formats.DateFormatter fmt = new DateFormatter(pattern);
		Locale loc = Locale.getDefault();

		Date dt = null;
		try {
			dt = fmt.parse(jj.getTextValue(), loc);
		} catch (ParseException e) 
		{
			//log error!!
			e.printStackTrace();
		}
		return dt;
	}
}

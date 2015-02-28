package org.mef.framework.metadata;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateValue extends Value
{
	public DateValue()
	{
		this(new Date());
	}
	public DateValue(Date n)
	{
		super(n);
	}

	@Override
	protected String render()
	{
		Date dt = get();
		//default rendering is the same as HTML5 date input: yyyy-mm-dd
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String s = dateFormat.format(dt);
		return s;
	}

	@Override
	protected void parse(String input)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date dt = null;
		try {
			dt = dateFormat.parse(input);
			this.setUnderlyingValue(dt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	//return in our type
	public Date get()
	{
		Date nVal = (Date)obj;
		return nVal;
	}
	public void set(Date dt)
	{
		setUnderlyingValue(dt);
	}
}

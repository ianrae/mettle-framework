package org.mef.framework.metadata;

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
		Date n = get();
		return n.toString();
	}

	@Override
	protected void parse(String input)
	{
		long lval = Date.parse(input);
		Date dt = new Date(lval);
		this.setUnderlyingValue(dt);
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

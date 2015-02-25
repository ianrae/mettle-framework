package org.mef.framework.metadata;

import java.util.Date;


public class DateValue extends Value
{
	public DateValue()
	{
		this(null);
	}
	public DateValue(Date dt)
	{
		super(Value.TYPE_DOUBLE, dt);
	}
	
	@Override
	public String render()
	{
		Date d = this.getDate();
		return d.toString();
	}
	
	//return in our type
	public Date get()
	{
		return this.getDate();
	}
	public void setValue(Date dt)
	{
		this.forceValueObject(dt);
	}

}

package org.mef.framework.metadata;


public class LongValue extends Value
{
	public LongValue(long val)
	{
		super(Value.TYPE_LONG, val);
	}
	
	@Override
	public String render()
	{
		Long n = this.getLong();
		return n.toString();
	}
	
	//return in our type
	public long get()
	{
		return this.getLong();
	}
	public void setValue(long n)
	{
		this.forceValueObject(new Long(n));
	}

}

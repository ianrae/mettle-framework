package org.mef.framework.metadata;

public class LongValue extends Value
{
	public LongValue()
	{
		this(0L);
	}
	public LongValue(Long n)
	{
		super(n);
	}

	@Override
	protected String render()
	{
		Long n = get();
		return n.toString();
	}

	@Override
	protected void parse(String input)
	{
		Long n = Long.parseLong(input);
		this.setUnderlyingValue(n);
	}

	//return in our type
	public Long get()
	{
		Long nVal = (Long)obj;
		return nVal;
	}
	public void set(Long nVal)
	{
		setUnderlyingValue(nVal);
	}
}

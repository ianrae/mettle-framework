package org.mef.framework.metadata;

public class IntegerValue extends Value
{
	public IntegerValue()
	{
		this(0);
	}
	public IntegerValue(Integer n)
	{
		super(n);
	}

	@Override
	protected String render()
	{
		Integer n = get();
		return n.toString();
	}

	@Override
	protected void parse(String input)
	{
		Integer n = Integer.parseInt(input);
		this.setUnderlyingValue(n);
	}

	//return in our type
	public Integer get()
	{
		Integer nVal = (Integer)obj;
		return nVal;
	}
	public void set(Integer nVal)
	{
		setUnderlyingValue(nVal);
	}
}

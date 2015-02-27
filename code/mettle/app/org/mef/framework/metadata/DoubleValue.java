package org.mef.framework.metadata;

public class DoubleValue extends Value
{
	public DoubleValue()
	{
		this(0.0);
	}
	public DoubleValue(Double n)
	{
		super(n);
	}

	@Override
	protected String render()
	{
		Double n = get();
		return n.toString();
	}

	@Override
	protected void parse(String input)
	{
		Double n = Double.parseDouble(input);
		this.setUnderlyingValue(n);
	}

	//return in our type
	public Double get()
	{
		Double nVal = (Double)obj;
		return nVal;
	}
	public void set(Double nVal)
	{
		setUnderlyingValue(nVal);
	}
}

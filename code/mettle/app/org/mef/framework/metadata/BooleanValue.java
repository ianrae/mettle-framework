package org.mef.framework.metadata;

public class BooleanValue extends Value
{
	public BooleanValue()
	{
		this(false);
	}
	public BooleanValue(Boolean n)
	{
		super(n);
	}

	@Override
	protected String render()
	{
		Boolean n = get();
		return n.toString();
	}

	@Override
	protected void parse(String input)
	{
		Boolean n = Boolean.parseBoolean(input);
		this.setUnderlyingValue(n);
	}

	//return in our type
	public Boolean get()
	{
		Boolean nVal = (Boolean)obj;
		return new Boolean(nVal);
	}
	public void set(Boolean bVal)
	{
		setUnderlyingValue(bVal);
	}
}

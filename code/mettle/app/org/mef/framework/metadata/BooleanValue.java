package org.mef.framework.metadata;


public class BooleanValue extends Value
{
	public BooleanValue()
	{
		this(false);
	}
	public BooleanValue(boolean val)
	{
		super(Value.TYPE_BOOLEAN, val);
	}

	@Override
	public String render()
	{
		Boolean b = this.getBoolean();
		return b.toString();
	}
	
	//return in our type
	public boolean get()
	{
		return this.getBoolean();
	}
	public void setValue(boolean b)
	{
		this.forceValueObject(new Boolean(b));
	}
}

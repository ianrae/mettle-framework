package org.mef.framework.metadata;


public class BooleanValue extends Value
{
	public BooleanValue(boolean val)
	{
		super(Value.TYPE_BOOLEAN, val);
	}

	@Override
	public String toString() 
	{
		Boolean b = this.getBoolean();
		return b.toString();
	}

	
}

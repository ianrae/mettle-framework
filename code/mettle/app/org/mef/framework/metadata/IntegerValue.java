package org.mef.framework.metadata;


public class IntegerValue extends Value
{
	public IntegerValue(int val)
	{
		super(Value.TYPE_INT, val);
	}
	
	@Override
	public String toString() 
	{
		Integer n = this.getInt();
		return n.toString();
	}
	
	//return in our type
	public int get()
	{
		return this.getInt();
	}
	public void setValue(int n)
	{
		this.forceValueObject(new Integer(n));
	}

}

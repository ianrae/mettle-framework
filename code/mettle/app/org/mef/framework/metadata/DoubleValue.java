package org.mef.framework.metadata;


public class DoubleValue extends Value
{
	public DoubleValue(double val)
	{
		super(Value.TYPE_DOUBLE, val);
	}
	
	@Override
	public String toString() 
	{
		Double d = this.getDouble();
		return d.toString();
	}
	
	//return in our type
	public double get()
	{
		return this.getDouble();
	}
	public void setValue(double d)
	{
		this.forceValueObject(new Double(d));
	}

}

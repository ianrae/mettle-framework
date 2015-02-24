package org.mef.framework.metadata;


public class StringValue extends Value
{
	public StringValue()
	{
		this(null);
	}
	public StringValue(String val)
	{
		super(Value.TYPE_STRING, val);
	}

	@Override
	public String render() 
	{
		String s = this.getString();
		return s;
	}

	//return in our type
	public String get()
	{
		return this.getString();
	}
	public void setValue(String s)
	{
		this.forceValueObject(s);
	}

}
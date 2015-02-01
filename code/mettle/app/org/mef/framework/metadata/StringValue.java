package org.mef.framework.metadata;


public class StringValue extends Value
	{
//		public MString()
//		{
//			super(MValue.TYPE_STRING);
//		}
		public StringValue(String val)
		{
			super(Value.TYPE_STRING, val);
		}
		
		
		@Override
		public String toString() 
		{
			String s = this.getString();
			return s;
		}
		
		//return in our type
		public String get()
		{
			return this.getString();
		}
	}
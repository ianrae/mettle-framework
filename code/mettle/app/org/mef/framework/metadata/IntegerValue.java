package org.mef.framework.metadata;


//////////////////////////////////////////////////////////////////
	//new idea: define the metadata in a series of classes. that is, use java as a DSL
	//then have tool that uses reflection to generate json
	//then have ui that reads json and allows modifications (of values only, no new types)
	//ui must do validation on any changed values (it can load the validator classes)
	//final json read at production time. read raw, don't need to validate again (but maybe in case we hacked the json file)
	//use same classes or gen some simple POJOS?
	public class IntegerValue extends Value
	{
//		public MInteger()
//		{
//			super(MValue.TYPE_INT);
//		}
		public IntegerValue(int val)
		{
			super(Value.TYPE_INT, val);
		}
		
	}
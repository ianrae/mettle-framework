package org.mef.framework.metadata;
import org.mef.framework.metadata.validate.IValidator;
import org.mef.framework.metadata.validate.ValidationErrors;



public class Value 
	{
		public static final int TYPE_INT=1;
		public static final int TYPE_STRING=2;
		public static final int TYPE_TUPLE=3;
		public static final int TYPE_LIST=4;
		
		private int typeOfValue;
		private Object obj;
		private IValidator validator;
		private String validatorItemName;  //eg. "email"
		
		public Value(int typeOfValue)
		{
			this.typeOfValue = typeOfValue;
		}
		public Value(int typeOfValue, int val)
		{
			this(typeOfValue);
			if (typeOfValue == TYPE_INT)
			{
				obj = new Integer(val);
			}
		}
		public Value(int typeOfValue, String val)
		{
			this(typeOfValue);
			if (typeOfValue == TYPE_STRING)
			{
				obj = val;
			}
		}
		
		//deep copy
		public Value(Value src) 
		{
			this.typeOfValue = src.typeOfValue;
			this.validator = src.validator;
			
			switch(this.typeOfValue)
			{
			case TYPE_INT:
				this.obj = new Integer((Integer)src.obj);
				break;
			case TYPE_STRING:
				this.obj = new String((String)src.obj);
				break;
			case TYPE_TUPLE:
				this.obj = new TupleValue((TupleValue)src.obj);
				break;
			case TYPE_LIST:
				this.obj = new ListValue((ListValue)src.obj);
				break;
			default:
				break; //err!!
			}
		}
		public int getInt()
		{
			throwIfNot(TYPE_INT);
			Integer nObj = (Integer)obj;
			return nObj;
		}
		public String getString()
		{
			throwIfNot(TYPE_STRING);
			String s = (String)obj;
			return s;
		}
		
		public void forceValue(Object obj)
		{
			this.obj = obj;
		}
		
		public TupleValue getTuple() 
		{
			throwIfNot(TYPE_TUPLE);
			TupleValue tuple = (TupleValue)obj;
			return tuple;
		}
		public Value field(String fieldName) 
		{
			throwIfNot(TYPE_TUPLE);
			TupleValue tuple = (TupleValue)obj;
			return tuple.field(fieldName);
		}
		
		public ListValue getList() 
		{
			throwIfNot(TYPE_LIST);
			ListValue list = (ListValue)obj;
			return list;
		}
		
		
		//helpers
		protected void throwIfNot(int expectedType)
		{
			if (typeOfValue != expectedType)
			{
				throw new IllegalArgumentException(); //!!
			}
		}
		
		
		
		//validation
		public boolean validate(ValidationErrors errors)
		{
			if (validator == null)
			{
				return true;
			}
			return validator.validate(obj, errors);
		}
		
		public IValidator getValidator() {
			return validator;
		}
		public void setValidator(String itemName, IValidator validator) {
			this.validator = validator;
			this.validatorItemName = itemName;
		}
		
//		@Override
//		public void validate(ValContext vtx) 
//		{
//			vtx.validate(this);
//		}
		
	    public String getItemName()
	    {
	    	return validatorItemName;
	    }
	}
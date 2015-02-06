package org.mef.framework.metadata;
import java.util.Locale;

import org.mef.framework.metadata.validate.IValidator;
import org.mef.framework.metadata.validate.ValidationErrors;



public class Value 
{
	public static final int TYPE_INT=1;
	public static final int TYPE_STRING=2;
	public static final int TYPE_TUPLE=3;
	public static final int TYPE_LIST=4;
	public static final int TYPE_BOOLEAN=5;
	public static final int TYPE_DOUBLE=6;
	public static final int TYPE_LONG=7;

	private int typeOfValue;
	private Object obj;
	private IValidator validator;
	private String validatorItemName;  //eg. "email"
	private Converter converter; //can be null
	
	//static
	private static ValueHandlerRegistry reg = new ValueHandlerRegistry();

	public Value(int typeOfValue)
	{
		this.typeOfValue = typeOfValue;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Value(int typeOfValue, int val)
	{
		this(typeOfValue);
		if (typeOfValue == TYPE_INT)
		{
			ValueHandler h = reg.get(TYPE_INT);
			obj = h.toObj(new Integer(val));
		}
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Value(int typeOfValue, long val)
	{
		this(typeOfValue);
		if (typeOfValue == TYPE_LONG)
		{
			ValueHandler h = reg.get(TYPE_LONG);
			obj = h.toObj(new Long(val));
		}
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Value(int typeOfValue, String val)
	{
		this(typeOfValue);
		if (typeOfValue == TYPE_STRING)
		{
			ValueHandler h = reg.get(TYPE_STRING);
			obj = h.toObj(val);
		}
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Value(int typeOfValue, boolean b)
	{
		this(typeOfValue);
		if (typeOfValue == TYPE_BOOLEAN)
		{
			ValueHandler h = reg.get(TYPE_BOOLEAN);
			obj = h.toObj(new Boolean(b));
		}
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Value(int typeOfValue, double d)
	{
		this(typeOfValue);
		if (typeOfValue == TYPE_DOUBLE)
		{
			ValueHandler h = reg.get(TYPE_DOUBLE);
			obj = h.toObj(new Double(d));
		}
	}

	//deep copy
	@SuppressWarnings("rawtypes")
	public Value(Value src) 
	{
		this.typeOfValue = src.typeOfValue;
		this.validator = src.validator;
		
		ValueHandler h = reg.get(this.typeOfValue);
		this.obj = h.copy(src.obj);
	}
	
	public boolean fromString(String sVal) 
	{
		boolean b = false;

		try
		{
			if (hasConverter())
			{
				b = fromStringUsingConverter(sVal);
			}
			else
			{
				b = fromStringImpl(sVal);
			}
		}
		catch(Exception ex)
		{}

		return b;
	}
	public boolean fromStringImpl(String sVal) throws Exception
	{
		boolean ok = true;
		
		switch(this.typeOfValue)
		{
		case TYPE_INT:
			this.obj = Integer.parseInt(sVal);
			break;
		case TYPE_LONG:
			this.obj = Long.parseLong(sVal);
			break;
		case TYPE_STRING:
			this.obj = sVal;
			break;
		case TYPE_BOOLEAN:
			this.obj = Boolean.parseBoolean(sVal);
			break;
		case TYPE_DOUBLE:
			this.obj = Double.parseDouble(sVal);
			break;

		case TYPE_TUPLE:
		case TYPE_LIST:
		default:
			ok = false;
			break; 
		}
		return ok;
	}



	public int getInt()
	{
		throwIfNot(TYPE_INT);
		Integer nObj = (Integer)obj;
		return nObj;
	}
	public long getLong()
	{
		throwIfNot(TYPE_LONG);
		Long nObj = (Long)obj;
		return nObj;
	}
	public String getString()
	{
		throwIfNot(TYPE_STRING);
		String s = (String)obj;
		return s;
	}
	public boolean getBoolean()
	{
		throwIfNot(TYPE_BOOLEAN);
		Boolean b = (Boolean)obj;
		return b;
	}
	public double getDouble()
	{
		throwIfNot(TYPE_DOUBLE);
		Double d = (Double)obj;
		return d;
	}

	public void forceValueObject(Object obj)
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
	
	
	public Converter getConverter() {
		return converter;
	}
	public void setConverter(Converter converter) {
		this.converter = converter;
	}
	public boolean hasConverter() 
	{
		return (converter != null);
	}
	
	public String convert()
	{
		String s = null;
		Locale locale = Locale.getDefault(); //fix later!!
		
		switch(this.typeOfValue)
		{
		case TYPE_INT:
			s = converter.printInt(this.getInt(), locale);
			break;
		case TYPE_STRING:
			s = converter.printString(this.getString(), locale);
			break;
		case TYPE_BOOLEAN:
			s = converter.printBoolean(this.getBoolean(), locale);
			break;
		case TYPE_DOUBLE:
			s = converter.printDouble(this.getDouble(), locale);
			break;

		case TYPE_TUPLE:
		case TYPE_LIST:
		default:
			//err!!ok = false;
			break; 
		}
		return s;
	}
	
	private boolean fromStringUsingConverter(String sVal) 
	{
		boolean ok = true;
		Locale locale = Locale.getDefault(); //fix later!!
		
		switch(this.typeOfValue)
		{
		case TYPE_INT:
			obj = new Integer(converter.parseInt(sVal, locale));
			break;
		case TYPE_STRING:
			obj = converter.parseString(sVal, locale);
			break;
		case TYPE_BOOLEAN:
			obj = new Boolean(converter.parseBoolean(sVal, locale));
			break;
		case TYPE_DOUBLE:
			obj = new Double(converter.parseDouble(sVal, locale));
			break;

		case TYPE_TUPLE:
		case TYPE_LIST:
		default:
			ok = false;
			//err!!ok = false;
			break; 
		}
		return ok;
	}
	
	@Override
	public String toString() 
	{
		if (hasConverter())
		{
			return convert();
		}
		return render();
	}

	//you must override this!
	public String render()
	{
		return null;
	}
	
}
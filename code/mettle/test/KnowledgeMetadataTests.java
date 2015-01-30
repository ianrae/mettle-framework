

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import tools.BaseTest;


public class KnowledgeMetadataTests extends BaseTest
{
	//todo: add Tuple which has map of name+MValue
	
	
//	types: int,float,bool,string,enum,struct,list,set
	//customtypes: derive from type. syntactic sugar. eg PostalCode
	
//	public static class ValueObj
//	{
//		public int val;
//	}
	
	public static class ValidationErrorSpec
	{
	    public String key;
	    public String message;
	}
	
	public static class ValidationErrors
	{
	    public Map<String,List<ValidationErrorSpec>> map;
	    private String itemName;
	    
	    public String getItemName()
	    {
	    	return itemName;
	    }
	    
	    public void addError(String message)
	    {
	    	ValidationErrorSpec spec = new ValidationErrorSpec();
	    	spec.key = itemName;
	    	spec.message = message;
	    	
	    	List<ValidationErrorSpec> L = map.get(itemName);
	    	if (L == null)
	    	{
	    		L = new ArrayList<ValidationErrorSpec>();
	    	}
	    	L.add(spec);
	    	map.put(itemName, L);
	    }
	}
	
	public static class ValContext
	{
		private int failCount;
	    private Map<String,List<ValidationErrorSpec>> mapErrors;
		
		public ValContext()
		{
			mapErrors = new HashMap<String,List<ValidationErrorSpec>>();
		}
		
		public void validate(Value val)
		{
			ValidationErrors errors = new ValidationErrors();
			errors.map = mapErrors;
			errors.itemName = val.getItemName();
			if (! val.validate(errors))
			{
				failCount++;
			}
		}
		
		public int getFailCount()
		{
			return failCount;
		}
		
	    public Map<String,List<ValidationErrorSpec>> getErrors()
	    {
	    	return mapErrors;
	    }
	}
	
	public interface IValidator
	{
		boolean validate(Object val, ValidationErrors errors);
	}
	
	public static class NoneValidator implements IValidator
	{
		@Override
		public boolean validate(Object val, ValidationErrors errors) 
		{
			return true;
		}
	}
	
	public static class NotEmptyStringValidator implements IValidator
	{
		@Override
		public boolean validate(Object val, ValidationErrors errors) 
		{
			String s = (String)val;
			if (s.isEmpty())
			{
				errors.addError("empty string not allowed");
				return false;
			}
			return true;
		}
	}
	
	public static interface ValueContainer
	{
		void validate(ValContext vtx);
	}
	
	public static class Value 
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
	
	public static class MTypeRegistry
	{
		private HashMap<String, Value> map;
		
		public MTypeRegistry()
		{
			map = new HashMap<String, KnowledgeMetadataTests.Value>();
			regDefaultTypes();
		}
		
		private void regDefaultTypes() 
		{
			Value val = new Value(Value.TYPE_INT, 0);
			map.put("int", val);
			
			val = new Value(Value.TYPE_STRING, "");
			map.put("string", val);
			
		}

		public Value create(String typeName)
		{
			Value val = map.get(typeName);
			
			//deep copy
			Value copy = new Value(val);
//			if (copy.typeOfValue == MValue.TYPE_TUPLE)
//			{
//				MTupleValueObject newtuple = new MTupleValueObject(copy.getTuple());
//				copy.forceValue(newtuple);
//			}
			return copy;
		}
		
		public void register(String typeName, Value val)
		{
			map.put(typeName, val);
		}
	}
	
	
	public static class TupleValue implements ValueContainer
	{
		private HashMap<String, Value> map;
		
		public TupleValue()
		{
			map = new HashMap<String, KnowledgeMetadataTests.Value>();
		}
		
		public TupleValue(TupleValue src)
		{
			map = new HashMap<String, KnowledgeMetadataTests.Value>();
			for(String fieldName : src.map.keySet())
			{
				Value val = src.map.get(fieldName);
				Value copy = new Value(val);
				map.put(fieldName, copy);
			}
		}
		
		public void addField(String fieldName, Value val)
		{
			map.put(fieldName, val);
		}

		public Value field(String fieldName) 
		{
			Value field = map.get(fieldName);
			return field;
		}
		
		//validation
		public void validate(ValContext vtx)
		{
			for(String fieldName : map.keySet())
			{
				Value val = map.get(fieldName);
				vtx.validate(val);
			}
		}
		
		
	}
	
	public static class ListValue implements ValueContainer
	{
		private List<Value> list;
		
		public ListValue()
		{
			list = new ArrayList<Value>();
		}
		
		public ListValue(ListValue src)
		{
			list = new ArrayList<Value>();
			for(Value val : src.list)
			{
				Value copy = new Value(val);
				list.add(copy);
			}
		}
		
		public void addElement(Value val)
		{
			list.add(val);
		}

		public Value getIth(int index) 
		{
			Value val = list.get(index);
			return val;
		}

		public Object size() 
		{
			return list.size();
		}
		
		//validation
		public void validate(ValContext vtx)
		{
			for(Value val : list)
			{
				vtx.validate(val);
			}
		}
	}
	
	//sample wrapper class that could be codegen'd
	public static class PersonNamePOJO
	{
		Value value;
		public PersonNamePOJO(Value value)
		{
			this.value = value;
		}
		
		public String getFirstName()
		{
			return value.field("firstName").getString();
		}
		public String getLastName()
		{
			return value.field("lastName").getString();
		}
	}
	
	//////////////////////////////////////////////////////////////////
	//new idea: define the metadata in a series of classes. that is, use java as a DSL
	//then have tool that uses reflection to generate json
	//then have ui that reads json and allows modifications (of values only, no new types)
	//ui must do validation on any changed values (it can load the validator classes)
	//final json read at production time. read raw, don't need to validate again (but maybe in case we hacked the json file)
	//use same classes or gen some simple POJOS?
	public static class IntegerValue extends Value
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
	public static class StringValue extends Value
	{
//		public MString()
//		{
//			super(MValue.TYPE_STRING);
//		}
		public StringValue(String val)
		{
			super(Value.TYPE_STRING, val);
		}
		
	}
	
	public static class SmallInt extends IntegerValue
	{
//		public SmallInt()
//		{
//			super();
//			//validator(new RangeValidator(1,10);
//		}
		public SmallInt(int val)
		{
			super(val);
			//validator(new RangeValidator(1,10);
		}
	}
	
	public static class PersonName implements ValueContainer
	{
		public StringValue firstName;
		public StringValue lastName;
		
		public PersonName(String string, String string2) 
		{
			firstName = new StringValue(string);
			firstName.setValidator("firstName", new NotEmptyStringValidator());
			lastName = new StringValue(string2);
			lastName.setValidator("secondName", new NotEmptyStringValidator());
			
		}

		@Override
		public void validate(ValContext vtx)
		{
			vtx.validate(firstName);
			vtx.validate(lastName);
		}
		
	}
	
	public static class System implements ValueContainer
	{
		//@Description("sdfsfd");
		public IntegerValue retries =  new IntegerValue(5);
		
		public SmallInt weekday = new SmallInt(4);
		
		public PersonName joe = new PersonName("bob", "smith");
		
		public void validate(ValContext vtx)
		{
			vtx.validate(retries);
			vtx.validate(weekday);
			joe.validate(vtx);
		}
	}
	
	@Test
	public void test() 
	{
		Value val = new Value(Value.TYPE_INT, 0);
		assertEquals(0, val.getInt());
		
		val = new Value(Value.TYPE_INT, 23);
		assertEquals(23, val.getInt());
		
		val = new Value(Value.TYPE_STRING, "abc");
		assertEquals("abc", val.getString());
	}
	
	@Test
	public void testReg() 
	{
		MTypeRegistry reg = new MTypeRegistry();
		
		Value val = reg.create("int");
		assertEquals(0, val.getInt());
		
		val = reg.create("string");
		assertEquals("", val.getString());
		
		val = new Value(Value.TYPE_INT, 14);
		reg.register("PosInt", val);
		
		val = reg.create("PosInt");
		assertEquals(14, val.getInt());
		val.forceValue(new Integer(33));
		assertEquals(33, val.getInt());
		
		val = reg.create("PosInt");
		assertEquals(14, val.getInt());
	}

	@Test
	public void testTuple() 
	{
		MTypeRegistry reg = new MTypeRegistry();
		
		TupleValue tuple = new TupleValue();
		tuple.addField("firstName", reg.create("string"));
		tuple.addField("lastName", reg.create("string"));
		
		Value obj = new Value(Value.TYPE_TUPLE);
		obj.forceValue(tuple);
		reg.register("personName", obj);
		
		Value val = reg.create("personName");
		TupleValue tup = val.getTuple();
		Value field1 = tup.field("firstName");
		assertEquals("", field1.getString());
		Value field2 = tup.field("lastName");
		assertEquals("", field2.getString());
		field1.forceValue("bob");
		field2.forceValue("jones");
		
		field1 = tup.field("firstName");
		field2 = tup.field("lastName");
		assertEquals("bob", field1.getString());
		assertEquals("jones", field2.getString());
		
		Value val2 = reg.create("personName");
		TupleValue tup2 = val2.getTuple();
		field1 = tup2.field("firstName");
		assertEquals("", field1.getString());
		
		assertEquals("jones", tup.field("lastName").getString());
		assertEquals("jones", val.field("lastName").getString());
		
		PersonNamePOJO pojo = new PersonNamePOJO(val);
		assertEquals("bob", pojo.getFirstName());
		assertEquals("jones", pojo.getLastName());
	}

	@Test
	public void testList() 
	{
		MTypeRegistry reg = new MTypeRegistry();
		
		ListValue listobj = new ListValue();
		Value el1 = reg.create("string");
		el1.forceValue("abc");
		listobj.addElement(el1);
		assertEquals(1, listobj.size());
		
		Value xval = new Value(Value.TYPE_LIST);
		xval.forceValue(listobj);
		reg.register("mylist", xval);
		
		Value val = reg.create("mylist");
		ListValue z = val.getList();
		assertEquals(1, z.size());
		
		Value el100 = z.getIth(0);
		assertEquals("abc", el100.getString());
		el100.forceValue("def");
		
		Value val2 = reg.create("mylist");
		ListValue z2 = val2.getList();
		assertEquals(1, z2.size());
		
		Value el101 = z2.getIth(0);
		assertEquals("abc", el101.getString());
		z2.addElement(reg.create("string"));

		assertEquals(1, z.size());
		assertEquals(2, z2.size());
	}
	
	@Test
	public void testNewIdea() 
	{
		System sys = new System();
		
		ValContext vtx = new ValContext();
		sys.validate(vtx);
		assertEquals(0, vtx.getFailCount());
		
		assertEquals("bob", sys.joe.firstName.getString());
	}
	
	@Test
	public void testValFail() 
	{
		System sys = new System();
		PersonName badjoe = new PersonName("", "smith");
		sys.joe = badjoe;
		
		ValContext vtx = new ValContext();
		sys.validate(vtx);
		assertEquals(1, vtx.getFailCount());
	    Map<String,List<ValidationErrorSpec>> errors = vtx.getErrors();
	    for(String key : errors.keySet())
	    {
	    	List<ValidationErrorSpec> L = errors.get(key);
	    	for(ValidationErrorSpec spec : L)
	    	{
	    		log(String.format("err %s: %s", spec.key, spec.message));
	    	}
	    }

		
		assertEquals("", sys.joe.firstName.getString());
	}
}

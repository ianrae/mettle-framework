

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
	
	public static class ValErrorX
	{
	    public Map<String,List<ValidationErrorSpec>> errors;
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
	    	
	    	List<ValidationErrorSpec> L = errors.get(itemName);
	    	if (L == null)
	    	{
	    		L = new ArrayList<ValidationErrorSpec>();
	    	}
	    	L.add(spec);
	    	errors.put(itemName, L);
	    }
	}
	
	public static class ValContext
	{
		private int failCount;
	    private Map<String,List<ValidationErrorSpec>> errors;
		
		public ValContext()
		{
			errors = new HashMap<String,List<ValidationErrorSpec>>();
		}
		
		public void validate(MValue val)
		{
			ValErrorX vex = new ValErrorX();
			vex.errors = errors;
			vex.itemName = val.getItemName();
			if (! val.validate(vex))
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
	    	return errors;
	    }
	}
	
	public interface IValidator
	{
		boolean validate(Object val, ValErrorX vex);
	}
	
	public static class NoneValidator implements IValidator
	{
		@Override
		public boolean validate(Object val, ValErrorX vex) 
		{
			return true;
		}
	}
	
	public static class NotEmptyStringValidator implements IValidator
	{
		@Override
		public boolean validate(Object val, ValErrorX vex) 
		{
			String s = (String)val;
			if (s.isEmpty())
			{
				vex.addError("empty string not allowed");
				return false;
			}
			return true;
		}
	}
	
	public static interface IBaseObj
	{
		void validate(ValContext vtx);
	}
	
	public static class MValue implements IBaseObj
	{
		public static final int TYPE_INT=1;
		public static final int TYPE_STRING=2;
		public static final int TYPE_TUPLE=3;
		public static final int TYPE_LIST=4;
		
		private int typeOfValue;
		private Object obj;
		private IValidator validator;
		private String validatorItemName;  //eg. "email"
		
		public MValue(int typeOfValue)
		{
			this.typeOfValue = typeOfValue;
		}
		public MValue(int typeOfValue, int val)
		{
			this(typeOfValue);
			if (typeOfValue == TYPE_INT)
			{
				obj = new Integer(val);
			}
		}
		public MValue(int typeOfValue, String val)
		{
			this(typeOfValue);
			if (typeOfValue == TYPE_STRING)
			{
				obj = val;
			}
		}
		
		//deep copy
		public MValue(MValue src) 
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
				this.obj = new MTupleValueObject((MTupleValueObject)src.obj);
				break;
			case TYPE_LIST:
				this.obj = new MListValueObject((MListValueObject)src.obj);
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
		
		public MTupleValueObject getTuple() 
		{
			throwIfNot(TYPE_TUPLE);
			MTupleValueObject tuple = (MTupleValueObject)obj;
			return tuple;
		}
		public MValue field(String fieldName) 
		{
			throwIfNot(TYPE_TUPLE);
			MTupleValueObject tuple = (MTupleValueObject)obj;
			return tuple.field(fieldName);
		}
		
		public MListValueObject getList() 
		{
			throwIfNot(TYPE_LIST);
			MListValueObject list = (MListValueObject)obj;
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
		public boolean validate(ValErrorX vex)
		{
			if (validator == null)
			{
				return true;
			}
			return validator.validate(obj, vex);
		}
		
		public IValidator getValidator() {
			return validator;
		}
		public void setValidator(String itemName, IValidator validator) {
			this.validator = validator;
			this.validatorItemName = itemName;
		}
		
		@Override
		public void validate(ValContext vtx) 
		{
			vtx.validate(this);
		}
		
	    public String getItemName()
	    {
	    	return validatorItemName;
	    }
	}
	
	public static class MTypeRegistry
	{
		private HashMap<String, MValue> map;
		
		public MTypeRegistry()
		{
			map = new HashMap<String, KnowledgeMetadataTests.MValue>();
			regDefaultTypes();
		}
		
		private void regDefaultTypes() 
		{
			MValue val = new MValue(MValue.TYPE_INT, 0);
			map.put("int", val);
			
			val = new MValue(MValue.TYPE_STRING, "");
			map.put("string", val);
			
		}

		public MValue create(String typeName)
		{
			MValue val = map.get(typeName);
			
			//deep copy
			MValue copy = new MValue(val);
//			if (copy.typeOfValue == MValue.TYPE_TUPLE)
//			{
//				MTupleValueObject newtuple = new MTupleValueObject(copy.getTuple());
//				copy.forceValue(newtuple);
//			}
			return copy;
		}
		
		public void register(String typeName, MValue val)
		{
			map.put(typeName, val);
		}
	}
	
	
	public static class MTupleValueObject implements IBaseObj
	{
		private HashMap<String, MValue> map;
		
		public MTupleValueObject()
		{
			map = new HashMap<String, KnowledgeMetadataTests.MValue>();
		}
		
		public MTupleValueObject(MTupleValueObject src)
		{
			map = new HashMap<String, KnowledgeMetadataTests.MValue>();
			for(String fieldName : src.map.keySet())
			{
				MValue val = src.map.get(fieldName);
				MValue copy = new MValue(val);
				map.put(fieldName, copy);
			}
		}
		
		public void addField(String fieldName, MValue val)
		{
			map.put(fieldName, val);
		}

		public MValue field(String fieldName) 
		{
			MValue field = map.get(fieldName);
			return field;
		}
		
		//validation
		public void validate(ValContext vtx)
		{
			for(String fieldName : map.keySet())
			{
				MValue val = map.get(fieldName);
				vtx.validate(val);
			}
		}
		
		
	}
	
	public static class MListValueObject implements IBaseObj
	{
		private List<MValue> list;
		
		public MListValueObject()
		{
			list = new ArrayList<MValue>();
		}
		
		public MListValueObject(MListValueObject src)
		{
			list = new ArrayList<MValue>();
			for(MValue val : src.list)
			{
				MValue copy = new MValue(val);
				list.add(copy);
			}
		}
		
		public void addElement(MValue val)
		{
			list.add(val);
		}

		public MValue getIth(int index) 
		{
			MValue val = list.get(index);
			return val;
		}

		public Object size() 
		{
			return list.size();
		}
		
		//validation
		public void validate(ValContext vtx)
		{
			for(MValue val : list)
			{
				vtx.validate(val);
			}
		}
	}
	
	//sample wrapper class that could be codegen'd
	public static class PersonNamePOJO
	{
		MValue value;
		public PersonNamePOJO(MValue value)
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
	public static class MInteger extends MValue
	{
//		public MInteger()
//		{
//			super(MValue.TYPE_INT);
//		}
		public MInteger(int val)
		{
			super(MValue.TYPE_INT, val);
		}
		
	}
	public static class MString extends MValue
	{
//		public MString()
//		{
//			super(MValue.TYPE_STRING);
//		}
		public MString(String val)
		{
			super(MValue.TYPE_STRING, val);
		}
		
	}
	
	public static class SmallInt extends MInteger
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
	
	public static class PersonName implements IBaseObj
	{
		public MString firstName;
		public MString lastName;
		
		public PersonName(String string, String string2) 
		{
			firstName = new MString(string);
			firstName.setValidator("firstName", new NotEmptyStringValidator());
			lastName = new MString(string2);
			lastName.setValidator("secondName", new NotEmptyStringValidator());
			
		}

		@Override
		public void validate(ValContext vtx)
		{
			vtx.validate(firstName);
			vtx.validate(lastName);
		}
		
	}
	
	public static class System implements IBaseObj
	{
		//@Description("sdfsfd");
		public MInteger retries =  new MInteger(5);
		
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
		MValue val = new MValue(MValue.TYPE_INT, 0);
		assertEquals(0, val.getInt());
		
		val = new MValue(MValue.TYPE_INT, 23);
		assertEquals(23, val.getInt());
		
		val = new MValue(MValue.TYPE_STRING, "abc");
		assertEquals("abc", val.getString());
	}
	
	@Test
	public void testReg() 
	{
		MTypeRegistry reg = new MTypeRegistry();
		
		MValue val = reg.create("int");
		assertEquals(0, val.getInt());
		
		val = reg.create("string");
		assertEquals("", val.getString());
		
		val = new MValue(MValue.TYPE_INT, 14);
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
		
		MTupleValueObject tuple = new MTupleValueObject();
		tuple.addField("firstName", reg.create("string"));
		tuple.addField("lastName", reg.create("string"));
		
		MValue obj = new MValue(MValue.TYPE_TUPLE);
		obj.forceValue(tuple);
		reg.register("personName", obj);
		
		MValue val = reg.create("personName");
		MTupleValueObject tup = val.getTuple();
		MValue field1 = tup.field("firstName");
		assertEquals("", field1.getString());
		MValue field2 = tup.field("lastName");
		assertEquals("", field2.getString());
		field1.forceValue("bob");
		field2.forceValue("jones");
		
		field1 = tup.field("firstName");
		field2 = tup.field("lastName");
		assertEquals("bob", field1.getString());
		assertEquals("jones", field2.getString());
		
		MValue val2 = reg.create("personName");
		MTupleValueObject tup2 = val2.getTuple();
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
		
		MListValueObject listobj = new MListValueObject();
		MValue el1 = reg.create("string");
		el1.forceValue("abc");
		listobj.addElement(el1);
		assertEquals(1, listobj.size());
		
		MValue xval = new MValue(MValue.TYPE_LIST);
		xval.forceValue(listobj);
		reg.register("mylist", xval);
		
		MValue val = reg.create("mylist");
		MListValueObject z = val.getList();
		assertEquals(1, z.size());
		
		MValue el100 = z.getIth(0);
		assertEquals("abc", el100.getString());
		el100.forceValue("def");
		
		MValue val2 = reg.create("mylist");
		MListValueObject z2 = val2.getList();
		assertEquals(1, z2.size());
		
		MValue el101 = z2.getIth(0);
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

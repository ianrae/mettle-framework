

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mef.framework.metadata.IntegerValue;
import org.mef.framework.metadata.ListValue;
import org.mef.framework.metadata.StringValue;
import org.mef.framework.metadata.TupleValue;
import org.mef.framework.metadata.Value;
import org.mef.framework.metadata.ValueContainer;
import org.mef.framework.metadata.validate.ValContext;
import org.mef.framework.metadata.validate.ValidationErrorSpec;
import org.mef.framework.metadata.validators.NotEmptyStringValidator;

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
	
	public static class MTypeRegistry
	{
		private HashMap<String, Value> map;
		
		public MTypeRegistry()
		{
			map = new HashMap<String, Value>();
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

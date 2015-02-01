

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Test;
import org.mef.framework.metadata.BooleanValue;
import org.mef.framework.metadata.EnumValue;
import org.mef.framework.metadata.IntegerValue;
import org.mef.framework.metadata.IntegerValueAndValidator;
import org.mef.framework.metadata.ListValue;
import org.mef.framework.metadata.StringValue;
import org.mef.framework.metadata.StringValueAndValidator;
import org.mef.framework.metadata.TupleValue;
import org.mef.framework.metadata.Value;
import org.mef.framework.metadata.ValueContainer;
import org.mef.framework.metadata.validate.ValContext;
import org.mef.framework.metadata.validate.ValidationErrorSpec;
import org.mef.framework.metadata.validate.ValidationErrors;
import org.mef.framework.metadata.validators.NotEmptyStringValidator;
import org.mef.framework.metadata.validators.RangeIntValidator;

import play.i18n.Messages;

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
			lastName = new StringValue(string2);
			firstName.setValidator("firstName", new NotEmptyStringValidator());
			lastName.setValidator("secondName", new NotEmptyStringValidator());

		}

		@Override
		public void validateContainer(ValContext vtx)
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
		
		public BooleanValue demoMode = new BooleanValue(true);

		public void validateContainer(ValContext vtx)
		{
			vtx.validate(retries);
			vtx.validate(weekday);
			joe.validateContainer(vtx);
			vtx.validate(demoMode);
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

		val = new Value(Value.TYPE_BOOLEAN, true);
		assertEquals(true, val.getBoolean());

		val = new Value(Value.TYPE_DOUBLE, 5.4);
		assertEquals(5.4, val.getDouble(), 0.1);
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
		chkContainer(vtx, sys, 0);

		assertEquals("bob", sys.joe.firstName.getString());
	}

	@Test
	public void testValFail() 
	{
		System sys = new System();
		PersonName badjoe = new PersonName("", "smith");
		sys.joe = badjoe;

		ValContext vtx = new ValContext();
		chkContainer(vtx, sys, 1);

		assertEquals("", sys.joe.firstName.getString());
	}

	public static class Coin extends EnumValue
	{
		public static final int PENNY = 1;
		public static final int NICKEL = 5;
		public static final int DIME = 10;
		public static final int QUARTER = 25;

		public Coin(int val)
		{
			super(val, "coin");
		}
		
		protected boolean onValidate(int val) 
		{
			switch(val)
			{
			case Coin.PENNY:
			case Coin.NICKEL:
			case Coin.DIME:
			case Coin.QUARTER:
				return true;
			default:
				return false;
			}
		}
	}	

	@Test
	public void testEnum() 
	{
		Coin coin = new Coin(Coin.DIME);

		ValContext vtx = new ValContext();
		vtx.validate(coin);
		assertEquals(0, vtx.getFailCount());

		coin = new Coin(77);
		chkVal(vtx, coin, 1);
	}
	
	
	public static class MyStringValue extends StringValueAndValidator
	{
		public MyStringValue(String val, String itemName)
		{
			super(val, itemName);
		}
		
		@Override
		public boolean validate(Object val, ValidationErrors errors) 
		{
			String s = (String)val;
			if (! s.contains("a"))
			{
				errors.addError(String.format("missing 'a'"));
				return false;
			}
			return true;
		}
	}
	
	@Test
	public void testStringAndVal() 
	{
		MyStringValue myval = new MyStringValue("abc", "");

		ValContext vtx = new ValContext();
		chkVal(vtx, myval, 0);

		myval = new MyStringValue("bb", "");
		chkVal(vtx, myval, 1);
	}
	
	@Test
	public void testRangeVal() 
	{
		IntegerValue val = new IntegerValue(1);
		val.setValidator("abc", new RangeIntValidator(1, 4));

		ValContext vtx = new ValContext();
		chkVal(vtx, val, 0);
		val.forceValue(4);
		chkVal(vtx, val, 0);
		val.forceValue(5);
		chkVal(vtx, val, 1);
		
		String s = Messages.get("error.required");
		log(s);
	}
	@Test
	public void testFromString()
	{
		String s = "45";
		IntegerValue val = new IntegerValue(0);
		boolean b = val.fromString(s);
		assertEquals(true, b);
		assertEquals(45, val.get());
	}
	@Test
	public void testVarArg()
	{
		String s = vararg1("a", "b", "c");
		assertEquals("abc", s);

		s = vararg1("a", 14, "c");
		assertEquals("a14c", s);
	
		s = vararg2("a", "b", "c");
		assertEquals("abc", s);

		s = vararg2("a", 14, "c");
		assertEquals("a14c", s);
	}
	
	
	private String vararg1(String string, Object... arguments) 
	{
		String s = string;
		for(Object tmp : arguments)
		{
			s += tmp.toString();
		}
		return s;
	}
	
	private String vararg2(String string, Object... arguments) 
	{
		String s = vararg1(string, arguments);
		return s;
	}

	//--helpers--
	private void logValErrors(ValContext vtx)
	{
		for(ValidationErrorSpec spec : vtx.getFlattendErrorList())
		{
			log(String.format("err %s: %s", spec.key, spec.message));
		}
	}
	
	private void chkVal(ValContext vtx, Value val, int expectedValErrors)
	{
		vtx.validate(val);
		assertEquals(expectedValErrors, vtx.getFailCount());
		logValErrors(vtx);
	}
	
	private void chkContainer(ValContext vtx, ValueContainer container, int expectedValErrors)
	{
		container.validateContainer(vtx);
		assertEquals(expectedValErrors, vtx.getFailCount());
		logValErrors(vtx);
	}
	
	/* messages should contain:
	 * error.invalid.java.util.Date=Invalid date value
error.required=This field is required
error.number=Numeric value expected

		these are from messages.default in C:\src\downloadedStuff\play\playframework-master\framework\src\play\src\main\resources
		
		we should define our own and tell developers they need to create a conf/messages (for each lang)
		and put mycroft
	 */
}

package unittests;



import static org.junit.Assert.*;

import org.junit.Test;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import sfx.SfxFileUtils;

public class OtherTests extends BaseTest
{

	@Test
	public void test() 
	{
		ST hello = new ST("Hello, <name>");
		hello.add("name", "World");
		System.out.println(hello.render());		
	}

	@Test
	public void test2() 
	{
		log("--test2--");
		String path = this.getTestFile("test.stg");
		STGroup group = new STGroupFile(path);
		ST st = group.getInstanceOf("decl");
		st.add("type", "int");
		st.add("name", "x");
		st.add("value", 0);
		String result = st.render(); // yields "int x = 0;"	}
		log(result);
	}
//http://www.antlr.org/wiki/display/ST4/Group+file+syntax
	
	@Test
	public void test3() 
	{
		log("--test3--");
		String path = this.getTestFile("test.stg");
		STGroup group = new STGroupFile(path);
		ST st = group.getInstanceOf("classdecl");
		st.add("type", "int");
		st.add("name", "Task");
		String result = st.render(); // yields "int x = 0;"	}
		log(result);
	}
	@Test
	public void test4() 
	{
		log("--test4--");
		String path = this.getTestFile("test.stg");
		STGroup group = new STGroupFile(path);
		ST st = group.getInstanceOf("sampleloop");
		
		st.addAggr("items.{ firstName ,lastName, id }", "Ter", "Parr", 99); // add() uses varargs
		st.addAggr("items.{firstName, lastName ,id}", "Tom", "Burns", 34);		
		String result = st.render(); // yields "int x = 0;"	}
		log(result);
	}
	
}

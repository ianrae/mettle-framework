import static org.junit.Assert.*;

import java.io.InputStream;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.junit.Test;
import org.mef.framework.entitydb.EntityDBPropertyHelper;
import org.mef.framework.sfx.SfxFileUtils;

import testentities.Hotel;
import testentities.StreetAddress;
import tools.BaseTest;
import java.util.Date;
import java.text.*;


public class OtherTests extends BaseTest
{
	@Test
	public void compareTo()
	{
		int i = 4;
		int j = 5;
		int k = 4;
		
		assertEquals(-1, new CompareToBuilder().append(i, j).toComparison());
		assertEquals(0, new CompareToBuilder().append(i, k).toComparison());
		assertEquals(1, new CompareToBuilder().append(j,k).toComparison());
		
		Integer ii = 4;
		Integer jj = 5;
		Integer kk = 4;
		
		assertEquals(-1, new CompareToBuilder().append(ii, jj).toComparison());
		assertEquals(0, new CompareToBuilder().append(ii, kk).toComparison());
		assertEquals(1, new CompareToBuilder().append(jj,kk).toComparison());
	}
	@Test
	public void test() throws Exception 
	{
		String dir = this.getCurrentDirectory();
		log(dir);
		dir = dir.replace("\\.", "");
		log(dir);
		
		//test using \ as separater
		SfxFileUtils utils = new SfxFileUtils();
		String path = utils.PathCombine(dir, "test");
		path = utils.PathCombine(path, "OtherTests.java");
		log(path);
		assertTrue(utils.fileExists(path));
		
		//test using / as separater
		log("now /");
		dir = dir.replace('\\', '/');
		log(dir);
		path = utils.PathCombine(dir, "test");
		path = utils.PathCombine(path, "OtherTests.java");
		log(path);
		assertTrue(utils.fileExists(path));
		
		log("and resources..");
		String resPath = "mgen/resources/presenter/reply.stg";
		InputStream stream = this.getClass().getResourceAsStream(resPath);
		assertNull(stream); //not available in unit tests?

		log("and resources..");
		resPath = "mgen/resources/presenter/reply.stg";
		path = utils.PathCombine(dir, "conf");
		path = utils.PathCombine(path, resPath);
		log(path);
		assertTrue(utils.fileExists(path));
	}

	@Test
	public void testProp()
	{
		Hotel hotel = new Hotel(45L, "flight1", "abc", 44);
		
		EntityDBPropertyHelper<Hotel> helper = new EntityDBPropertyHelper<Hotel>();
		
		String s = (String) helper.getPropertyValue(hotel, "flight");
		assertEquals("flight1", s);
		
		s = (String) helper.getPropertyValue(hotel, "zzzz"); //no such prop
		assertEquals(null, s);
		
		int n = (Integer) helper.getPropertyValue(hotel, "num");
		assertEquals(44, n);
		long lval = (Long) helper.getPropertyValue(hotel, "id");
		assertEquals(45L, lval);
		
		StreetAddress addr = (StreetAddress) helper.getPropertyValue(hotel, "addr");
		assertNull(addr);
		
		Class clazz = helper.getPropertyType(hotel, "id");
		assertEquals("long", clazz.getSimpleName());
		
		//property must be non-null to call get property type
		clazz = helper.getPropertyType(hotel, "addr");
		assertEquals("StreetAddress", clazz.getSimpleName());
	}
	
	@Test
	public void testFmt()
	{
		int planet = 7;
		 String event = "a disturbance in the Force";

		 String result = MessageFormat.format(
		     "At {1,time} on {1,date}, there was {2} on planet {0,number,integer}.",
		     planet, new Date(), event);
		 log(result);

		 result = MessageFormat.format(
			     "on planet {0}.", planet);
			 log(result);
	}	
}

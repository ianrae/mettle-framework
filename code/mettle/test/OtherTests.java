import static org.junit.Assert.*;

import java.io.InputStream;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.junit.Test;
import org.mef.framework.sfx.SfxFileUtils;

import tools.BaseTest;


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

}
